package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.*;
import cn.edu.zsc.rms.exception.api.EntityNotExistException;
import cn.edu.zsc.rms.exception.api.validation.field.ObjectStatusValidationException;
import cn.edu.zsc.rms.repository.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author hsj
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AnnouncementServiceTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private AnnouncementRepository announcementRepo;
    @Autowired
    private AnnouncementTypeRepository announcementTypeRepo;

    @Autowired
    private AnnouncementService announcementService;

    private User publishUser;
    private User closeUser;
    private User editUser;
    private Role role;
    private Set<Role> roles;
    private Department department;
    private AnnouncementType announcementType;

    @Before
    public void setUp() throws Exception {
        announcementType = new AnnouncementTypeBuilder().build();
        department = new DepartmentBuilder().build();
        role = new RoleBuilder().build();
        roles = new LinkedHashSet<>();
        roles.add(role);
        editUser = new UserBuilder(roles, department).build();
        publishUser = new UserBuilder(roles, department).build();
        closeUser = new UserBuilder(roles, department).build();
        departmentRepository.save(department);
        roleRepository.save(role);
        userRepository.save(editUser);
        userRepository.save(publishUser);
        userRepository.save(closeUser);
        announcementTypeRepo.save(announcementType);
    }

    @After
    public void tearDown() throws Exception {
        announcementRepo.deleteAll();
        announcementTypeRepo.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    public void query() {
        Announcement e1 = new AnnouncementBuilder(editUser, announcementType).build();
        Announcement e2 = new AnnouncementBuilder(editUser, announcementType).topping(false).build();
        Announcement p1 = new AnnouncementBuilder(editUser, announcementType)
                .publishUser(publishUser)
                .publishTime(LocalDateTime.now().plusDays(2)).build();
        Announcement p2 = new AnnouncementBuilder(editUser, announcementType)
                .publishUser(publishUser).topping(false)
                .publishTime(LocalDateTime.now().plusDays(2)).build();

        Announcement c1 = new AnnouncementBuilder(editUser, announcementType)
                .publishUser(publishUser)
                .publishTime(LocalDateTime.now().plusDays(3))
                .closeUser(closeUser)
                .closeTime(LocalDateTime.now().plusDays(5)).build();
        Announcement c2 = new AnnouncementBuilder(editUser, announcementType)
                .publishUser(publishUser).topping(false)
                .publishTime(LocalDateTime.now().plusDays(3))
                .closeUser(closeUser)
                .closeTime(LocalDateTime.now().plusDays(5)).build();
        announcementRepo.save(e1);
        announcementRepo.save(e2);
        announcementRepo.save(p1);
        announcementRepo.save(p2);
        announcementRepo.save(c1);
        announcementRepo.save(c2);
        assertEquals(announcementRepo.findAll().size(),
                announcementService.query(null, null,
                LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(10),
                        Pageable.unpaged()).getTotalElements()
        );
        Pageable pageable = PageRequest.of
                (0, 10,
                        Sort.by(Sort.Direction.DESC, "topping", "publishTime")
                );
        ArrayList<Announcement> announcements = announcementService.query( null, null,
                LocalDateTime.now(), LocalDateTime.now().plusDays(5),
                pageable).get().collect(Collectors.toCollection(ArrayList::new));
        assertEquals(c1.getId(), announcements.get(0).getId());
        assertEquals(p1.getId(), announcements.get(1).getId());
        assertEquals(e1.getId(), announcements.get(2).getId());
        assertEquals(c2.getId(), announcements.get(3).getId());
        assertEquals(p2.getId(), announcements.get(4).getId());
        assertEquals(e2.getId(), announcements.get(5).getId());

    }

    @Test
    public void findById() {
        Announcement e1 = new AnnouncementBuilder(editUser, announcementType).build();
        announcementRepo.save(e1);
        assertEquals(e1.getId(), announcementService.findById(e1.getId()).getId());
    }
    @Test(expected = EntityNotExistException.class)
    public void findByIdThrowEntityNotExistException() {
        Announcement e1 = new AnnouncementBuilder(editUser, announcementType).build();
        assertEquals(e1.getId(), announcementService.findById(e1.getId()).getId());
    }


    @Test
    public void create() {
        Announcement e1 = new AnnouncementBuilder(editUser, announcementType).build();
        Announcement create = announcementService.create(
                e1.getEditUser().getId(),
                e1.getTitle(),
                e1.getAnnouncementType().getId(),
                e1.getTopping(),
                e1.getContent(),
                e1.getFiles()
        );
        assertEquals(e1.getContent(), create.getContent());
        assertEquals(Announcement.AnnouncementStatus.EDITING, create.getStatus());
    }

    @Test
    public void update() {
        Announcement e1 = new AnnouncementBuilder(editUser, announcementType).build();
        announcementRepo.save(e1);
        announcementService.update(
                e1.getId(),
                e1.getEditUser().getId(),
                "555",
                e1.getAnnouncementType().getId(),
                false,
                e1.getContent(),
                e1.getFiles()
        );
        assertNotEquals(e1.getTitle(), announcementRepo.findById(e1.getId()).get().getTitle());
        assertTrue(!announcementRepo.findById(e1.getId()).get().getTopping());
        assertNotEquals(e1.getEditTime(), announcementRepo.findById(e1.getId()).get().getEditTime());
    }

    @Test(expected = ObjectStatusValidationException.class)
    public void updateByPublishStatus() {
        Announcement e1 = new AnnouncementBuilder(editUser, announcementType).publishUser(publishUser)
                .status(Announcement.AnnouncementStatus.PUBLISHED)
                .publishTime(LocalDateTime.now().plusDays(2)).build();
        announcementRepo.save(e1);
        announcementService.update(
                e1.getId(),
                e1.getEditUser().getId(),
                "555",
                e1.getAnnouncementType().getId(),
                false,
                e1.getContent(),
                e1.getFiles()
        );
    }

    @Test
    public void delete() {
        Announcement e1 = new AnnouncementBuilder(editUser, announcementType).build();
        announcementRepo.save(e1);
        announcementService.delete(
                e1.getId()
        );
        assertEquals(false, announcementRepo.findById(e1.getId()).isPresent());
    }

    @Test(expected = ObjectStatusValidationException.class)
    public void deleteByPublishStatus() {
        Announcement e1 = new AnnouncementBuilder(editUser, announcementType).publishUser(publishUser)
                .status(Announcement.AnnouncementStatus.PUBLISHED)
                .publishTime(LocalDateTime.now().plusDays(2)).build();
        announcementRepo.save(e1);
        announcementService.delete(
                e1.getId()
        );
    }

    @Test
    public void publish() {
        Announcement e1 = new AnnouncementBuilder(editUser, announcementType).build();
        announcementRepo.save(e1);
        announcementService.publish(e1.getId(), publishUser.getId());
        Announcement result = announcementRepo.findById(e1.getId()).get();
        assertEquals(Announcement.AnnouncementStatus.PUBLISHED, result.getStatus());
        assertEquals(publishUser.getNumber(), result.getPublishUser().getNumber());
    }

    @Test(expected = ObjectStatusValidationException.class)
    public void publishByPublished() {
        Announcement e1 = new AnnouncementBuilder(editUser, announcementType).publishUser(publishUser)
                .status(Announcement.AnnouncementStatus.PUBLISHED)
                .publishTime(LocalDateTime.now().plusDays(2)).build();
        announcementRepo.save(e1);
        announcementService.publish(e1.getId(), publishUser.getId());
    }

    @Test
    public void close() {
        Announcement e1 = new AnnouncementBuilder(editUser, announcementType).publishUser(publishUser)
                .status(Announcement.AnnouncementStatus.PUBLISHED)
                .publishTime(LocalDateTime.now().plusDays(2)).build();
        announcementRepo.save(e1);
        announcementService.close(e1.getId(), closeUser.getId());
        Announcement result = announcementRepo.findById(e1.getId()).get();
        assertEquals(Announcement.AnnouncementStatus.CLOSED, result.getStatus());
        assertEquals(closeUser.getNumber(), result.getCloseUser().getNumber());
    }

    @Test
    public void closeByClosed() {
        Announcement e1 = new AnnouncementBuilder(editUser, announcementType).publishUser(publishUser)
                .status(Announcement.AnnouncementStatus.PUBLISHED)
                .publishTime(LocalDateTime.now().plusDays(2)).build();
        announcementRepo.save(e1);
        announcementService.close(e1.getId(), closeUser.getId());
        Announcement result = announcementRepo.findById(e1.getId()).get();
        assertEquals(Announcement.AnnouncementStatus.CLOSED, result.getStatus());
        assertEquals(closeUser.getNumber(), result.getCloseUser().getNumber());
    }
}