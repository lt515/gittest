package cn.edu.zsc.rms.schedule;

import cn.edu.zsc.rms.domain.Job;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时任务工具类
 *
 * @author pengzheng
 */
public class ScheduleUtils {
    private static final Logger log = LoggerFactory.getLogger(ScheduleUtils.class);

    private static final String TASK_CLASS_NAME = "__TASK_CLASS_NAME__";

    static final String TASK_PROPERTIES = "__TASK_PROPERTIES__";

    static final String TASK_EXECUTOR = "__TASK_EXECUTOR__";

    public static final ConcurrentHashMap<String, Double> progressPercentage = new ConcurrentHashMap<>();

    private static TriggerKey getTriggerKey(UUID jobId) {
        return TriggerKey.triggerKey(TASK_CLASS_NAME + jobId);
    }

    private static JobKey getJobKey(UUID jobId) {
        return JobKey.jobKey(TASK_CLASS_NAME + jobId);
    }

    public static CronTrigger getCronTrigger(Scheduler scheduler, UUID jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            log.error("getCronTrigger 异常：", e);
        }
        return null;
    }

    public static void createScheduleJob(Scheduler scheduler, Job job) {
        try {
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(job.getId())).build();

            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(job.getId())).withSchedule(cronScheduleBuilder).build();

            // 放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(TASK_PROPERTIES, job);

            scheduler.scheduleJob(jobDetail, trigger);

            log.warn("createScheduleJob");
            log.warn(job.toString());
            log.warn(jobDetail.toString());
            // 暂停任务
            if (job.getStatus() == Job.JobStatus.DISABLE) {
                pauseScheduleJob(scheduler, job.getId());
            }
        } catch (SchedulerException e) {
            log.error("createScheduleJob 异常：", e);
        }
    }

    public static void updateScheduleJob(Scheduler scheduler, Job job) {
        try {
            TriggerKey triggerKey = getTriggerKey(job.getId());

            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());

            CronTrigger trigger = getCronTrigger(scheduler, job.getId());

            // 按新的cronExpression表达式重新构建trigger
            if (trigger == null) {
                return;
            }
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

            // 参数
            trigger.getJobDataMap().put(TASK_PROPERTIES, job);

            scheduler.rescheduleJob(triggerKey, trigger);

            // 暂停任务
            if (job.getStatus() == Job.JobStatus.DISABLE) {
                pauseScheduleJob(scheduler, job.getId());
            }
        } catch (SchedulerException e) {
            log.error("SchedulerException 异常：", e);
        }
    }

    public static void deleteScheduleJob(Scheduler scheduler, UUID jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            log.error("deleteScheduleJob 异常：", e);
        }
    }

    public static void pauseScheduleJob(Scheduler scheduler, UUID jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            log.error("pauseJob 异常：", e);
        }
    }

    public static void resumeScheduleJob(Scheduler scheduler, UUID jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            log.error("resumeJob 异常：", e);
        }
    }

    public static boolean triggerScheduleJob(Scheduler scheduler, Job job, String executor) {
        boolean result = true;
        try {
            // 参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(TASK_PROPERTIES, job);
            dataMap.put(TASK_EXECUTOR, executor);
            scheduler.triggerJob(getJobKey(job.getId()), dataMap);
        } catch (SchedulerException e) {
            log.error("trigger 异常：", e);
            result = false;
        }
        return result;
    }

}
