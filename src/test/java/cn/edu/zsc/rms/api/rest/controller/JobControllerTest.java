package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.MockMvcConfigBase;
import cn.edu.zsc.rms.api.rest.dto.JobDTO;
import cn.edu.zsc.rms.domain.*;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author pengzheng
 */
@WebMvcTest(JobController.class)
public class JobControllerTest extends MockMvcConfigBase {

    @MockBean
    private JobService jobService;

    private Job job;
    private JobDTO jobDTO;

    private User user;

    @Before
    public void setUp() {
        job = new JobBuilder().build();
        jobDTO = new JobDTO(
                job.getName(),
                job.getType(),
                job.getBeanName(),
                job.getMethodName(),
                job.getMethodParams(),
                job.getCron(),
                job.getStatus()
        );
        Department department = new DepartmentBuilder().build();
        Role role = new RoleBuilder().build();
        Set<Role> roles = new LinkedHashSet<>();
        roles.add(role);
        user = new UserBuilder(roles, department).build();
    }

    @Test
    @WithMockUser(authorities = {"JOB_QUERY"})
    public void jobQuery() throws Exception {
        List<Job> jobs = new LinkedList<>();
        jobs.add(job);

        when(jobService.query(any(), any(), any(), any())).thenReturn(new PageImpl<>(
                jobs,
                PageRequest.of(0, 10, Sort.Direction.DESC, "updateTime"),
                1
        ));

        mockMvc.perform(get("/api/rest/job/")
                .param("name", job.getName())
                .param("type", job.getType())
                .param("status", job.getStatus().toString())
                .param("page", "0")
                .param("size", "10")
                .param("sort", "updateTime,DESC")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("job/query"));
    }

    @Test
    @WithMockUser(authorities = {"JOB_QUERY"})
    public void jobQueryProgress() throws Exception {
        List<UUID> ids = new LinkedList<>();
        Map<UUID, Double> map = new HashMap<>();
        Job job2 = new JobBuilder().build();

        ids.add(job.getId());
        map.put(job.getId(), RandomUtils.nextDouble(0.0, 100.0));
        ids.add(job2.getId());
        map.put(job2.getId(), RandomUtils.nextDouble(0.0, 100.0));

        when(jobService.queryProgress(ids)).thenReturn(map);

        mockMvc.perform(get("/api/rest/job/progress")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(ids))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("job/progress"));
    }

    @Test
    @WithMockUser(authorities = {"JOB_CREATE"})
    public void jobCreate() throws Exception {
        when(jobService.create(any(Job.class))).thenReturn(job);

        mockMvc.perform(post("/api/rest/job/")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(jobDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name", equalTo(job.getName())))
                .andDo(document("job/create"));
    }

    @Test
    @WithMockUser(authorities = {"JOB_UPDATE"})
    public void jobUpdate() throws Exception {
        when(jobService.update(any(Job.class))).thenReturn(job);

        mockMvc.perform(patch("/api/rest/job/{job-id}", job.getId())
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(jobDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name", equalTo(jobDTO.getName())))
                .andDo(document("job/update"));
    }

    @Test
    @WithMockUser(authorities = {"JOB_UPDATE"})
    public void jobToggleStatus() throws Exception {
        when(jobService.toggleStatus(job.getId())).thenReturn(job);

        mockMvc.perform(patch("/api/rest/job/{job-id}/toggle-status", job.getId())
                .with(csrf().asHeader())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("job/toggle-status"));
    }

    @Test
        @WithMockUser(authorities = {"JOB_TRIGGER"})
    public void jobTrigger() throws Exception {
        when(jobService.triggerJob(job.getId(), user.getId())).thenReturn(Boolean.TRUE);

        mockMvc.perform(patch("/api/rest/job/{job-id}/trigger", job.getId())
                .with(user(user.toUserDetail()))
                .with(csrf().asHeader())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("job/trigger"));
    }

    @Test
    @WithMockUser(authorities = {"JOB_DELETE"})
    public void jobDelete() throws Exception {
        when(jobService.delete(any())).thenReturn(job);

        mockMvc.perform(delete("/api/rest/job/{job-id}", job.getId())
                .with(csrf().asHeader())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("job/delete"));
    }


}
