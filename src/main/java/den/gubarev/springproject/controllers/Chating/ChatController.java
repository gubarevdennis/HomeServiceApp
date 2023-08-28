package den.gubarev.springproject.controllers.Chating;

import den.gubarev.springproject.dto.MessageDTO;
import den.gubarev.springproject.models.Chat;
import den.gubarev.springproject.models.Message;
import den.gubarev.springproject.security.ResidentDetails;
import den.gubarev.springproject.services.chatServices.ChatService;
import den.gubarev.springproject.services.chatServices.MessageService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.stream.Collectors;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
@Controller
public class ChatController {

    private final MessageService messageService;
    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ModelMapper modelMapper;

    @Autowired
    public ChatController(MessageService messageService, ChatService chatService, SimpMessagingTemplate simpMessagingTemplate,
                          ModelMapper modelMapper) {
        this.messageService = messageService;
        this.chatService = chatService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.modelMapper = modelMapper;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message) {
        int taskId = message.getChat().getTask().getId();
        messageService.save(message);
        this.simpMessagingTemplate.convertAndSend("/topic/public-" + taskId, message);
    }

    @MessageMapping("/chat.createChat")
    public Chat createChat(@Payload Chat chat,
                           SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("senderName", chat.getSenderName());
        headerAccessor.getSessionAttributes().put("senderRole", chat.getSenderRole());
        headerAccessor.getSessionAttributes().put("taskId", chat.getTask().getId());

        int taskId = chat.getTask().getId();
        this.simpMessagingTemplate.convertAndSend("/topic/public-" + taskId,
                messageService.findByTaskId(taskId).stream()
                        .map(this::convertToMessageDTO).collect(Collectors.toList()));
        System.out.println(chat.getTask().getId());
        return chat;
    }

    public MessageDTO convertToMessageDTO(Message message) {
        return modelMapper.map(message, MessageDTO.class);
    }


}
