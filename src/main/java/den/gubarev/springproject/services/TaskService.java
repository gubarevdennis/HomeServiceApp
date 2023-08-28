package den.gubarev.springproject.services;


import den.gubarev.springproject.models.Employee;
import den.gubarev.springproject.models.Task;
import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.repositories.EmployeeRepository;
import den.gubarev.springproject.repositories.OrderRepository;
import den.gubarev.springproject.repositories.ResidentRepository;
import den.gubarev.springproject.services.chatServices.ChatService;
import den.gubarev.springproject.util.TaskStatus;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final OrderRepository orderRepository;
    private final ResidentRepository residentRepository;
    private final EmployeeRepository employeeRepository;
    private final ChatService chatService;

    @Autowired
    public TaskService(OrderRepository orderRepository, ResidentRepository residentRepository, EmployeeRepository employeeRepository, ChatService chatService) {
        this.orderRepository = orderRepository;
        this.residentRepository = residentRepository;
        this.employeeRepository = employeeRepository;
        this.chatService = chatService;
    }

    @Transactional(readOnly = true)
    public List<Task> findAll(String sortByField) {
        return orderRepository.findAll(Sort.by(sortByField));
    }

    @Transactional(readOnly = true)
    // Отсоритрованный список
    public List<Task> findAll(int page, int orderPerPage, String sortByField) {
        return orderRepository.findAll(PageRequest.of(page,orderPerPage, Sort.by(sortByField))).getContent();
    }

    @Transactional(readOnly = true)
    public Optional<Task> findAnyOrderByEmployee(Task task, Employee employee) {
      //  return orderRepository.findOrdersByEmployees(order, employee).stream().findAny();
        return null;
    }

    @Transactional(readOnly = true)
    public Task findOne(int id) {
        Optional<Task> foundOrder = orderRepository.findById(id);
        return foundOrder.orElse(null);
    }

    @Transactional(readOnly = true)
    public List <Task> showByResidentId(int residentId) {
        Resident resident = residentRepository.findById(residentId).orElse(null);
        List <Task> tasks = resident == null ? null : resident.getTasks();
        Hibernate.initialize(tasks);
        return tasks;
    }

    @Transactional(readOnly = true)
    public List <Task> showByEmployeeId(int residentId) {
        Employee employee = employeeRepository.findById(residentId).orElse(null);
        List <Task> tasks = employee == null ? null : employee.getTasks();
        Hibernate.initialize(tasks);
        return tasks;
    }

    @Transactional
    public void save(Task task, int residentId) {

        // Почему-то поле id кэшировалась при нескольких вызовах, сделал фильтр через ноый объект
        Task newTask = new Task();

        newTask.setName(task.getName());
        newTask.setMessage(task.getMessage());

        // Присваивание статуса заказу
        newTask.setStatus(TaskStatus.SENT.toString());

        // Время отправки заказа
        newTask.setDateAndTimeOfSend(task.createDateAndTime());

        // Присваивание заказа жителю
        Optional<Resident> resident = residentRepository.findById(residentId);

        resident.ifPresent(
                r ->  r.addTask(newTask));

        // Сохраниение в базу данных заказа
        orderRepository.save(newTask);

        // Создание чата под заказ
        chatService.createAndSaveWithTask(newTask);

    }

    @Transactional
    public void update(int id, Task updatedTask) {
        Optional<Task> order = orderRepository.findById(id);

        order.ifPresent(o -> {
            o.setName(updatedTask.getName());
            o.setStatus(updatedTask.getStatus());
            o.setMessage(updatedTask.getMessage());
        });

    }

    @Transactional
    public void updateStatus(int id, TaskStatus status) {
        Optional<Task> order = orderRepository.findById(id);

        // Установка статуса заказа
        order.ifPresent(o -> {
            o.setStatus(status.toString());

        // Установка времени взятия заказа в работу или завершения заказа
            switch (status) {
                case IN_PROGRESS: {
                    o.setDateAndTimeOfGet(o.createDateAndTime());
                }
                break;
                case DONE: {
                    o.setDateAndTimeOfDone(o.createDateAndTime());
                }
            }
        });
    }

    @Transactional(readOnly = true)
    public double showAverageGrade(int employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        // Расчет средней оценки по уже выполненым заказам работника
        double averageGrade = employee.isEmpty() ? 0.0 : employee.get().getTasks().stream()
                .filter(t -> t.getStatus().equalsIgnoreCase(TaskStatus.DONE.toString()))
                .mapToDouble(Task::getGrade).average().orElse(0.0);

        return averageGrade;
    }

    @Transactional
    public void updateGrade(int id, double grade) {
        Optional<Task> order = orderRepository.findById(id);

        List<Employee> assignedEmployee = order.isEmpty() ? null : order.get().getEmployees();

            // Вынести в валидацию
        if (order.isPresent()) {
            // Проверка на то, что заказ уже выполнен
            if (order.get().getStatus().equalsIgnoreCase(TaskStatus.DONE.toString())
                    && !(order.get().getGrade() > 0)) {
                order.get().setGrade(grade);

                // Пересчет ретинга работников заказа и обновление их рейтинга
                for (Employee employee :
                        assignedEmployee) {
                    // Расчет рейтинга
                    double newGrade = this.showAverageGrade(employee.getId());
                    employee.setGrade(newGrade);
                }
            }
        }

    }

    @Transactional
    public void delete(int id) {
        orderRepository.deleteById(id);
    }

}
