package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.AnnouncementType;
import cn.edu.zsc.rms.domain.AnnouncementTypeBuilder;
import cn.edu.zsc.rms.exception.api.EntityNotExistException;
import cn.edu.zsc.rms.repository.AnnouncementTypeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author hsj
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AnnouncementTypeServiceTest {

    @Autowired
    private AnnouncementTypeRepository announcementTypeRepo;

    @Autowired
    private AnnouncementTypeService announcementTypeService;

    private AnnouncementType announcementType;

    @Before
    public void setUp() throws Exception {
        announcementType = new AnnouncementTypeBuilder().build();
        announcementTypeRepo.save(announcementType);
    }

    @After
    public void tearDown() throws Exception {
        announcementTypeRepo.deleteAll();
    }

    @Test
    public void create() {
        AnnouncementType test1 = announcementTypeService.create(
                announcementType.getName(),
                null
        );
        assertEquals(2, announcementTypeRepo.findAll().size());
        assertNull(announcementTypeService.findById(test1.getId()).getParentId());
        AnnouncementType test2 = announcementTypeService.create(
                announcementType.getName(),
                announcementType.getId()
        );
        assertEquals(announcementType.getId(), announcementTypeService.findById(test2.getId()).getParentId());

    }

    @Test(expected = EntityNotExistException.class)
    public void createThrowEntityNotExistException() {
        AnnouncementType test1 = announcementTypeService.create(
                announcementType.getName(),
                UUID.randomUUID()
        );
    }
}