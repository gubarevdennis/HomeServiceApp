package den.gubarev.springproject.controllers;

import den.gubarev.springproject.models.Employee;
import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.services.EmployeeService;
import den.gubarev.springproject.services.ResidentService;
import den.gubarev.springproject.util.EmployeeValidator;
import den.gubarev.springproject.util.ResidentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final EmployeeValidator employeeValidator;
    private final ResidentValidator residentValidator;
    private final EmployeeService employeeService;
    private final ResidentService residentService;

    @Autowired
    public AuthController(EmployeeValidator employeeValidator, ResidentValidator residentValidator, EmployeeService employeeService, ResidentService residentService) {
        this.employeeValidator = employeeValidator;
        this.residentValidator = residentValidator;
        this.employeeService = employeeService;
        this.residentService = residentService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/registration-resident")
    public String registrationResidentPage(@ModelAttribute("resident") Resident resident){
        return "auth/registration-resident";
    }

    @GetMapping("/registration-employee")
    public String registrationEmployeePage(@ModelAttribute("employee") Employee employee){
        return "auth/registration-employee";
    }

    @PostMapping("/registration-resident")
    public String performRegistrationResidentPage(@ModelAttribute("resident") Resident resident,
                                                  BindingResult bindingResult){

        residentValidator.validate(resident, bindingResult);

        if (bindingResult.hasErrors())
            return "redirect:/auth/registration-resident";

        residentService.save(resident);
        return "redirect:/auth/login";
    }

    @PostMapping("/registration-employee")
    public String performRegistrationEmployeePage(@ModelAttribute("employee") @Valid Employee employee,
                                                  BindingResult bindingResult){

        employeeValidator.validate(employee, bindingResult);

        if (bindingResult.hasErrors())
            return "redirect:/auth/registration-employee";

        employeeService.save(employee);

        return "redirect:/auth/login";
    }

    @GetMapping("/admin")
    public String adminPage(Model model, @ModelAttribute("resident") Resident resident) {
        model.addAttribute("residentAll", residentService.findAll());
        return "adminPage";
    }

//    @PostMapping("/admin/add")
//    public String makeAdmin(Model model, @ModelAttribute("resident") Resident resident) {
//
//        return "redirect:/resident";
//    }

}
