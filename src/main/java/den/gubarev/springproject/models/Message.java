package den.gubarev.springproject.models;

import den.gubarev.springproject.util.MessageStatus;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "Message")
public class Message {

   @Id
   @Column(name = "id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;

   @ManyToOne
   @JoinColumn(name = "chat_id", referencedColumnName = "id")
   private Chat chat;

   @Column(name = "sender_name")
   private String senderName;

   @Column(name = "sender_role")
   private String senderRole;

   @Column(name = "recipient_id")
   private int recipientId;

   @Column(name = "recipient_name")
   private String recipientName;

   @Column(name = "content")
   private String content;

   @Column(name = "timestamp")
   private Date timestamp;

   @Column(name = "status")
   private MessageStatus status;

   public Message() {
   }

   public Message(int id, String senderRole, int recipientId, String senderName, String recipientName, String content, Date timestamp, MessageStatus status) {
      this.id = id;
      this.senderRole = senderRole;
      this.senderName = senderName;
      this.recipientId = recipientId;
      this.recipientName = recipientName;
      this.content = content;
      this.timestamp = timestamp;
      this.status = status;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public Chat getChat() {
      return chat;
   }

   public void setChat(Chat chat) {
      this.chat = chat;
   }

   public String getSenderName() {
      return senderName;
   }

   public int getRecipientId() {
      return recipientId;
   }

   public void setRecipientId(int recipientId) {
      this.recipientId = recipientId;
   }

   public void setSenderName(String senderName) {
      this.senderName = senderName;
   }

   public String getRecipientName() {
      return recipientName;
   }

   public void setRecipientName(String recipientName) {
      this.recipientName = recipientName;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public Date getTimestamp() {
      return timestamp;
   }

   public void setTimestamp(Date timestamp) {
      this.timestamp = timestamp;
   }

   public MessageStatus getStatus() {
      return status;
   }

   public void setStatus(MessageStatus status) {
      this.status = status;
   }

   @Override
   public String toString() {
      return "Message{" +
              "id=" + id +
              ", chat=" + chat +
              ", senderId=" + senderName +
              ", recipientId=" + recipientId +
              ", senderName='" + senderName + '\'' +
              ", recipientName='" + recipientName + '\'' +
              ", content='" + content + '\'' +
              ", timestamp=" + timestamp +
              ", status=" + status +
              '}';
   }

   public String getSenderRole() {
      return senderRole;
   }

   public void setSenderRole(String senderRole) {
      this.senderRole = senderRole;
   }
}
