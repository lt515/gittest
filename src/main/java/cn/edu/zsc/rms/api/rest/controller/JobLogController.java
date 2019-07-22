package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.domain.Job;
import cn.edu.zsc.rms.domain.JobLog;
import cn.edu.zsc.rms.service.JobLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author pengzheng
 */

@RestController
@RequestMapping("api/rest/job-log")
public class JobLogController {

    private JobLogService jobLogService;

    public JobLogController(JobLogService jobLogService) {
        this.jobLogService = jobLogService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('JOB_LOG_QUERY')")
    public Page<JobLog> query(
            @RequestParam(required = false) UUID jobId,
            @RequestParam(required = false) String jobName,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) JobLog.JobLogResult result,
            @PageableDefault(
                    sort = {"startTime"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return jobLogService.query(jobId, jobName, jobType, result, pageable);
    }
}
