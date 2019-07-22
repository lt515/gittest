package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.MockMvcConfigBase;
import cn.edu.zsc.rms.domain.*;
import cn.edu.zsc.rms.service.OnlineUserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author hsj
 */
@WebMvcTest(OnlineUserController.class)
public class OnlineUserControllerTest extends MockMvcConfigBase {

    @MockBean
    private OnlineUserService onlineUserService;

    private User user;
    private Role role;
    private Set<Role> roles;
    private Department department;
    private OnlineUser onlineUser;

    @Before
    public void setUp() throws Exception {
        department = new DepartmentBuilder().build();
        role = new RoleBuilder().build();
        roles = new LinkedHashSet<>();
        roles.add(role);
        user = new UserBuilder(roles, department).build();
        onlineUser = new OnlineUserBuilder(user.getName(), user.getNumber(), department.getName(), department.getNumber()).build();
    }

    @Test
    @WithMockUser(authorities = {"ONLINE_USER_QUERY"})
    public void query() throws Exception {
        List<OnlineUser> onlineUsers = new LinkedList<>();
        onlineUsers.add(onlineUser);
        when(onlineUserService.query(user.getName(), user.getNumber(),
                department.getNumber(), onlineUser.getIp(),
                onlineUser.getLoginTime(), onlineUser.getLoginTime().plusDays(2),
                PageRequest.of(0, 10, Sort.Direction.DESC, "loginTime")
        )).thenReturn(new PageImpl<>(
                onlineUsers,
                PageRequest.of(0, 10, Sort.Direction.DESC, "loginTime"), 2
        ));
        mockMvc.perform(get("/api/rest/online-user/")
                .param("userName", user.getName())
                .param("userNumber", user.getNumber())
                .param("departmentNumber", department.getNumber())
                .param("ip", onlineUser.getIp())
                .param("loginTimeBegin", onlineUser.getLoginTime().toString())
                .param("loginTimeEnd", onlineUser.getLoginTime().plusDays(2).toString())
                .param("page", "0")
                .param("size", "10")
                .param("sort", "loginTime,DESC")
        )
                .andExpect(status().isOk())
                .andDo(document("online-user/query"));
        verify(onlineUserService).query(user.getName(), user.getNumber(),
                department.getNumber(), onlineUser.getIp(),
                onlineUser.getLoginTime(), onlineUser.getLoginTime().plusDays(2),
                PageRequest.of(0, 10, Sort.Direction.DESC, "loginTime")
        );
    }

    @Test
    @WithMockUser(authorities = {"ONLINE_USER_DELETE"})
    public void forceLogout() throws Exception {
        when(onlineUserService.forceLogout(user.getNumber())).thenReturn(onlineUser);
        mockMvc.perform(delete("/api/rest/online-user/{user-number}", user.getNumber())
               .with(csrf().asHeader())
        )
                .andExpect(status().isOk())
                .andDo(document("online-user/force-logout"));
        verify(onlineUserService).forceLogout(user.getNumber());
    }
}