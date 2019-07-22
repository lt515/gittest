package cn.edu.zsc.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author hsj
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announcement {

    @Column(nullable = false)
    String title;

    @ManyToOne(optional = false)
    AnnouncementType announcementType;

    @Column(nullable = false)
    Boolean topping;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AnnouncementStatus status;

    @OneToOne
    User editUser;
    LocalDateTime editTime;

    @OneToOne
    User publishUser;
    LocalDateTime publishTime;

    @OneToOne
    User closeUser;
    LocalDateTime closeTime;

    @Lob
    String content;

    @ElementCollection(fetch = FetchType.EAGER)
    List<FileInfo> files;

    @Id
    UUID id;

    public enum AnnouncementStatus {
        /**
         * 编辑中
         */
        EDITING,
        /**
         * 已发布
         */
        PUBLISHED,
        /**
         * 已关闭
         */
        CLOSED
    }
}


