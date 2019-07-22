package cn.edu.zsc.rms.domain;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * @author hsj
 */
public class MessageBuilder {
    private Message message;

    public MessageBuilder() {
        List<FileInfo> files = new LinkedList<>();
        files.add(new FileInfoBuilder(1).build());
        files.add(new FileInfoBuilder(2).build());
        files.add(new FileInfoBuilder(3).build());
        message = new Message(
                randomAlphabetic(5),
                Message.MessageType.USER_UPDATE_PASSWORD,
                randomAlphabetic(30),
                randomAlphabetic(15),
                Message.CreateBy.USER,
                null,
                files,
                LocalDateTime.now(),
                UUID.randomUUID()
        );
    }

    public MessageBuilder title(String title) {
        message.setTitle(title);
        return this;
    }

    public MessageBuilder type(Message.MessageType type) {
        message.setType(type);
        return this;
    }

    public MessageBuilder content(String content) {
        message.setContent(content);
        return this;
    }

    public MessageBuilder briefContent(String briefContent) {
        message.setBriefContent(briefContent);
        return this;
    }

    public MessageBuilder createBy(Message.CreateBy createBy) {
        message.setCreateBy(createBy);
        return this;
    }

    public MessageBuilder pushMethods(Set<Message.PushMethod> pushMethods) {
        message.setPushMethods(pushMethods);
        return this;
    }

    public MessageBuilder files(List<FileInfo> files) {
        message.setFiles(files);
        return this;
    }

    public MessageBuilder sendTime(LocalDateTime sendTime) {
        message.setSendTime(sendTime);
        return this;
    }

    public MessageBuilder id(UUID id) {
        message.setId(id);
        return this;
    }

    public Message build() {
        return message;
    }
}
