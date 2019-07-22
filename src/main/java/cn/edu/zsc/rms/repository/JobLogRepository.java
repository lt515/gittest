package cn.edu.zsc.rms.repository;

import cn.edu.zsc.rms.domain.JobLog;
import cn.edu.zsc.rms.domain.QJobLog;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

/**
 * @author pengzheng
 */
public interface JobLogRepository extends JpaRepository<JobLog, Long>,
        QuerydslPredicateExecutor<JobLog> {

    /**
     * 为定时任务日志提供查询功能
     * @param jobId
     * @param jobName
     * @param jobType
     * @param result
     * @param pageable
     * @return
     */
    default Page<JobLog> query(UUID jobId, String jobName, String jobType, JobLog.JobLogResult result, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (jobId != null) {
            query.and(QJobLog.jobLog.jobId.eq(jobId));
        }
        if (jobName != null) {
            query.and(QJobLog.jobLog.jobName.like(jobName));
        }
        if (jobType != null) {
            query.and(QJobLog.jobLog.jobType.like(jobType));
        }
        if (result != null) {
            query.and(QJobLog.jobLog.result.eq(result));
        }
        return this.findAll(query, pageable);
    }

}
