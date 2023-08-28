package den.gubarev.springproject.services.chatServices;

import den.gubarev.springproject.models.Chat;
import den.gubarev.springproject.models.Employee;
import den.gubarev.springproject.models.Task;
import den.gubarev.springproject.repositories.chatRepository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Transactional
    public void createAndSaveWithTask(Task task) {
        Chat chat = new Chat();
        chat.setTask(task);
        chatRepository.save(chat);
    }


//    public Optional<String> getChatId(
//            String senderId, String recipientId, boolean createIfNotExist) {

//         return chatRoomRepository
//                .findBySenderIdAndRecipientId(senderId, recipientId)
//                .map(ChatRoom::getChatId)
//                 .or(() -> {
//                    if(!createIfNotExist) {
//                        return  Optional.empty();
//                    }
//                     var chatId =
//                            String.format("%s_%s", senderId, recipientId);
//
//                    ChatRoom senderRecipient = ChatRoom
//                            .builder()
//                            .chatId(chatId)
//                            .senderId(senderId)
//                            .recipientId(recipientId)
//                            .build();
//
//                    ChatRoom recipientSender = ChatRoom
//                            .builder()
//                            .chatId(chatId)
//                            .senderId(recipientId)
//                            .recipientId(senderId)
//                            .build();
//                    chatRoomRepository.save(senderRecipient);
//                    chatRoomRepository.save(recipientSender);
//
//                    return Optional.of(chatId);
//                });
//    }
}
