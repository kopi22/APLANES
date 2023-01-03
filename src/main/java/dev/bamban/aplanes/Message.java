package dev.bamban.aplanes;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_generator")
    @SequenceGenerator(name="message_generator", sequenceName = "message_id_generator", allocationSize = 1)
    private Integer id;
    private String sender;
    private String receiver;
    private MessageType type;
    private String content;

    public Message(String sender, String receiver, MessageType type, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.content = content;
    }

    protected Message() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String from) {
        this.sender = from;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String to) {
        this.receiver = to;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message that = (Message) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + id +
                ", from='" + sender + '\'' +
                ", to='" + receiver + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
