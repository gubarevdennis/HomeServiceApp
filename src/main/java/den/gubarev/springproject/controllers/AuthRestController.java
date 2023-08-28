package den.gubarev.springproject.controllers;

import den.gubarev.springproject.dto.AuthenticationDTO;
import den.gubarev.springproject.dto.EmployeeDTO;
import den.gubarev.springproject.dto.ResidentDTO;
import den.gubarev.springproject.models.Employee;
import den.gubarev.springproject.models.Resident;
import den.gubarev.springproject.security.JWTUtil;
import den.gubarev.springproject.services.EmployeeService;
import den.gubarev.springproject.services.ResidentService;
import den.gubarev.springproject.util.EmployeeValidator;
import den.gubarev.springproject.util.ResidentValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

        private final EmployeeValidator employeeValidator;
        private final ResidentValidator residentValidator;
        private final EmployeeService employeeService;
        private final ResidentService residentService;
        private final AuthenticationManager authenticationManager;

        private final JWTUtil jwtUtil;
        private final ModelMapper modelMapper;

        @Autowired
        public AuthRestController(EmployeeValidator employeeValidator, ResidentValidator residentValidator,
                                  EmployeeService employeeService, ResidentService residentService, AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                                  ModelMapper modelMapper) {
            this.employeeValidator = employeeValidator;
            this.residentValidator = residentValidator;
            this.employeeService = employeeService;
            this.residentService = residentService;
            this.authenticationManager = authenticationManager;
            this.jwtUtil = jwtUtil;
            this.modelMapper = modelMapper;
        }

        @PostMapping("/registration-resident")
        public Map<String, String> performRegistrationResidentPage(@RequestBody @Valid ResidentDTO residentDTO,
                                                                   BindingResult bindingResult){

            Resident resident = convertToResident(residentDTO);

            residentValidator.validate(resident, bindingResult);

            if (bindingResult.hasErrors()) {
                return Map.of("message", bindingResult.toString()); // по хорошему выбрасывать ошибку и овить ее exception handlerом
            }

            residentService.save(resident);

            String token = jwtUtil.generateToken(resident.getName());
            return Map.of("jwt-token",token);
        }

        @PostMapping("/registration-employee")
        public Map<String, String> performRegistrationEmployeePage(@ModelAttribute("employee") @Valid EmployeeDTO employeeDTO,
                                                                   BindingResult bindingResult){

            Employee employee = convertToEmployee(employeeDTO);

            employeeValidator.validate(employee, bindingResult);

            if (bindingResult.hasErrors()) {
                return Map.of("message", bindingResult.toString()); // по хорошему выбрасывать ошибку и овить ее exception handlerом
            }

            employeeService.save(employee);

            String token = jwtUtil.generateToken(employee.getName());
            return Map.of("jwt-token",token);
        }

        // Получение токена
    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO){
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            Map.of("message", "Incorrect credentionals");
        }

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());

        return Map.of("jwt-token",token);
    }

//        @GetMapping("/admin")
//        public String adminPage(Model model, @ModelAttribute("resident") Resident resident) {
//            model.addAttribute("residentAll", residentService.findAll());
//            return "adminPage";
//        }

//    @PostMapping("/admin/add")
//    public String makeAdmin(Model model, @ModelAttribute("resident") Resident resident) {
//
//        return "redirect:/resident";
//    }

    public Resident convertToResident(ResidentDTO residentDTO) {
        return modelMapper.map(residentDTO, Resident.class);
    }

    public ResidentDTO convertToResidentDTO(Resident resident) {
        return modelMapper.map(resident, ResidentDTO.class);
    }

    public Employee convertToEmployee(EmployeeDTO employeeDTO) {
        return modelMapper.map(employeeDTO, Employee.class);
    }

    public EmployeeDTO convertToEmployeeDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeDTO.class);
    }

}

