package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.*;
import cn.edu.zsc.rms.exception.api.validation.field.MessagePushValidationException;
import cn.edu.zsc.rms.exception.api.validation.field.ObjectStatusValidationException;
import cn.edu.zsc.rms.repository.*;
import org.assertj.core.util.Sets;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author hsj
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysMessageServiceTest {

    @Autowired
    UserMessageRepository userMessageRepo;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private SysMessageService sysMessageService;

    private Message message;
    private User user;
    private Role role;
    private Set<Role> roles;
    private Set<UUID> rolesUuid;
    private Department department;
    private UserMessage userMessage;

    @Before
    public void setUp() throws Exception {
        department = new DepartmentBuilder().build();
        role = new RoleBuilder().build();
        roles = new LinkedHashSet<>();
        roles.add(role);
        rolesUuid = roles.stream().map(Role::getId).collect(Collectors.toSet());
        user = new UserBuilder(roles, department).email("hsj215@aliyun.com").build();
        departmentRepository.save(department);
        roleRepository.save(role);
        userRepository.save(user);
        message = new MessageBuilder().build();
        messageRepo.save(message);
        userMessage = new UserMessageBuilder(user, message).build();
        userMessageRepo.save(userMessage);

    }

    @After
    public void tearDown() throws Exception {
        userMessageRepo.deleteAll();
        messageRepo.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    public void query() {
        assertEquals(1, sysMessageService.query(null, null, null, null,
                null, null, null, null, Pageable.unpaged()).getTotalElements());
        Message message1 = new MessageBuilder().sendTime(message.getSendTime().plusDays(2)).build();
        Message message2 = new MessageBuilder().sendTime(message.getSendTime().plusDays(4)).build();
        Message message3 = new MessageBuilder().sendTime(message.getSendTime().plusDays(6)).build();
        UserMessage userMessage1 = new UserMessageBuilder(user, message1).build();
        UserMessage userMessage2 = new UserMessageBuilder(user, message2).build();
        UserMessage userMessage3 = new UserMessageBuilder(user, message3).build();
        messageRepo.save(message3);
        messageRepo.save(message1);
        messageRepo.save(message2);
        userMessageRepo.save(userMessage1);
        userMessageRepo.save(userMessage2);
        userMessageRepo.save(userMessage3);
        Page<UserMessage> userMessagePage = sysMessageService.query(null, null, null, null,
                null, null, null, null,
                PageRequest.of(0, 10, Sort.Direction.DESC, "message.sendTime"));
        assertEquals(message3.getTitle(), userMessagePage.getContent().get(0).getMessage().getTitle());
        assertEquals(message2.getTitle(), userMessagePage.getContent().get(1).getMessage().getTitle());
        assertEquals(message1.getTitle(), userMessagePage.getContent().get(2).getMessage().getTitle());
        assertEquals(message.getTitle(), userMessagePage.getContent().get(3).getMessage().getTitle());
    }

    @Test
    public void findById() {
        assertNotNull(sysMessageService.findById(new UserMessage.UserMessageId(user.getId(), message.getId())));

    }

    @Test
    public void create() {
        Set<UUID> userIds = new LinkedHashSet<>();
        userIds.add(user.getId());
        sysMessageService.create(
                userIds,
                "5578",
                message.getType(),
                message.getCreateBy(),
                message.getContent(),
                message.getBriefContent(),
                message.getPushMethods(),
                message.getFiles()
        );
        assertNotNull(sysMessageService.query("5578", null, null, null,
                null, null, null, null, Pageable.unpaged()).get().findFirst().get());
    }

    @Test
    @Ignore
    public void createPushMessage() {
        Set<Message.PushMethod> pushMethods = new LinkedHashSet<>();
        pushMethods.add(Message.PushMethod.EMAIl);
        Message message1 = new MessageBuilder().pushMethods(pushMethods).build();
        Set<UUID> userIds = new LinkedHashSet<>();
        userIds.add(user.getId());
        sysMessageService.create(
                userIds,
                message1.getTitle(),
                message1.getType(),
                message1.getCreateBy(),
                message1.getContent(),
                message1.getBriefContent(),
                message1.getPushMethods(),
                message1.getFiles()
        );
    }

    @Test
    public void delete(){
        Message message1 = new MessageBuilder().pushMethods(Sets.newHashSet()).build();
        UserMessage userMessage1 = new UserMessageBuilder(user, message1).build();
        messageRepo.save(message1);
        userMessageRepo.save(userMessage1);
        sysMessageService.delete(userMessage1.getMessageId());
        assertFalse(userMessageRepo.findById(userMessage1.getMessageId()).isPresent());
    }
    @Test(expected = ObjectStatusValidationException.class)
    public void deleteThrowObjectStatusValidationException(){
        Message message1 = new MessageBuilder().pushMethods(Sets.newHashSet()).build();
        UserMessage userMessage1 = new UserMessageBuilder(user, message1).haveRead(true).build();
        messageRepo.save(message1);
        userMessageRepo.save(userMessage1);
        sysMessageService.delete(userMessage1.getMessageId());
    }

    @Test(expected = MessagePushValidationException.class)
    public void deleteThrowMessagePushValidationException(){
        Set<Message.PushMethod> pushMethods = new HashSet<>();
        pushMethods.add(Message.PushMethod.EMAIl);
        Message message1 = new MessageBuilder().pushMethods(pushMethods).build();
        UserMessage userMessage1 = new UserMessageBuilder(user, message1).build();
        messageRepo.save(message1);
        userMessageRepo.save(userMessage1);
        sysMessageService.delete(userMessage1.getMessageId());
    }
}