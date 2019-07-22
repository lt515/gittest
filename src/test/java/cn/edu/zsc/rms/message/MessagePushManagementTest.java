package cn.edu.zsc.rms.message;

import cn.edu.zsc.rms.domain.*;
import cn.edu.zsc.rms.repository.DepartmentRepository;
import cn.edu.zsc.rms.repository.MessageRepository;
import cn.edu.zsc.rms.repository.RoleRepository;
import cn.edu.zsc.rms.repository.UserRepository;
import cn.edu.zsc.rms.service.SysMessageService;
import cn.edu.zsc.rms.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author hsj
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessagePushManagementTest {

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
    private SysMessageService messageService;
    private Message message;

    private User user;
    private Role role;
    private Set<Role> roles;
    private Set<UUID> rolesUuid;
    private Department department;

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
        Set<Message.PushMethod> pushMethods = new LinkedHashSet<>();
        pushMethods.add(Message.PushMethod.EMAIl);
        message = new MessageBuilder().pushMethods(pushMethods).build();
        messageRepo.save(message);
    }

    @After
    public void tearDown() throws Exception {
        messageRepo.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    @Ignore
    public void sendEmail(){
        MessagePushManagement.pushMessage(user, message);
    }
}