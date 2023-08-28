package den.gubarev.springproject.services.chatServices;

import den.gubarev.springproject.models.Chat;
import den.gubarev.springproject.models.Message;
import den.gubarev.springproject.repositories.chatRepository.ChatRepository;
import den.gubarev.springproject.util.MessageStatus;
import den.gubarev.springproject.repositories.chatRepository.MessageRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;

    @Autowired
    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository, ChatService chatService) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.chatService = chatService;
    }

    public List<Message> findByTaskId(int taskId) {
        System.out.println(taskId);
        Chat chat = chatRepository.findByTaskId(taskId).orElse(null);
        System.out.println(chatRepository.findByTaskId(taskId).isEmpty());
        List<Message> messages = chat == null ?  null : chat.getMessages();
        Hibernate.initialize(messages);
        return messages;
    }

    public List<Message> findByChatId(int chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        List<Message> messages = chat == null ?  null : chat.getMessages();
        Hibernate.initialize(messages);
        return messages;
    }

    @Transactional
    public Message save(Message message) {

        Message newMessage = new Message();

        newMessage.setSenderName(message.getSenderName());
        newMessage.setSenderRole(message.getSenderRole());
        newMessage.setContent(message.getContent());
        newMessage.setStatus(MessageStatus.SENT);

        Chat chat = chatRepository.findByTaskId(message.getChat().getTask()
                .getId()).orElse(null);

        newMessage.setChat(chat);

        messageRepository.save(newMessage);
        return newMessage;
    }

//    public long countNewMessages(String senderId, String recipientId) {
//        return repository.countBySenderIdAndRecipientIdAndStatus(
//                senderId, recipientId, MessageStatus.RECEIVED);
//    }

//    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
//        var chatId = chatRoomService.getChatId(senderId, recipientId, false);
//
//        var messages =
//                chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());
//
////        if(messages.size() > 0) {
////            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
////        }
//
//        return messages;
//    }

//    public ChatMessage findById(String id) {
//        return repository
//                .findById(id)
//                .map(chatMessage -> {
//                    chatMessage.setStatus(MessageStatus.DELIVERED);
//                    return repository.save(chatMessage);
//                })
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("can't find message (" + id + ")"));
//    }
//
//    public void updateStatuses(String senderId, String recipientId, MessageStatus status) {
//        Query query = new Query(
//                Criteria
//                        .where("senderId").is(senderId)
//                        .and("recipientId").is(recipientId));
//        Update update = Update.update("status", status);
//        mongoOperations.updateMulti(query, update, ChatMessage.class);
//    }
}
