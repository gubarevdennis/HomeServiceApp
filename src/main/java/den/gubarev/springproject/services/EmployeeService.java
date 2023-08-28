package den.gubarev.springproject.services;


import den.gubarev.springproject.models.Employee;
import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.models.Task;
import den.gubarev.springproject.repositories.EmployeeRepository;
import den.gubarev.springproject.repositories.OrderRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Employee> findAll(String sortByField) {
        return employeeRepository.findAll(Sort.by(sortByField));
    }

    public List<Employee> findAll(int page, int orderPerPage, String sortByField) {
        return employeeRepository.findAll(PageRequest.of(page,orderPerPage, Sort.by(sortByField))).getContent();
    }

    public Employee findOne(int id) {
        Optional<Employee> foundEmployee = employeeRepository.findById(id);
        return foundEmployee.orElse(null);
    }

    public Employee findOne(String name) {
        Optional<Employee> foundEmployee = employeeRepository.findByName(name);
        return foundEmployee.orElse(null);
    }

    public List<Employee> indexAssignedEmployee(int id) {
        Task task = orderRepository.findById(id).orElse(null);
        List<Employee> employees = task == null ?  null : task.getEmployees();
        Hibernate.initialize(employees);
        return employees;
    }

    @Transactional
    public void save(Employee employee) {
        // Шифруем пароль
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(encodedPassword);
        // Время создания аккаунта
        employee.setDateAndTimeOfCreate(employee.createDateAndTime());
        // Задаем роль
        employee.setRole("ROLE_EMPLOYEE");

        employeeRepository.save(employee);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void update(int id, Employee updatedEmployee) {
        Optional<Employee> employee = employeeRepository.findById(id);

        employee.ifPresent(e -> {
            e.setName(updatedEmployee.getName());
            e.setPosition(updatedEmployee.getPosition());
            e.setCompany(updatedEmployee.getCompany());
            e.setGrade(updatedEmployee.getGrade());
            e.setInformation(updatedEmployee.getInformation());
        });
    }

    @Transactional
    public void updateGrade(int id, double grade) {
        Optional<Employee> employee = employeeRepository.findById(id);

        employee.ifPresent(e -> {
            e.setGrade(grade);
        });
    }

    @Transactional
    public void takeTask(int taskId, int employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        System.out.println(employeeId);
        Optional<Task> order = orderRepository.findById(taskId);

        employee.ifPresent(e -> e.addTask(order.orElse(null))); // проверить работу!

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }

}
