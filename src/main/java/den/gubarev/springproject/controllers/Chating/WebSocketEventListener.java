package den.gubarev.springproject.controllers.Chating;

import den.gubarev.springproject.models.Chat;
import den.gubarev.springproject.models.Message;
import den.gubarev.springproject.util.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Created by rajeevkumarsingh on 25/07/17.
 */
@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("senderName");
        int taskId = (int) headerAccessor.getSessionAttributes().get("taskId");
        System.out.println(taskId);
        if(username != null) {
            logger.info("User Disconnected : " + username);

            Message message = new Message();
//            Chat chat = new Chat();
//
//            message.setStatus(MessageStatus.DELIVERED);
//            message.setChat(chat);

            // Отправляет сообщение
            messagingTemplate.convertAndSend("/topic/public", message);
        }
    }
}
