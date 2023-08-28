package den.gubarev.springproject.models;

import org.hibernate.annotations.Cascade;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "Chat")
public class Chat {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "sender_role")
    private String senderRole;

    @Column(name = "recipient_id")
    private int recipientId;

    @OneToMany(mappedBy = "chat")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Message> messages;

    @OneToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    public Chat() {
    }

    public Chat(int id, String senderName, String senderRole, int recipientId) {
        this.id = id;
        this.senderName = senderName;
        this.senderRole = senderRole;
        this.recipientId = recipientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderId) {
        this.senderName = senderId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }


    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String createDateAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd-MMMM-yyyy", Locale.ENGLISH);
        return dtf.format(LocalDateTime.now());
    }

    public void addMessage(Message message) {
        if (this.messages == null)
            this.messages = new ArrayList<>();

        this.messages.add(message);
        message.setChat(this);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", senderName='" + senderName + '\'' +
                ", senderRole='" + senderRole + '\'' +
                ", recipientId=" + recipientId +
                ", messages=" + messages +
                ", task=" + task +
                '}';
    }

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }
}
