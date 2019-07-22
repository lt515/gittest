package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.JobLog;
import cn.edu.zsc.rms.exception.api.EntityNotExistException;
import cn.edu.zsc.rms.repository.JobLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author pengzheng
 */
@Service
public class JobLogService {
    private JobLogRepository jobLogRepo;

    public JobLogService(JobLogRepository jobLogRepo) {
        this.jobLogRepo = jobLogRepo;
    }

    public Page<JobLog> query(UUID jobId, String jobName, String jobType, JobLog.JobLogResult result, Pageable pageable) {
        return jobLogRepo.query(jobId, jobName, jobType, result, pageable);
    }

    public JobLog findById(Long jobLogId) {
        return jobLogRepo.findById(jobLogId).orElseThrow(() -> new EntityNotExistException(JobLog.class));
    }

    public JobLog create(JobLog jobLog) {
        return jobLogRepo.save(jobLog);
    }

}