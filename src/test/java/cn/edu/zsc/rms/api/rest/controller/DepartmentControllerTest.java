package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.MockMvcConfigBase;
import cn.edu.zsc.rms.domain.Department;
import cn.edu.zsc.rms.domain.DepartmentBuilder;
import cn.edu.zsc.rms.service.DepartmentService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @author hsj
 */
@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest extends MockMvcConfigBase {

    @MockBean
    private DepartmentService departmentService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @WithMockUser(authorities = {"DEPT_QUERY"})
    public void list() throws Exception {
        Department department = new DepartmentBuilder().build();
        List<Department> departments = new LinkedList<>();

        departments.add(department);
        when(departmentService.list()).thenReturn(departments);
        mockMvc.perform(get("/api/rest/department/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("department/list"));
        verify(departmentService).list();
    }
}