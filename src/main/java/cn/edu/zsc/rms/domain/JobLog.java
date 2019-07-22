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
public class JobLog implements Serializable {

    private UUID jobId;
    private String jobName;
    private String jobType;
    private String jobBeanName;
    private String jobMethodName;
    private String jobMethodParams;
    private String message;
    @Enumerated(EnumType.STRING)
    private JobLogResult result;
    private LocalDateTime startTime;
    private Long duration;
    private String executor = "SYSTEM";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum JobLogResult {
        /**
         * 成功
         */
        SUCCESS,
        /**
         * 失败
         */
        FAILURE
    }

}
