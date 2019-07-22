package cn.edu.zsc.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author pengzheng
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job implements Serializable {
    @Column(unique = true)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String beanName;

    @Column(nullable = false)
    private String methodName;

    private String methodParams;

    @Column(nullable = false)
    private String cron;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private LocalDateTime updateTime;

    @Id
    private UUID id;

    public enum JobStatus {
        /**
         * 启用
         */
        ENABLE,
        /**
         * 停用
         */
        DISABLE
    }

}
