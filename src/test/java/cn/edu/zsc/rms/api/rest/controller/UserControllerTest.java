package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.MockMvcConfigBase;
import cn.edu.zsc.rms.api.rest.dto.UserDTO;
import cn.edu.zsc.rms.domain.*;
import cn.edu.zsc.rms.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


/**
 * @author hsj
 */
@WebMvcTest(UserController.class)
public class UserControllerTest extends MockMvcConfigBase {

    @MockBean
    private UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    private User user;
    private Role role;
    private Role role1;
    private Set<Role> roles;
    private Department department;
    private List<User> users;

    @Before
    public void setUp() throws Exception {
        department = new DepartmentBuilder().build();
        role = new RoleBuilder().build();
        role1 = new RoleBuilder().build();
        roles = new LinkedHashSet<>();
        roles.add(role);
        roles.add(role1);
        user = new UserBuilder(roles, department).build();
        users = new ArrayList<>();
        users.add(user);
    }

    @Test
    public void query() throws Exception {
        when(userService.query(user.getNumber(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getId).collect(Collectors.toSet()),
                user.getDepartment().getId(),
                user.getType(),
                user.getStatus(),
                PageRequest.of(0, 10, Sort.Direction.DESC, "updateTime")
        )).thenReturn(new PageImpl<>(
                users,
                PageRequest.of(0, 10, Sort.Direction.DESC, "updateTime"), 2
        ));
        mockMvc.perform(get("/api/rest/user/")
                .with(user(
                        user.toUserDetail()
                ))
                .param("number", user.getNumber())
                .param("name", user.getName())
                .param("phone", user.getPhone())
                .param("email", user.getEmail())
                .param("roles", role.getId().toString(), role1.getId().toString())
                .param("departmentId", user.getDepartment().getId().toString())
                .param("type", user.getType().name())
                .param("status", user.getStatus().name())
                .param("page", "0")
                .param("size", "10")
                .param("sort", "updateTime,DESC")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user/query"));
        verify(userService).query(user.getNumber(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getId).collect(Collectors.toSet()),
                user.getDepartment().getId(),
                user.getType(),
                user.getStatus(),
                PageRequest.of(0, 10, Sort.Direction.DESC, "updateTime")
        );
    }

    @Test
    public void create() throws Exception {
        UserDTO.CreateDTO createDTO = new UserDTO.CreateDTO(user.getNumber(),
                user.getPassword(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getStatus(),
                user.getRoles().stream().map(Role::getId).collect(Collectors.toSet()),
                user.getDepartment().getId());
        when(userService.create(createDTO.getNumber(),
                createDTO.getPassword(),
                createDTO.getName(),
                createDTO.getPhone(),
                createDTO.getEmail(),
                createDTO.getAvatarUrl(),
                createDTO.getRoles(),
                createDTO.getDepartmentId(),
                createDTO.getStatus()
        )).thenReturn(user);
        mockMvc.perform(post("/api/rest/user/")
                .with(csrf().asHeader())
                .with(user(
                        user.toUserDetail()
                ))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(createDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user/create"));
        verify(userService).create(createDTO.getNumber(),
                createDTO.getPassword(),
                createDTO.getName(),
                createDTO.getPhone(),
                createDTO.getEmail(),
                createDTO.getAvatarUrl(),
                createDTO.getRoles(),
                createDTO.getDepartmentId(),
                createDTO.getStatus()
        );
    }

    @Test
    public void setPassword() throws Exception {
        when(userService.setPassword(user.getId(), user.getPassword())
        ).thenReturn(user);
        mockMvc.perform(patch("/api/rest/user/{user-id}/password", user.getId())
                .with(csrf().asHeader())
                .with(user(
                        user.toUserDetail()
                ))
                .param("password", user.getPassword())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user/set-password"));
        verify(userService).setPassword(user.getId(), user.getPassword());
    }

    @Test
    public void update() throws Exception {
        UserDTO.UpdateDTO updateDTO = new UserDTO.UpdateDTO(
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getStatus(),
                user.getRoles().stream().map(Role::getId).collect(Collectors.toSet()),
                user.getDepartment().getId());
        when(userService.update(user.getId(),
                updateDTO.getName(),
                updateDTO.getPhone(),
                updateDTO.getEmail(),
                updateDTO.getAvatarUrl(),
                updateDTO.getRoles(),
                updateDTO.getDepartmentId(),
                updateDTO.getStatus()
        )).thenReturn(user);
        mockMvc.perform(patch("/api/rest/user/{user-id}", user.getId())
                .with(csrf().asHeader())
                .with(user(
                        user.toUserDetail()
                ))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(updateDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user/update"));
        verify(userService).update(user.getId(),
                updateDTO.getName(),
                updateDTO.getPhone(),
                updateDTO.getEmail(),
                updateDTO.getAvatarUrl(),
                updateDTO.getRoles(),
                updateDTO.getDepartmentId(),
                updateDTO.getStatus()
        );
    }

    @Test
    public void me() throws Exception {
        mockMvc.perform(get("/api/rest/user/me")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user/csrf"));
    }

    @Test
    public void getUserSelfInfo() throws Exception {
        when(userService.findById(user.getId())).thenReturn(user);
        mockMvc.perform(get("/api/rest/user/me/info")
                .with(user(
                        user.toUserDetail()
                ))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user/get-user-self-info"));
        verify(userService).findById(user.getId());
    }

    @Test
    public void updateUserSelfInfo() throws Exception {
        UserDTO.UpdateUserSelfDTO updateUserSelfDTO = new UserDTO.UpdateUserSelfDTO(
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getAvatarUrl()
        );

        when(userService.update(
                user.getId(),
                updateUserSelfDTO.getName(),
                updateUserSelfDTO.getPhone(),
                updateUserSelfDTO.getEmail(),
                updateUserSelfDTO.getAvatarUrl(),
                null,
                null,
                null
        )).thenReturn(user);
        mockMvc.perform(patch("/api/rest/user/me")
                .with(csrf().asHeader())
                .with(user(
                        user.toUserDetail()
                ))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(updateUserSelfDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user/update-user-self-info"));
        verify(userService).update(
                user.getId(),
                updateUserSelfDTO.getName(),
                updateUserSelfDTO.getPhone(),
                updateUserSelfDTO.getEmail(),
                updateUserSelfDTO.getAvatarUrl(),
                null,
                null,
                null
        );
    }

    @Test
    public void setUserSelfPassword() throws Exception {
        when(userService.setPassword(user.getId(), user.getPassword(),user.getPassword())).thenReturn(user);
        mockMvc.perform(patch("/api/rest/user/me/password")
                .with(csrf().asHeader())
                .with(user(
                        user.toUserDetail()
                ))
                .param("oldPassword", user.getPassword())
                .param("password", user.getPassword())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user/set-user-self-password"));
        verify(userService).setPassword(user.getId(), user.getPassword(),user.getPassword());
    }

    @Test
    public void login() throws Exception {
        mockMvc.perform(
                post("/api/rest/user/login")
                        .with(csrf().asHeader())
                        .param("username", "008")
                        .param("password", "008")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user/login"));
    }

    @Test
    public void logout() throws Exception {
        mockMvc.perform(
                post("/api/rest/user/logout")
                        .with(csrf().asHeader())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user/logout"));
    }

    @Test
    public void excelExport() throws Exception {
        doNothing().when(userService).excelExport(any(), any(), any(), any(), any(), any(), any(), any(), any());
        mockMvc.perform(get("/api/rest/user/excel")
                .with(user(
                user.toUserDetail()
                ))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user/excelExport"));
    }

    @Test
    public void excelImport() throws Exception {
        doNothing().when(userService).excelImport(any());
        mockMvc.perform(multipart("/api/rest/user/excel")
                .file(new MockMultipartFile("file",objectMapper.writeValueAsBytes("user.xlsx")))
                .with(csrf().asHeader())
                .with(user(
                        user.toUserDetail()
                ))

        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("user/excelImport"));
        verify(userService).excelImport(any());
    }

}
