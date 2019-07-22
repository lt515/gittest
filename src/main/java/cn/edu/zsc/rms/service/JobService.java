package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.Job;
import cn.edu.zsc.rms.domain.User;
import cn.edu.zsc.rms.exception.api.EntityNotExistException;
import cn.edu.zsc.rms.repository.JobRepository;
import cn.edu.zsc.rms.schedule.ScheduleUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author pengzheng
 */
@Service
public class JobService {

    private static final Logger log = LoggerFactory.getLogger(JobService.class);


    private Scheduler scheduler;

    private JobRepository jobRepo;

    private UserService userService;

    public JobService(Scheduler scheduler, JobRepository jobRepo, UserService userService) {
        this.scheduler = scheduler;
        this.jobRepo = jobRepo;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        List<Job> jobList = jobRepo.findAll();
        for (Job job : jobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, job.getId());
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, job);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, job);
            }
        }
    }

    public Page<Job> query(String name, String type, Job.JobStatus status, Pageable pageable) {
        return jobRepo.query(name, type, status, pageable);
    }

    public Job findById(UUID id) {
        return jobRepo.findById(id).orElseThrow(() -> new EntityNotExistException(Job.class));
    }

    public Job create(String name, String type, String beanName, String methodName, String methodParams, String cron, Job.JobStatus status) {
        Job job = new Job(name, type, beanName, methodName, methodParams, cron, status, LocalDateTime.now(), UUID.randomUUID());
        return jobRepo.save(job);
    }

    public Job create(Job create) {
        create.setUpdateTime(LocalDateTime.now());
        create.setId(UUID.randomUUID());
        Job job = jobRepo.save(create);
        ScheduleUtils.createScheduleJob(scheduler, job);
        return job;
    }

    public Job update(Job update) {
        Job job = findById(update.getId());
        BeanUtils.copyProperties(update, job);
        job.setUpdateTime(LocalDateTime.now());
        job = jobRepo.save(job);
        ScheduleUtils.updateScheduleJob(scheduler, job);
        return job;
    }

    public Job delete(UUID id) {
        Job job = findById(id);
        jobRepo.delete(job);
        ScheduleUtils.deleteScheduleJob(scheduler, id);
        return job;
    }

    private Job resumeJob(Job resume) {
        resume.setStatus(Job.JobStatus.ENABLE);
        resume.setUpdateTime(LocalDateTime.now());
        Job job = jobRepo.save(resume);
        ScheduleUtils.resumeScheduleJob(scheduler, job.getId());
        return job;
    }

    private Job pauseJob(Job pause) {
        pause.setStatus(Job.JobStatus.DISABLE);
        pause.setUpdateTime(LocalDateTime.now());
        Job job = jobRepo.save(pause);
        ScheduleUtils.pauseScheduleJob(scheduler, job.getId());
        return job;
    }

    public Job toggleStatus(UUID id) {
        Job job = findById(id);
        if (job.getStatus() == Job.JobStatus.ENABLE) {
            return pauseJob(job);
        }
        if (job.getStatus() == Job.JobStatus.DISABLE) {
            return resumeJob(job);
        }
        return job;
    }

    public boolean triggerJob(UUID id, UUID userId) {
        Job job = findById(id);
        User user = userService.findById(userId);
        return ScheduleUtils.triggerScheduleJob(scheduler, job, user.getName() + ":" + user.getNumber());
    }

    public Map queryProgress(List<UUID> ids) {
        Map<UUID, Double> map = new HashMap<>();
        for(UUID id : ids) {
            Job job = findById(id);
            String progressKey = job.getBeanName() + "-" + job.getMethodName();
            map.put(id, ScheduleUtils.progressPercentage.get(progressKey));
        }
        return map;
    }

}