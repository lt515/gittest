package cn.edu.zsc.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Column
    private String name;

    @Column
    private String type;

    @Enumerated(EnumType.STRING)
    private DocumentsStatus status;

    @OneToOne
    private User editUser;
    private LocalDateTime editTime;

    @OneToOne
    private User publishUser;
    private LocalDateTime publishTime;

    private String remark;

    @ElementCollection(fetch = FetchType.EAGER)
    List<FileInfo> files;

    private int downloadTimes;

    @Id
    private UUID id;

    public enum DocumentsStatus{
        /**
         * 已发布
         */
        PUBLISHED,

        /**
         * 未发布
         */
        UNPUBLISHED
    }

}
