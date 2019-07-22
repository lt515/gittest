package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.Announcement;
import cn.edu.zsc.rms.domain.AnnouncementType;
import cn.edu.zsc.rms.domain.FileInfo;
import cn.edu.zsc.rms.domain.User;
import cn.edu.zsc.rms.exception.api.EntityNotExistException;
import cn.edu.zsc.rms.exception.api.validation.field.ObjectStatusValidationException;
import cn.edu.zsc.rms.repository.AnnouncementRepository;
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
public class AnnouncementService {

    private AnnouncementRepository announcementRepo;

    private UserService userService;
    private AnnouncementTypeService announcementTypeService;

    public AnnouncementService(AnnouncementRepository announcementRepo, UserService userService, AnnouncementTypeService announcementTypeService) {
        this.announcementRepo = announcementRepo;
        this.userService = userService;
        this.announcementTypeService = announcementTypeService;
    }

    public Page<Announcement> query(String title, UUID announcementTypeId,
                                    LocalDateTime publishTimeBegin, LocalDateTime publishTimeEnd,
                                    Pageable pageable
    ) {

        if (publishTimeBegin == null) {
            publishTimeBegin = LocalDateTime.now();
        }
        return announcementRepo.query(title, announcementTypeId, publishTimeBegin, publishTimeEnd, pageable);
    }

    public Announcement findById(UUID announcementId) {
        return announcementRepo.findById(announcementId).orElseThrow(() ->
                new EntityNotExistException(Announcement.class)
        );
    }

    public Announcement create(UUID userId, String title, UUID announcementTypeId, boolean topping, String content, List<FileInfo> files) {
        User user = userService.findById(userId);
        AnnouncementType type = announcementTypeService.findById(announcementTypeId);
        return announcementRepo.save(
                new Announcement(
                        title,
                        type,
                        topping,
                        Announcement.AnnouncementStatus.EDITING,
                        user,
                        LocalDateTime.now(),
                        null,
                        null,
                        null,
                        null,
                        content,
                        files,
                        UUID.randomUUID()
                )
        );
    }

    public Announcement update(UUID announcementId, UUID userId, String title, UUID announcementTypeId,
                               boolean topping, String content, List<FileInfo> files) {
        User user = userService.findById(userId);
        Announcement announcement = findById(announcementId);
        AnnouncementType type = announcementTypeService.findById(announcementTypeId);
        checkAnnouncementStatus(announcement.getStatus(), Announcement.AnnouncementStatus.EDITING);
        return announcementRepo.save(
                new Announcement(
                        title,
                        type,
                        topping,
                        Announcement.AnnouncementStatus.EDITING,
                        user,
                        LocalDateTime.now(),
                        null,
                        null,
                        null,
                        null,
                        content,
                        files,
                        announcementId
                )
        );
    }

    public Announcement delete(UUID announcementId){
        Announcement announcement = findById(announcementId);
        checkAnnouncementStatus(announcement.getStatus(), Announcement.AnnouncementStatus.EDITING);
        announcementRepo.deleteById(announcementId);
        return announcement;
    }

    public Announcement publish(UUID announcementId, UUID userId) {
        User user = userService.findById(userId);
        Announcement announcement = findById(announcementId);
        checkAnnouncementStatus(announcement.getStatus(), Announcement.AnnouncementStatus.EDITING);
        announcement.setStatus(Announcement.AnnouncementStatus.PUBLISHED);
        announcement.setPublishUser(user);
        return announcementRepo.save(announcement);
    }

    public Announcement close(UUID announcementId, UUID userId) {
        User user = userService.findById(userId);
        Announcement announcement = findById(announcementId);
        checkAnnouncementStatus(announcement.getStatus(), Announcement.AnnouncementStatus.PUBLISHED);
        announcement.setStatus(Announcement.AnnouncementStatus.CLOSED);
        announcement.setCloseUser(user);
        return announcementRepo.save(announcement);
    }

    private void checkAnnouncementStatus(Announcement.AnnouncementStatus currentStatus,
                                         Announcement.AnnouncementStatus correctStatus) {
        if (currentStatus != correctStatus) {
            throw new ObjectStatusValidationException("status", correctStatus,
                    correctStatus);
        }
    }


}
