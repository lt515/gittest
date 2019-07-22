package cn.edu.zsc.rms.api.rest.dto;

import cn.edu.zsc.rms.domain.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengzheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {
    private String name;
    private String type;
    private String beanName;
    private String methodName;
    private String methodParams;
    private String cron;
    private Job.JobStatus status;
}
