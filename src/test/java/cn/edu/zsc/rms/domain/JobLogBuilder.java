package cn.edu.zsc.rms.domain;

import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * @author pengzheng
 */
public final class JobLogBuilder {
    private JobLog jobLog;

    public JobLogBuilder() {
        jobLog = new JobLog(
                UUID.randomUUID(),
                randomAlphabetic(5),
                randomAlphabetic(5),
                "simulationJob",
                "log",
                null,
                randomAlphabetic(50),
                JobLog.JobLogResult.SUCCESS,
                LocalDateTime.now().minus(30, ChronoUnit.MINUTES),
                RandomUtils.nextLong(100, 200),
                "SYSTEM",
                RandomUtils.nextLong(100,200)
        );
    }

    public static JobLogBuilder aJobLog() {
        return new JobLogBuilder();
    }

    public JobLogBuilder jobId(UUID  jobId) {
        jobLog.setJobId(jobId);
        return this;
    }

    public JobLogBuilder jobName(String jobName) {
        jobLog.setJobName(jobName);
        return this;
    }

    public JobLogBuilder jobType(String jobType) {
        jobLog.setJobType(jobType);
        return this;
    }

    public JobLogBuilder jobBeanName(String jobBeanName) {
        jobLog.setJobBeanName(jobBeanName);
        return this;
    }

    public JobLogBuilder jobMethodName(String jobMethodName) {
        jobLog.setJobMethodName(jobMethodName);
        return this;
    }

    public JobLogBuilder jobMethodParams(String jobMethodParams) {
        jobLog.setJobMethodParams(jobMethodParams);
        return this;
    }

    public JobLogBuilder message(String message) {
        jobLog.setMessage(message);
        return this;
    }

    public JobLogBuilder result(JobLog.JobLogResult result) {
        jobLog.setResult(result);
        return this;
    }

    public JobLogBuilder startTime(LocalDateTime startTime) {
        jobLog.setStartTime(startTime);
        return this;
    }

    public JobLogBuilder duration(Long duration) {
        jobLog.setDuration(duration);
        return this;
    }

    public JobLogBuilder executor(String executor) {
        jobLog.setExecutor(executor);
        return this;
    }

    public JobLogBuilder id(Long id) {
        jobLog.setId(id);
        return this;
    }

    public JobLog build() {
        return jobLog;
    }
}
