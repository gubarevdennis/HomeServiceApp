package den.gubarev.springproject.repositories.chatRepository;


import den.gubarev.springproject.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository
        extends JpaRepository<Message, Integer> {
//
//    long countBySenderNameAndRecipientIdAndStatus(
//            String senderName, String recipientId, MessageStatus status);


    List<Message> findByChatId(String chatId);
}