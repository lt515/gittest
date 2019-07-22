package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.*;
import cn.edu.zsc.rms.repository.DepartmentRepository;
import cn.edu.zsc.rms.repository.OnlineUserRepository;
import cn.edu.zsc.rms.repository.RoleRepository;
import cn.edu.zsc.rms.repository.UserRepository;
import net.bytebuddy.asm.Advice;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author hsj
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OnlineUserServiceTest {

    @Autowired
    private OnlineUserRepository onlineUserRepo;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private OnlineUserService onlineUserService;

    private User user;
    private Role role;
    private Set<Role> roles;
    private Department department;
    private OnlineUser onlineUser;

    @Before
    public void setUp() throws Exception {
        department = new DepartmentBuilder().build();
        role = new RoleBuilder().build();
        roles = new LinkedHashSet<>();
        roles.add(role);
        user = new UserBuilder(roles, department).build();
        departmentRepository.save(department);
        roleRepository.save(role);
        userRepository.save(user);
        onlineUser = new OnlineUserBuilder(user.getName(), user.getNumber(), department.getName(), department.getNumber()).build();
        onlineUserRepo.save(onlineUser);
    }

    @After
    public void tearDown() throws Exception {
        onlineUserRepo.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    public void query() {
        assertEquals(0, onlineUserService.query(null, null, null,
                null, null, null, Pageable.unpaged()).getTotalElements());
    }
}