package cn.edu.zsc.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author hsj
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementType {
    @Column(nullable = false)
    private String name;

    private UUID parentId;

    private LocalDateTime updateTime;

    @Id
    private UUID id;
}
