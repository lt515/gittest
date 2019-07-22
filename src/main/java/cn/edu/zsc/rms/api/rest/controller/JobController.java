package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.dto.JobDTO;
import cn.edu.zsc.rms.domain.Job;
import cn.edu.zsc.rms.service.JobService;
import cn.edu.zsc.rms.spring.security.UserDetailsImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author pengzheng
 */

@RestController
@RequestMapping("api/rest/job")
public class JobController {

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('JOB_QUERY')")
    public Page<Job> query(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Job.JobStatus status,
            @PageableDefault(
                    sort = {"updateTime"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return jobService.query(name, type, status, pageable);
    }

    @GetMapping("/progress")
    @PreAuthorize("hasAuthority('JOB_QUERY')")
    public Map queryProgress(
            @RequestBody List<UUID> ids
    ) {
        return jobService.queryProgress(ids);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('JOB_CREATE')")
    public Job create(
            @RequestBody JobDTO jobDTO
    ) {
        Job job = new Job();
        BeanUtils.copyProperties(jobDTO, job);
        return jobService.create(job);
    }

    @PatchMapping("/{job-id}")
    @PreAuthorize("hasAuthority('JOB_UPDATE')")
    public Job update(
            @PathVariable("job-id") UUID jobId,
            @RequestBody JobDTO jobDTO
    ) {
        Job job = new Job();
        BeanUtils.copyProperties(jobDTO, job);
        job.setId(jobId);
        return jobService.update(job);
    }

    @PatchMapping("/{job-id}/toggle-status")
    @PreAuthorize("hasAuthority('JOB_UPDATE')")
    public Job toggleStatus(
            @PathVariable("job-id") UUID jobId
    ) {
        return jobService.toggleStatus(jobId);
    }

    @PatchMapping("/{job-id}/trigger")
    @PreAuthorize("hasAuthority('JOB_TRIGGER')")
    public Boolean trigger(
            @PathVariable("job-id") UUID jobId,
            @AuthenticationPrincipal UserDetailsImpl principal
    ) {
        return jobService.triggerJob(jobId, principal.getUuid());
    }

    @DeleteMapping("/{job-id}")
    @PreAuthorize("hasAuthority('JOB_DELETE')")
    public Job delete(
            @PathVariable("job-id") UUID jobId
    ) {
        return jobService.delete(jobId);
    }


}
