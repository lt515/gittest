package cn.edu.zsc.rms.repository;

import cn.edu.zsc.rms.domain.Job;
import cn.edu.zsc.rms.domain.QJob;
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
public interface JobRepository extends JpaRepository<Job, UUID>,
        QuerydslPredicateExecutor<Job> {

    /**
     * 为定时任务管理提供分页查询功能
     *
     * @param name
     * @param type
     * @param status
     * @param pageable
     * @return
     */
    default Page<Job> query(String name, String type, Job.JobStatus status, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (name != null) {
            query.and(QJob.job.name.like(name));
        }
        if (type != null) {
            query.and(QJob.job.type.like(type));
        }
        if (status != null) {
            query.and(QJob.job.status.eq(status));
        }
        return this.findAll(query, pageable);
    }

}
