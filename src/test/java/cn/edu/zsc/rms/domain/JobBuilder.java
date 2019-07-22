package cn.edu.zsc.rms.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public final class JobBuilder {
    private Job job;

    public JobBuilder() {
        job = new Job(
                randomAlphabetic(5),
                randomAlphabetic(5),
                "simulationJob",
                "log",
                null,
                "* * * * * ? *",
                Job.JobStatus.ENABLE,
                LocalDateTime.now(),
                UUID.randomUUID()
        );
    }

    public static JobBuilder aJob() {
        return new JobBuilder();
    }

    public JobBuilder name(String name) {
        job.setName(name);
        return this;
    }

    public JobBuilder type(String type) {
        job.setType(type);
        return this;
    }

    public JobBuilder beanName(String beanName) {
        job.setBeanName(beanName);
        return this;
    }

    public JobBuilder methodName(String methodName) {
        job.setMethodName(methodName);
        return this;
    }

    public JobBuilder methodParams(String methodParams) {
        job.setMethodParams(methodParams);
        return this;
    }

    public JobBuilder cron(String cron) {
        job.setCron(cron);
        return this;
    }

    public JobBuilder status(Job.JobStatus status) {
        job.setStatus(status);
        return this;
    }

    public JobBuilder updateTime(LocalDateTime updateTime) {
        job.setUpdateTime(updateTime);
        return this;
    }

    public JobBuilder id(UUID id) {
        job.setId(id);
        return this;
    }

    public Job build() {
        return job;
    }
}
