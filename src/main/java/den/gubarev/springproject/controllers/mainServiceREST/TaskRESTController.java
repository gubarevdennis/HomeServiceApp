package den.gubarev.springproject.controllers.mainServiceREST;

import den.gubarev.springproject.dto.TaskDTO;
import den.gubarev.springproject.models.Message;
import den.gubarev.springproject.models.Task;
import den.gubarev.springproject.services.TaskService;
import den.gubarev.springproject.services.chatServices.MessageService;
import den.gubarev.springproject.util.Errors.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task")
public class TaskRESTController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;
    private final MessageService messageService;

    @Autowired
    public TaskRESTController(ModelMapper modelMapper, TaskService taskService, MessageService messageService) {
        this.modelMapper = modelMapper;
        this.taskService = taskService;
        this.messageService = messageService;
    }

    @GetMapping
    public List<TaskDTO> getTask() {
        return taskService.findAll("name").stream().map(this::convertToTaskDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TaskDTO show(@PathVariable("id") int id) {
        return convertToTaskDTO(taskService.findOne(id));
    }

    @PostMapping("/assign/{id}")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid TaskDTO taskDTO, @PathVariable("id") int residentId,BindingResult bindingResult) {
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

            throw new TaskNotCreatedException(errorMessage.toString());
        }

        taskService.save(convertToTask(taskDTO), residentId);

        // ответ с пустым телом и статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/grade/{id}")
    public ResponseEntity<HttpStatus> updateGrade(@RequestBody TaskDTO taskDTO, BindingResult bindingResult,
                              @PathVariable("id") int id) {
        System.out.println(bindingResult.getModel());
        if (bindingResult.hasErrors())
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        taskService.updateGrade(id, convertToTask(taskDTO).getGrade());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<TaskErrorResponse> handleException(TaskNotFoundException e) {
        TaskErrorResponse taskErrorResponse = new TaskErrorResponse(
                "No task found with such id",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(taskErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<TaskErrorResponse> handleException(TaskNotCreatedException e) {
        TaskErrorResponse taskErrorResponse = new TaskErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(taskErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/chat/{id}")
    public List<Message> showOldMessages(@PathVariable("id") int chatId) {
        return messageService.findByTaskId(chatId);
    }

    public Task convertToTask(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }

    public TaskDTO convertToTaskDTO(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }
}
