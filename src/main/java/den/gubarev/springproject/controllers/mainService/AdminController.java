//package den.gubarev.springproject.controllers;
//
//import den.gubarev.springproject.DAO.ResidentDAO;
//import den.gubarev.springproject.models.Resident;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/admin")
//public class AdminController {
//
//    private final ResidentDAO personDAO;
//
//    @Autowired
//    public AdminController(ResidentDAO personDAO) {
//        this.personDAO = personDAO;
//    }
//
//    @GetMapping
//    // @ModelAttribute нужна для:
//    // 1) создания пустого объекта person
//    // 2) вставить в него нужный параметры (значение полей)
//    // 3) добавление объекта в модель, чтобы отобразить на странице
//    public String adminPage(Model model, @ModelAttribute("resident") Resident resident) {
//        model.addAttribute("residentAll", personDAO.index());
//        return "adminPage";
//    }
//
//    @PatchMapping("/add")
//    public String makeAdmin(@ModelAttribute("resident") Resident resident) {
//        System.out.println(resident.getId());
//        return "redirect:/resident";
//    }
//
//}
