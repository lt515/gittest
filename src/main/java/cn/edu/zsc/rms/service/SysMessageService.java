package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.FileInfo;
import cn.edu.zsc.rms.domain.Message;
import cn.edu.zsc.rms.domain.User;
import cn.edu.zsc.rms.domain.UserMessage;
import cn.edu.zsc.rms.exception.api.EntityNotExistException;
import cn.edu.zsc.rms.exception.api.validation.field.AssociationEntityDoseNotExistException;
import cn.edu.zsc.rms.exception.api.validation.field.MessagePushValidationException;
import cn.edu.zsc.rms.exception.api.validation.field.ObjectStatusValidationException;
import cn.edu.zsc.rms.message.MessagePushManagement;
import cn.edu.zsc.rms.repository.MessageRepository;
import cn.edu.zsc.rms.repository.UserMessageRepository;
import cn.edu.zsc.rms.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author hsj
 */
@Service
public class SysMessageService {
    private MessageRepository messageRepo;
    private UserMessageRepository userMessageRepo;
    private UserRepository userRepo;

    public SysMessageService(MessageRepository messageRepo, UserMessageRepository userMessageRepo, UserRepository userRepository) {
        this.messageRepo = messageRepo;
        this.userMessageRepo = userMessageRepo;
        this.userRepo = userRepository;
    }

    public Page<UserMessage> query(String title, Message.MessageType type,
                                   String userNameOrNumber, Message.CreateBy createBy,
                                   Message.PushMethod pushMethod, Boolean haveRead,
                                   LocalDateTime sendTimeBegin, LocalDateTime sendTimeEnd,
                                   Pageable pageable) {
        return userMessageRepo.query(title,
                type,
                userNameOrNumber,
                createBy,
                pushMethod,
                haveRead,
                sendTimeBegin,
                sendTimeEnd,
                pageable);
    }

    public UserMessage findById(UserMessage.UserMessageId userMessageId) {
        return userMessageRepo.findById(userMessageId).orElseThrow(() ->
                new EntityNotExistException(UserMessage.class)
        );
    }

    public List<UserMessage> create(Set<UUID> userIds, String title, Message.MessageType type,
                                    Message.CreateBy createBy, String content, String briefContent,
                                    Set<Message.PushMethod> pushMethods, List<FileInfo> files) {
        List<User> users = userIds.stream().map(this::mapUser).collect(Collectors.toList());
        Message message = messageRepo.save(new Message(title, type, content, briefContent, createBy, pushMethods, files, LocalDateTime.now(), UUID.randomUUID()));
        List<UserMessage> userMessages = new LinkedList<>();
        users.forEach((user) -> userMessages.add(new UserMessage(user, message, false, null, new UserMessage.UserMessageId(user.getId(), message.getId()))));
        userMessageRepo.saveAll(userMessages);
        try {
            if(message.getPushMethods() != null) {
                MessagePushManagement.pushMessage(users, message);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return userMessages;
    }

    public UserMessage delete(UserMessage.UserMessageId userMessageId) {
        UserMessage userMessage = findById(userMessageId);
        if (userMessage.getHaveRead()) {
            throw new ObjectStatusValidationException("haveRead", userMessage.getHaveRead(), false);
        }
        if (userMessage.getMessage().getPushMethods() != null && !userMessage.getMessage().getPushMethods().isEmpty()) {
            throw new MessagePushValidationException("pushMethods");
        }
        userMessageRepo.deleteById(userMessageId);
        return userMessage;
    }

    private User mapUser(UUID userId) {
        return userRepo.findById(userId).orElseThrow(() ->
                new AssociationEntityDoseNotExistException("userId", userId)
        );
    }
}
