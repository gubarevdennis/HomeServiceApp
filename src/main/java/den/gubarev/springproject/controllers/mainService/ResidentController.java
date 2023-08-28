package den.gubarev.springproject.controllers.mainService;

//import den.gubarev.springMVC.DAO.OrderDAO;

import den.gubarev.springproject.models.Task;
import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.services.TaskService;
import den.gubarev.springproject.services.ResidentService;
import den.gubarev.springproject.util.ResidentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/resident")
public class ResidentController {

    private final ResidentService residentService;
    private final TaskService taskService;

    private final ResidentValidator residentValidator;

    @Autowired
    public ResidentController(ResidentService residentService, TaskService taskService, ResidentValidator residentValidator) {
        this.residentService = residentService;
        this.taskService = taskService;
        this.residentValidator = residentValidator;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "resident_per_page", required = false) Integer residentPerPage) {
        if (page == null || residentPerPage == null) {
            model.addAttribute("residentAll", residentService.findAll("name"));
        } else {
            //Получим всех людей и отобразим в предсталении
            model.addAttribute("residentAll", residentService.findAll(page,residentPerPage,"name"));
        }
        return "resident/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {

        //Получим одного человека по его id из DAO и отобразим
        model.addAttribute("resident", residentService.findOne(id));
        model.addAttribute("assignedTask", taskService.showByResidentId(id));
        model.addAttribute("task", new Task());
        return "resident/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("resident") Resident person) {
        return "resident/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("resident") Resident resident,
                         BindingResult bindingResult) {
        // Если есть ошибки валидации
        if (bindingResult.hasErrors()) {
            return "resident/new";
        }

        residentService.save(resident);
        return "redirect:/resident";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("resident", residentService.findOne(id));
        return "resident/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("resident") @Valid Resident resident, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "resident/edit";

        residentService.update(id, resident);
        return "redirect:/resident";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        residentService.delete(id);
        return "redirect:/resident";
    }
}
