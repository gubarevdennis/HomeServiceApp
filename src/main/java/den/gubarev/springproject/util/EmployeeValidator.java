package den.gubarev.springproject.util;


import den.gubarev.springproject.models.Employee;
import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.models.Task;
import den.gubarev.springproject.services.EmployeeService;
import den.gubarev.springproject.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmployeeValidator implements Validator {

    private Task task;
//    private final OrderDAO orderDAO;
    private final TaskService taskService;
    // нужен для объектов класса Resident
    @Override
    public boolean supports(Class<?> clazz) {
        return Resident.class. equals(clazz);
    }

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeValidator(TaskService taskService, EmployeeService employeeService) {
        this.taskService = taskService;
        this.employeeService = employeeService;
    }
    @Override
    public void validate(Object o, Errors errors) {
        // есть ли человек с таким же емаилом
        Employee employee = (Employee) o;

        System.out.println("----->" + employeeService.findOne(employee.getName()));

        if (employeeService.findOne(employee.getName()) != null) { // проверка на null
            errors.rejectValue("name", "", "Работник с таким именем пользователя существует!");
        }
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
