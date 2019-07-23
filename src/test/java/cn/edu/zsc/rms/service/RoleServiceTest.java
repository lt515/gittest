package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.Role;
import cn.edu.zsc.rms.domain.RoleBuilder;
import cn.edu.zsc.rms.exception.api.EntityNotExistException;
import cn.edu.zsc.rms.exception.api.validation.field.UniquenessConstraintValidationException;
import cn.edu.zsc.rms.repository.RoleRepository;
import cn.edu.zsc.rms.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * @author hsj
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleServiceTest {

    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    private Role role;

    @Before
    public void setUp() throws Exception {
        role = new RoleBuilder().build();
        roleRepo.save(role);
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
        roleRepo.deleteAll();
    }

    @Test(expected = EntityNotExistException.class)
    public void shouldThrowEntityNotExistException() {
        Role role = new RoleBuilder().build();
        roleService.findById(role.getId());
    }

    @Test
    public void findById() {
        roleService.findById(role.getId());
    }

    @Test
    public void query() {
        Role role1 = new RoleBuilder().roleType(Role.RoleType.SYS_ADMIN).build();
        Role role2 = new RoleBuilder().build();
        Role role3 = new RoleBuilder().build();
        Role role4 = new RoleBuilder().build();
        roleRepo.save(role1);
        roleRepo.save(role2);
        roleRepo.save(role3);
        roleRepo.save(role4);
        roleService.query(null, null, Pageable.unpaged());
        assertEquals(5, roleService.query(null, null, Pageable.unpaged()).getTotalElements());
        Assert.assertEquals(role.getName(), roleService.query(role.getName(), null, Pageable.unpaged()).getContent().get(0).getName());
        assertEquals(0, roleService.query(role.getName(), role1.getType(), Pageable.unpaged()).getTotalElements());
    }

    @Test
    public void create() {
        Role role = new RoleBuilder().build();
        Role createAfterRole = roleService.create(role.getName(), role.getAuthorities(), role.getDataScope(), role.getRemark());
        assertEquals(role.getName(), roleRepo.findById(createAfterRole.getId()).get().getName());
    }

    @Test
    public void update() {
        Role role = roleRepo.save(new RoleBuilder().build());
        Role updateAfterRole = new RoleBuilder().build();
        roleService.update(role.getId(), role.getName(), updateAfterRole.getAuthorities(), updateAfterRole.getDataScope(), updateAfterRole.getRemark());
        assertEquals(updateAfterRole.getRemark(), roleRepo.findById(role.getId()).get().getRemark());
        assertEquals(role.getName(), roleRepo.findById(role.getId()).get().getName());
        assertEquals(updateAfterRole.getAuthorities(), roleRepo.findById(role.getId()).get().getAuthorities());
    }

    @Test(expected = UniquenessConstraintValidationException.class)
    public void shouldThrowUniquenessConstraintValidationExceptionWhenUpdate() {
        Role role = roleRepo.save(new RoleBuilder().build());
        Role role2 = roleRepo.save(new RoleBuilder().build());
        roleService.update(role.getId(), role2.getName(), role2.getAuthorities(), role.getDataScope(), role2.getRemark());
    }

    @Test(expected = UniquenessConstraintValidationException.class)
    public void shouldThrowUniquenessConstraintValidationExceptionWhenCreate() {
        Role role = roleRepo.save(new RoleBuilder().build());
        roleService.create(role.getName(), role.getAuthorities(), role.getDataScope(), role.getRemark());
    }
}