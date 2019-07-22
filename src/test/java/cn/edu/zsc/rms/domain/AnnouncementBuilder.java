package cn.edu.zsc.rms.domain;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * @author hsj
 */
public class AnnouncementBuilder {

    private Announcement announcement;

    public AnnouncementBuilder(User editUser, AnnouncementType announcementType) {
        List<FileInfo> files = new LinkedList<>();
        files.add(new FileInfoBuilder(1).build());
        files.add(new FileInfoBuilder(2).build());
        files.add(new FileInfoBuilder(3).build());
        announcement = new Announcement(
                randomAlphabetic(8),
                announcementType,
                true,
                Announcement.AnnouncementStatus.EDITING,
                editUser,
                LocalDateTime.now(),
                null,
                null,
                null,
                null,
                randomAlphabetic(55),
                files,
                UUID.randomUUID()
        );
    }

    public AnnouncementBuilder title(String title) {
        announcement.setTitle(title);
        return this;
    }


    public AnnouncementBuilder announcementType(AnnouncementType announcementType) {
        announcement.setAnnouncementType(announcementType);
        return this;
    }

    public AnnouncementBuilder topping(Boolean topping) {
        announcement.setTopping(topping);
        return this;
    }

    public AnnouncementBuilder status(Announcement.AnnouncementStatus status) {
        announcement.setStatus(status);
        return this;
    }

    public AnnouncementBuilder editUser(User editUser) {
        announcement.setEditUser(editUser);
        return this;
    }

    public AnnouncementBuilder editTime(LocalDateTime editTime) {
        announcement.setEditTime(editTime);
        return this;
    }

    public AnnouncementBuilder publishUser(User publishUser) {
        announcement.setPublishUser(publishUser);
        return this;
    }

    public AnnouncementBuilder publishTime(LocalDateTime publishTime) {
        announcement.setPublishTime(publishTime);
        return this;
    }

    public AnnouncementBuilder closeUser(User closeUser) {
        announcement.setCloseUser(closeUser);
        return this;
    }

    public AnnouncementBuilder closeTime(LocalDateTime closeTime) {
        announcement.setCloseTime(closeTime);
        return this;
    }

    public AnnouncementBuilder content(String content) {
        announcement.setContent(content);
        return this;
    }

    public AnnouncementBuilder fileUrls(List<FileInfo> files) {
        announcement.setFiles(files);
        return this;
    }
    public AnnouncementBuilder id(UUID id) {
        announcement.setId(id);
        return this;
    }


    public Announcement build(){
        return announcement;
    }
}
