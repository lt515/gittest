package cn.edu.zsc.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author hsj
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage {
    @MapsId("userId")
    @ManyToOne
    private User user;
    @MapsId("messageId")
    @ManyToOne
    private Message message;
    private Boolean haveRead;
    private LocalDateTime readTime;

    @EmbeddedId
    private UserMessageId messageId;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserMessageId implements Serializable {
        private UUID userId;
        private UUID messageId;
    }
}
