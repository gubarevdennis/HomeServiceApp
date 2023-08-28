package den.gubarev.springproject.controllers.mainServiceREST;

import den.gubarev.springproject.dto.MessageDTO;
import den.gubarev.springproject.models.Message;
import den.gubarev.springproject.services.TaskService;
import den.gubarev.springproject.services.chatServices.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class MessageRESTController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;
    private final MessageService messageService;

    @Autowired
    public MessageRESTController(ModelMapper modelMapper, TaskService taskService, MessageService messageService) {
        this.modelMapper = modelMapper;
        this.taskService = taskService;
        this.messageService = messageService;
    }

    @GetMapping("/{id}")
    public List<MessageDTO> showOldMessages(@PathVariable("id") int taskId) {
        return messageService.findByTaskId(taskId).stream()
                .map(this::convertToMessageDTO).collect(Collectors.toList());
    }

    public Message convertToMessage(MessageDTO messageDTO) {
        return modelMapper.map(messageDTO, Message.class);
    }

    public MessageDTO convertToMessageDTO(Message message) {
        return modelMapper.map(message, MessageDTO.class);
    }
}
