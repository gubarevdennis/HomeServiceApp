package den.gubarev.springproject.controllers.mainService;


import den.gubarev.springproject.models.Employee;
import den.gubarev.springproject.models.Message;
import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.models.Task;
import den.gubarev.springproject.security.EmployeeDetails;
import den.gubarev.springproject.security.ResidentDetails;
import den.gubarev.springproject.services.EmployeeService;
import den.gubarev.springproject.services.TaskService;
import den.gubarev.springproject.services.chatServices.ChatService;
import den.gubarev.springproject.services.chatServices.MessageService;
import den.gubarev.springproject.util.EmployeeValidator;
import den.gubarev.springproject.util.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {

    private final EmployeeValidator employeeValidator;
    private final TaskService taskService;
    private final EmployeeService employeeService;
    private final ChatService chatService;

    @Autowired
    public TaskController(EmployeeValidator employeeValidator, TaskService taskService, EmployeeService employeeService, ChatService chatService ) {
        this.employeeValidator = employeeValidator;
        this.taskService = taskService;
        this.employeeService = employeeService;
        this.chatService = chatService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "task_per_page", required = false) Integer orderPerPage) {
        if (page == null || orderPerPage == null) {
            model.addAttribute("taskAll", taskService.findAll("name"));
        }
            else {
                model.addAttribute("taskAll", taskService.findAll( page,orderPerPage,"name"));
            }
        return "task/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("employee") Employee employee, Model model) {
        //Получим одного человека по его id из DAO и отобразим
        model.addAttribute("task", taskService.findOne(id));
        model.addAttribute("employeeAll",employeeService.findAll("name"));
        model.addAttribute("assignedEmployee",employeeService.indexAssignedEmployee(id));
       // model.addAttribute("resident", taskService.findOne(id).getResident());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof ResidentDetails) {
            ResidentDetails residentDetails = (ResidentDetails) authentication.getPrincipal();
            model.addAttribute("resident", residentDetails.getResident());
        } else {
            EmployeeDetails employeeDetails = (EmployeeDetails) authentication.getPrincipal();
            model.addAttribute("resident", employeeDetails.getEmployee());
        }



        return "task/show";
    }

    @GetMapping("/new")
    public String newOrder(@ModelAttribute("task") Task task) {
        return "task/new";
    }

    @PostMapping("/assign/{id}")
    public String createAssigned(@ModelAttribute("task") Task task, @PathVariable("id") int residentId,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "task/new";

        taskService.save(task, residentId);


        return "redirect:/resident/" + residentId;
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("task", taskService.findOne(id));
        return "task/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("task") Task task, BindingResult bindingResult , @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "task/edit";

        taskService.update(id, task);
        return "redirect:/task";
    }

    @PatchMapping("/status/{id}")
    public String updateStatus(@PathVariable("id") int id) {
        //Вынести в валидацию
        if (!taskService.findOne(id).getStatus()
                .equalsIgnoreCase(TaskStatus.DONE.toString()))
        taskService.updateStatus(id, TaskStatus.DONE);
        return "redirect:/task/" + id;
    }

    @PatchMapping("/grade/{id}")
    public String updateGrade(@ModelAttribute("task") Task task, BindingResult bindingResult,
                              @PathVariable("id") int id) {
        System.out.println(bindingResult.getModel());
        if (bindingResult.hasErrors())
            return "redirect:/task";
        taskService.updateGrade(id, task.getGrade());
        return "redirect:/task/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        taskService.delete(id);
        return "redirect:/task";
    }

}
