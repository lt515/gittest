package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.*;
import cn.edu.zsc.rms.exception.api.validation.field.AssociationEntityDoseNotExistException;
import cn.edu.zsc.rms.exception.api.validation.field.UniquenessConstraintValidationException;
import cn.edu.zsc.rms.repository.DepartmentRepository;
import cn.edu.zsc.rms.repository.RoleRepository;
import cn.edu.zsc.rms.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author hsj
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserService userService;

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
        user = new UserBuilder(roles, department).build();
        departmentRepository.save(department);
        roleRepository.save(role);
        userRepository.save(user);
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    public void findById() {
        assertNotNull(userService.findById(user.getId()));
    }

    @Test
    public void query() {
        Department department = new DepartmentBuilder().build();
        Role role = new RoleBuilder().build();
        Role role1 = new RoleBuilder().build();
        Role role2 = new RoleBuilder().build();
        Set<Role> roles = new LinkedHashSet<>();
        Set<Role> roles2 = new LinkedHashSet<>();
        roles.add(role);
        roles.add(role1);
        roles2.add(role1);
        roles2.add(role2);
        User user = new UserBuilder(roles, department).build();
        User user1 = new UserBuilder(roles, department).build();
        departmentRepository.save(department);
        roleRepository.save(role);
        roleRepository.save(role1);
        roleRepository.save(role2);
        userRepository.save(user);
        userRepository.save(user1);
        Page<User> users;

        users = userService.query(null, null, null, null, roles2.stream().map(Role::getId).collect(Collectors.toSet()), null, null, null, Pageable.unpaged());
        assertEquals(2L, users.getTotalElements());

        users = userService.query(null, null, null, null, new HashSet<>(), null, null, null, Pageable.unpaged());
        assertEquals(0L, users.getTotalElements());

        users = userService.query(user.getNumber(), null, null, null, null, null, null, null, Pageable.unpaged());
        assertEquals(user.getName(), users.getContent().get(0).getName());

        users = userService.query(null, null, null, null, null, department.getId(), null, null, Pageable.unpaged());
        assertEquals(2L, users.getContent().size());
    }

    @Test(expected = UniquenessConstraintValidationException.class)
    public void checkUniquenessConstraintByNumber() {
        User user = new UserBuilder(roles, department).build();
        userRepository.save(user);
        userService.create(user.getNumber(), user.getPassword(), user.getName(),
                "112", "77", user.getAvatarUrl(), rolesUuid,
                user.getDepartment().getId(), user.getStatus());
    }

    @Test(expected = UniquenessConstraintValidationException.class)
    public void checkUniquenessConstraintByNumberWhenUpdate() {
        User user = new UserBuilder(roles, department).build();
        userRepository.save(user);
        userService.update(this.user.getId(), user.getName(),
                user.getPhone(), user.getEmail(), user.getAvatarUrl(), rolesUuid,
                user.getDepartment().getId(), user.getStatus());
    }

    @Test(expected = UniquenessConstraintValidationException.class)
    public void checkUniquenessConstraintByPhone() {
        User user = new UserBuilder(roles, department).build();
        userRepository.save(user);
        userService.create("999", user.getPassword(), user.getName(),
                user.getPhone(), "333", user.getAvatarUrl(), rolesUuid,
                user.getDepartment().getId(), user.getStatus());
    }

    @Test(expected = UniquenessConstraintValidationException.class)
    public void checkUniquenessConstraintByPhoneWhenUpdate() {
        User user = new UserBuilder(roles, department).build();
        userRepository.save(user);
        userService.update(user.getId(), user.getName(),
                this.user.getPhone(), user.getEmail(), user.getAvatarUrl(), rolesUuid,
                user.getDepartment().getId(), user.getStatus());
    }

    @Test(expected = UniquenessConstraintValidationException.class)
    public void checkUniquenessConstraintByEmail() {
        User user = new UserBuilder(roles, department).build();
        userRepository.save(user);
        userService.create("77", user.getPassword(), user.getName(),
                "88", user.getEmail(), user.getAvatarUrl(), rolesUuid,
                user.getDepartment().getId(), user.getStatus());
    }

    @Test(expected = UniquenessConstraintValidationException.class)
    public void checkUniquenessConstraintByEmailWhenUpdate() {
        User user = new UserBuilder(roles, department).build();
        userRepository.save(user);
        userService.update(user.getId(), user.getName(),
                user.getPhone(), this.user.getEmail(), user.getAvatarUrl(), rolesUuid,
                user.getDepartment().getId(), user.getStatus());
    }


    @Test(expected = AssociationEntityDoseNotExistException.class)
    public void shouldThrowAssociationEntityDoseNotExistExceptionWhenRoleIdNotExist() {
        Department department = new DepartmentBuilder().build();
        Role role = new RoleBuilder().build();
        Set<Role> roles = new LinkedHashSet<>();
        roles.add(role);
        Set<UUID> rolesUuid = roles.stream().map(Role::getId).collect(Collectors.toSet());
        User user = new UserBuilder(roles, department).build();
        departmentRepository.save(department);
        userService.create("77", user.getPassword(), "22",
                "88", user.getEmail(), user.getAvatarUrl(), rolesUuid,
                user.getDepartment().getId(), user.getStatus());
    }

    @Test(expected = AssociationEntityDoseNotExistException.class)
    public void shouldThrowAssociationEntityDoseNotExistExceptionWhenDepartmentIdNotExist() {
        Department department = new DepartmentBuilder().build();
        Role role = new RoleBuilder().build();
        Set<Role> roles = new LinkedHashSet<>();
        roles.add(role);
        Set<UUID> rolesUuid = roles.stream().map(Role::getId).collect(Collectors.toSet());
        user = new UserBuilder(roles, department).build();
        roleRepository.save(role);
        userService.create("77", user.getPassword(), "22",
                "88", user.getEmail(), user.getAvatarUrl(), rolesUuid,
                department.getId(), user.getStatus());
    }

    @Test
    public void create() {
        Department department = new DepartmentBuilder().build();
        Role role = new RoleBuilder().build();
        Set<Role> roles = new LinkedHashSet<>();
        roles.add(role);
        Set<UUID> rolesUuid = roles.stream().map(Role::getId).collect(Collectors.toSet());
        user = new UserBuilder(roles, department).build();
        roleRepository.save(role);
        departmentRepository.save(department);
        userService.create("77", user.getPassword(), "22",
                "88", user.getEmail(), user.getAvatarUrl(), rolesUuid,
                department.getId(), user.getStatus());

        assertEquals(true, userRepository.existsByNumber("77"));
    }

    @Test
    public void update() {
        User updateAfterUser = new UserBuilder(roles, department).status(User.UserStatus.DISABLED).build();
        userService.update(user.getId(), user.getName(),
                user.getPhone(), user.getEmail(), user.getAvatarUrl(), rolesUuid,
                user.getDepartment().getId(), updateAfterUser.getStatus());

        assertEquals(updateAfterUser.getStatus(), userRepository.findById(user.getId()).get().getStatus());
        assertEquals(updateAfterUser.getRoles(), userRepository.findById(user.getId()).get().getRoles());
    }

    @Test
    public void findUserInfoById() {
        User result = userService.findByNumber(user.getNumber());
        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void excel() throws Exception {
        userRepository.deleteAll();
        User user = new UserBuilder(roles, department).build();
        userRepository.save(user);
        Path path =  Files.createTempFile(null, null);
        OutputStream out = Files.newOutputStream(path);
        userService.excelExport(out, null, null, null, null, null, null, null, null);
        userRepository.deleteAll();
        InputStream in = Files.newInputStream(path);
        userService.excelImport(in);
        assertEquals(1, userRepository.findAll().size());
        Files.deleteIfExists(path);
    }

}