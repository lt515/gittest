package cn.edu.zsc.rms.schedule;

import cn.edu.zsc.rms.domain.Job;
import cn.edu.zsc.rms.domain.JobLog;
import cn.edu.zsc.rms.service.JobLogService;
import cn.edu.zsc.rms.spring.SpringBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 定时任务
 *
 * @author pengzheng
 */
public class ScheduleJob extends QuartzJobBean {
    private static final Logger log = LoggerFactory.getLogger(ScheduleJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();

        Job job = new Job();
        BeanUtils.copyProperties(jobDataMap.get(ScheduleUtils.TASK_PROPERTIES), job);

        JobLog jobLog = new JobLog();
        jobLog.setJobId(job.getId());
        jobLog.setJobName(job.getName());
        jobLog.setJobType(job.getType());
        jobLog.setJobBeanName(job.getBeanName());
        jobLog.setJobMethodName(job.getMethodName());
        jobLog.setJobMethodParams(job.getMethodParams());

        if(jobDataMap.containsKey(ScheduleUtils.TASK_EXECUTOR)) {
            jobLog.setExecutor(jobDataMap.get(ScheduleUtils.TASK_EXECUTOR).toString());
        }

        LocalDateTime startTime = LocalDateTime.now();
        jobLog.setStartTime(startTime);

        log.info("任务开始执行 - 名称：{} 方法：{}", job.getBeanName(), job.getMethodName());
        try {
            executeJob(job.getBeanName(), job.getMethodName(), job.getMethodParams());
            jobLog.setResult(JobLog.JobLogResult.SUCCESS);
            log.info("任务执行成功 - 名称：{} 方法：{}", job.getBeanName(), job.getMethodName());
        } catch (Exception e) {
            log.error("任务执行失败 - 名称：{} 方法：{} - 异常信息：{}", job.getBeanName(), job.getMethodName(), e);
            jobLog.setResult(JobLog.JobLogResult.FAILURE);
            jobLog.setMessage(StringUtils.abbreviate(e.getLocalizedMessage(), 255));
        } finally {
            LocalDateTime endTime = LocalDateTime.now();
            jobLog.setDuration(Duration.between(startTime, endTime).getSeconds());

            JobLogService jobLogService = SpringBeanUtil.getBean(JobLogService.class);
            jobLogService.create(jobLog);
        }
    }

    private void executeJob(String beanName, String methodName, String params) throws Exception {
        Object bean = SpringBeanUtil.getBean(beanName);
        Method method;
        if (StringUtils.isNotEmpty(params)) {
            method = bean.getClass().getDeclaredMethod(methodName, String.class);
        } else {
            method = bean.getClass().getDeclaredMethod(methodName);
        }
        ReflectionUtils.makeAccessible(method);
        if (StringUtils.isNotEmpty(params)) {
            method.invoke(bean, params);
        } else {
            method.invoke(bean);
        }
    }

}
