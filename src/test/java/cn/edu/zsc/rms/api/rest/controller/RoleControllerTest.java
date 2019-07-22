package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.MockMvcConfigBase;
import cn.edu.zsc.rms.api.rest.dto.RoleDTO;
import cn.edu.zsc.rms.domain.Role;
import cn.edu.zsc.rms.domain.RoleBuilder;
import cn.edu.zsc.rms.service.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author hsj
 */
@WebMvcTest(RoleController.class)
public class RoleControllerTest extends MockMvcConfigBase {

    @MockBean
    private RoleService roleService;

    private Role role;
    private List<Role> roles;

    @Before
    public void setUp() throws Exception {
        role = new RoleBuilder().build();
        roles = new LinkedList<>();
        roles.add(role);
    }

    @Test
    @WithMockUser(authorities = "ROLE_QUERY")
    public void query() throws Exception {
        when(roleService.query(role.getName(), role.getType(),
                PageRequest.of(0, 10, Sort.Direction.DESC, "updateTime")
        )).thenReturn(new PageImpl<>(
                roles,
                PageRequest.of(0, 10, Sort.Direction.DESC, "updateTime"), 2
        ));
        mockMvc.perform(get("/api/rest/user/role/")
                .param("name", role.getName())
                .param("type", role.getType().toString())
                .param("page", "0")
                .param("size", "10")
                .param("sort", "updateTime,DESC")
        )
                .andExpect(status().isOk())
                .andDo(document("role/query"));
        verify(roleService).query(role.getName(), role.getType(),
                PageRequest.of(0, 10, Sort.Direction.DESC, "updateTime")
        );
    }

    @Test
    @WithMockUser(authorities = "ROLE_CREATE")
    public void create() throws Exception {
        RoleDTO roleDTO = new RoleDTO(role.getName(), role.getAuthorities(), role.getDataScope(), role.getRemark());
        when(roleService.create(roleDTO.getName(), roleDTO.getAuthorities(), roleDTO.getDataScope(), roleDTO.getRemark()
        )).thenReturn(
                role
        );
        mockMvc.perform(post("/api/rest/user/role/")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(roleDTO))
        )
                .andExpect(status().isOk())
                .andDo(document("role/create"));
        verify(roleService).create(roleDTO.getName(), roleDTO.getAuthorities(), roleDTO.getDataScope(), roleDTO.getRemark()
        );
    }

    @Test
    @WithMockUser(authorities = "ROLE_UPDATE")
    public void update() throws Exception {
        RoleDTO roleDTO = new RoleDTO(role.getName(), role.getAuthorities(), role.getDataScope(), role.getRemark());
        when(roleService.update(role.getId(), roleDTO.getName(), roleDTO.getAuthorities(), roleDTO.getDataScope(), roleDTO.getRemark()
        )).thenReturn(
                role
        );
        mockMvc.perform(patch("/api/rest/user/role/{role-number}", role.getId())
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(roleDTO))
        )
                .andExpect(status().isOk())
                .andDo(document("role/update"));
        verify(roleService).update(role.getId(), roleDTO.getName(), roleDTO.getAuthorities(), roleDTO.getDataScope(), roleDTO.getRemark()
        );
    }
}