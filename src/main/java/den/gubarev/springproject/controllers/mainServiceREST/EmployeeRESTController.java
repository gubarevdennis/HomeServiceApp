package den.gubarev.springproject.controllers.mainServiceREST;

import den.gubarev.springproject.dto.EmployeeDTO;
import den.gubarev.springproject.models.Employee;
import den.gubarev.springproject.services.EmployeeService;
import den.gubarev.springproject.services.TaskService;
import den.gubarev.springproject.util.Errors.*;
import den.gubarev.springproject.util.TaskStatus;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee")
public class EmployeeRESTController {

    private final EmployeeService employeeService;
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    public EmployeeRESTController(EmployeeService employeeService, TaskService taskService, ModelMapper modelMapper) {
        this.employeeService = employeeService;
        this.taskService = taskService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<EmployeeDTO> getResident() {
        return employeeService.findAll("name").stream().map(this::convertToEmployeeDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EmployeeDTO show(@PathVariable("id") int id) {
        return convertToEmployeeDTO(employeeService.findOne(id));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid EmployeeDTO employeeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            // Лист ошибок
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                // получаем ошибки полей и конкетинируем инфу
                errorMessage.append(error.getField())
                        .append("-")
                        .append(error.getDefaultMessage())
                        .append(";");
            }

            throw new EmployeeNotCreatedException(errorMessage.toString());
        }

        employeeService.save(convertToEmployee(employeeDTO));

        // ответ с пустым телом и статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/takeOrder/{id}")
    public ResponseEntity<HttpStatus> takeOrder(@RequestBody EmployeeDTO employeeDTO, @PathVariable("id") int taskId,
                                                BindingResult bindingResult) {
        // Передаем id для валидации привязки работника
//        employeeValidator.setTask(taskService.findOne(orderId));
//
//        // Сама валидация
//        employeeValidator.validate(employee,bindingResult);

        if (bindingResult.hasErrors())
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);

        System.out.println(employeeDTO.getId());
        if (!taskService.findOne(taskId).getStatus().equals(TaskStatus.DONE.toString())) {
            employeeService.takeTask(taskId, convertToEmployee(employeeDTO).getId());
//        System.out.println("Order ID: " + orderId + " Employee ID: " + employee.getId());
            taskService.updateStatus(taskId, TaskStatus.IN_PROGRESS);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<EmployeeErrorResponse> handleException(ResidentNotFoundException e) {
        EmployeeErrorResponse employeeErrorResponse = new EmployeeErrorResponse(
                "No resident found with such id",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(employeeErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<EmployeeErrorResponse> handleException(ResidentNotCreatedException e) {
        EmployeeErrorResponse employeeErrorResponse = new EmployeeErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(employeeErrorResponse, HttpStatus.BAD_REQUEST);
    }

    public Employee convertToEmployee(EmployeeDTO employeeDTO) {
        return modelMapper.map(employeeDTO, Employee.class);
    }

    public EmployeeDTO convertToEmployeeDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeDTO.class);
    }

}
