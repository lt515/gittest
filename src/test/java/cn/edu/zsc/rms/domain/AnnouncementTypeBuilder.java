package cn.edu.zsc.rms.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * @author hsj
 */
public class AnnouncementTypeBuilder {
    private AnnouncementType announcementType;

    public AnnouncementTypeBuilder() {
        this.announcementType = new AnnouncementType(
                randomAlphabetic(6),
                null,
                LocalDateTime.now(),
                UUID.randomUUID()
        );
    }


    public AnnouncementTypeBuilder name(String name) {
        announcementType.setName(name);
        return this;
    }

    public AnnouncementTypeBuilder parentId(UUID parentId) {
        announcementType.setParentId(parentId);
        return this;
    }

    public AnnouncementTypeBuilder id(UUID id) {
        announcementType.setId(id);
        return this;
    }

    public AnnouncementType build(){
        return announcementType;
    }
}
