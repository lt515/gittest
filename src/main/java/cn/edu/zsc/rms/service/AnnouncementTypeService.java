package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.AnnouncementType;
import cn.edu.zsc.rms.exception.api.EntityNotExistException;
import cn.edu.zsc.rms.repository.AnnouncementTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author hsj
 */
@Service
public class AnnouncementTypeService {

    private AnnouncementTypeRepository announcementTypeRepo;

    public AnnouncementTypeService(AnnouncementTypeRepository announcementTypeRepo) {
        this.announcementTypeRepo = announcementTypeRepo;
    }

    public Page<AnnouncementType> query(String name, UUID parentId, Pageable pageable) {
        return announcementTypeRepo.query(name, parentId, pageable);
    }

    public AnnouncementType findById(UUID announcementTypeId) {
        return announcementTypeRepo.findById(announcementTypeId).orElseThrow(() -> new EntityNotExistException(AnnouncementType.class));
    }

    public List<AnnouncementType> list() {
        return announcementTypeRepo.findAll();
    }


    public AnnouncementType create(String name, UUID parentId) {
        AnnouncementType announcementType = new AnnouncementType(name,
                parentId,
                LocalDateTime.now(),
                UUID.randomUUID());
        if (parentId == null) {
            announcementType.setParentId(null);
        } else {
            findById(parentId);
        }
        return announcementTypeRepo.save(announcementType);
    }

    public AnnouncementType delete(UUID announcementTypeId) {
        AnnouncementType announcementType = findById(announcementTypeId);
        announcementTypeRepo.deleteById(announcementTypeId);
        return announcementType;
    }
}
