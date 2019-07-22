package cn.edu.zsc.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author hsj
 */
public class UserMessageBuilder {
   private UserMessage userMessage;

    public UserMessageBuilder(User user, Message message) {
        userMessage = new UserMessage(
                user,
                message,
                false,
                LocalDateTime.now(),
                new UserMessage.UserMessageId(user.getId(), message.getId())
        );
    }

    public UserMessageBuilder user(User user) {
        userMessage.setUser(user);
        return this;
    }

    public UserMessageBuilder message(Message message) {
        userMessage.setMessage(message);
        return this;
    }

    public UserMessageBuilder haveRead(Boolean haveRead) {
        userMessage.setHaveRead(haveRead);
        return this;
    }

    public UserMessageBuilder readTime(LocalDateTime readTime) {
        userMessage.setReadTime(readTime);
        return this;
    }

    public UserMessageBuilder messageId(UserMessage.UserMessageId messageId) {
        userMessage.setMessageId(messageId);
        return this;
    }

    public UserMessage build(){
        return userMessage;
    }
}
