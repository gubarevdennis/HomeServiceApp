package den.gubarev.springproject.controllers.mainService;

import den.gubarev.springproject.models.Employee;
import den.gubarev.springproject.services.EmployeeService;
import den.gubarev.springproject.services.TaskService;
import den.gubarev.springproject.util.EmployeeValidator;
import den.gubarev.springproject.util.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final TaskService taskService;

    private final EmployeeValidator employeeValidator;

    @Autowired
    public EmployeeController(EmployeeService employeeService, TaskService taskService, EmployeeValidator employeeValidator) {
        this.employeeService = employeeService;
        this.taskService = taskService;
        this.employeeValidator = employeeValidator;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "employee_per_page", required = false) Integer employeePerPage) {
        if (page == null || employeePerPage == null) {
            model.addAttribute("employeeAll", employeeService.findAll("name"));
        } else {
            //Получим всех employee и отобразим в предсталении
            model.addAttribute("employeeAll", employeeService.findAll(page,employeePerPage,"name"));
        }
        return "employee/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        //Получим одного человека по его id из DAO и отобразим
        model.addAttribute("employee", employeeService.findOne(id));
        model.addAttribute("assignedTask", taskService.showByEmployeeId(id));
        return "employee/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("employee") Employee employee) {
        return "employee/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("employee") Employee employee,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "employee/new";

        employeeService.save(employee);
        return "redirect:/employee";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("employee", employeeService.findOne(id));
        return "employee/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "employee/edit";

        employeeService.update(id, employee);
        return "redirect:/employee";
    }

    @PatchMapping("/takeOrder/{id}")
    public String takeOrder(@ModelAttribute("employee") Employee employee, @PathVariable("id") int orderId,
                            BindingResult bindingResult) {
        // Передаем id для валидации привязки работника
        employeeValidator.setTask(taskService.findOne(orderId));

        // Сама валидация
        employeeValidator.validate(employee,bindingResult);

        if (bindingResult.hasErrors())
            return "redirect:/task/" + orderId;

        if (!taskService.findOne(orderId).getStatus().equals(TaskStatus.DONE.toString())) {
            employeeService.takeTask(orderId, employee.getId());
//        System.out.println("Order ID: " + orderId + " Employee ID: " + employee.getId());
            taskService.updateStatus(orderId, TaskStatus.IN_PROGRESS);
        }
            return "redirect:/task/" + orderId;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        employeeService.delete(id);
        return "redirect:/employee";
    }
}
