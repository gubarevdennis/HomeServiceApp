package den.gubarev.springproject.repositories.chatRepository;

import den.gubarev.springproject.models.Chat;
import den.gubarev.springproject.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    Optional<Chat> findBySenderNameAndRecipientId(String senderName, String recipientId);

    Optional<Chat> findByTaskId(int taskId);
}
