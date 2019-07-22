package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.*;
import cn.edu.zsc.rms.repository.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * @author pengzheng
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobServiceTest {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobLogRepository jobLogRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    private Job job;
    private User user;

    @Before
    public void setUp() {
        job = new JobBuilder().build();
        Department department = new DepartmentBuilder().build();
        Role role = new RoleBuilder().build();
        Set<Role> roles = new LinkedHashSet<>();
        roles.add(role);
        departmentRepository.save(department);
        roleRepository.save(role);
        user = new UserBuilder(roles, department).build();
        userRepository.save(user);
    }

    @After
    public void tearDown() {
        jobRepository.deleteAll();
        jobLogRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    public void queryJob() throws Exception {
        Job job1 = new JobBuilder().name("hello").cron("0/2 0 0 ? * * *").build();
        Job job2 = new JobBuilder().status(Job.JobStatus.DISABLE).build();
        Job job3 = new JobBuilder().status(Job.JobStatus.DISABLE).build();
        jobRepository.save(job1);
        jobRepository.save(job2);
        jobRepository.save(job3);
//        Thread.sleep(5 * 1000L);
        Page<Job> jobs;
        jobs = jobService.query(null, null, null, Pageable.unpaged());
        assertEquals(3L, jobs.getContent().size());
        jobs = jobService.query("hello", null, null, Pageable.unpaged());
        assertEquals(1L, jobs.getContent().size());
    }

    @Test
    public void createJob() throws Exception {
        Job create = jobService.create(job);
//        Thread.sleep(5 * 1000L);
        assertEquals(create.getType(), job.getType());
    }

    @Test
    public void triggerJob() throws Exception {
        job.setCron("0 0 0/2 ? * * *");
        Job create = jobService.create(job);
        boolean result = jobService.triggerJob(create.getId(), user.getId());
//        Thread.sleep(5 * 1000L);
        assertEquals(Boolean.TRUE, result);
    }
}
