package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.MockMvcConfigBase;
import cn.edu.zsc.rms.api.rest.dto.JobDTO;
import cn.edu.zsc.rms.domain.Job;
import cn.edu.zsc.rms.domain.JobBuilder;
import cn.edu.zsc.rms.domain.JobLog;
import cn.edu.zsc.rms.domain.JobLogBuilder;
import cn.edu.zsc.rms.service.JobLogService;
import cn.edu.zsc.rms.service.JobService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author pengzheng
 */
@WebMvcTest(JobLogController.class)
public class JobLogControllerTest extends MockMvcConfigBase {

    @MockBean
    private JobLogService jobLogService;


    @Before
    public void setUp() {

    }

    @Test
    @WithMockUser(authorities = {"JOB_LOG_QUERY"})
    public void jobLogQuery() throws Exception {
        List<JobLog> jobLogs = new LinkedList<>();
        JobLog jobLog = new JobLogBuilder().build();
        jobLogs.add(jobLog);

        when(jobLogService.query(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(
                jobLogs,
                PageRequest.of(0, 10, Sort.Direction.DESC, "startTime"),
                1
        ));

        mockMvc.perform(get("/api/rest/job-log/")
                .param("jobId", jobLog.getJobId().toString())
                .param("jobName", jobLog.getJobName())
                .param("jobType", jobLog.getJobType())
                .param("result", jobLog.getResult().toString())
                .param("page", "0")
                .param("size", "10")
                .param("sort", "startTime,DESC")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("job-log/query"));
    }

}
