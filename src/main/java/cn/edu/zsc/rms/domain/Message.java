package cn.edu.zsc.rms.domain;

import cn.edu.zsc.rms.message.MessagePush;
import cn.edu.zsc.rms.message.email.EmailMessagePush;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author hsj
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Column(nullable = false)
    private String title;
    private MessageType type;
    @Lob
    private String content;
    private String briefContent;
    private CreateBy createBy;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<PushMethod> pushMethods;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<FileInfo> files;
    private LocalDateTime sendTime;
    @Id
    private UUID id;



    public enum MessageType{
        /**
         * 用户修改密码
         */
        USER_UPDATE_PASSWORD

    }

    public enum CreateBy{
        /**
         * 系统生成
         */
        SYSTEM,
        /**
         * 手动生成
         */
        USER
    }

    public enum PushMethod{
        /**
         * 邮件
         */
        EMAIl(EmailMessagePush.class);

        private Class<? extends MessagePush> messagePushClass;

        PushMethod(Class<? extends MessagePush> messagePushClass) {
            this.messagePushClass = messagePushClass;
        }

        public Class<? extends MessagePush> getMessagePushClass(){
            return messagePushClass;
        }
    }
}
