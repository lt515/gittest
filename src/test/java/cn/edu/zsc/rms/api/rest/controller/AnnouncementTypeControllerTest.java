package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.MockMvcConfigBase;
import cn.edu.zsc.rms.api.rest.dto.AnnouncementTypeDTO;
import cn.edu.zsc.rms.domain.AnnouncementType;
import cn.edu.zsc.rms.domain.AnnouncementTypeBuilder;
import cn.edu.zsc.rms.service.AnnouncementTypeService;
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

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @author hsj
 */
@WebMvcTest(AnnouncementTypeController.class)
public class AnnouncementTypeControllerTest extends MockMvcConfigBase {

    @MockBean
    AnnouncementTypeService announcementTypeService;

    private AnnouncementType announcementType;
    private AnnouncementType parent;

    @Before
    public void setUp() throws Exception {
        parent = new AnnouncementTypeBuilder().build();
        announcementType = new AnnouncementTypeBuilder().parentId(parent.getId()).build();
    }

    @Test
    @WithMockUser(authorities = {"ANNOUNCEMENT_TYPE_QUERY"})
    public void query() throws Exception {
        List<AnnouncementType> announcementTypes = new LinkedList<>();
        announcementTypes.add(announcementType);
        when(announcementTypeService.query(
                announcementType.getName(),
                announcementType.getParentId(),
                PageRequest.of(0, 10, Sort.Direction.DESC, "updateTime")
        )).thenReturn(new PageImpl<>(
                announcementTypes,
                PageRequest.of(0, 10, Sort.Direction.DESC, "updateTime"), 2
        ));
        mockMvc.perform(get("/api/rest/announcement-type/")
                .param("name", announcementType.getName())
                .param("parentId", announcementType.getParentId().toString())
                .param("page", "0")
                .param("size", "10")
                .param("sort", "updateTime,DESC")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("announcement-type/query"));
        verify(announcementTypeService).query(
                announcementType.getName(),
                announcementType.getParentId(),
                PageRequest.of(0, 10, Sort.Direction.DESC, "updateTime")
        );
    }

    @Test
    @WithMockUser(authorities = {"ANNOUNCEMENT_TYPE_QUERY"})
    public void list() throws Exception {
        List<AnnouncementType> announcementTypes = new LinkedList<>();
        announcementTypes.add(announcementType);
        when(announcementTypeService.list(
        )).thenReturn(
                announcementTypes
        );
        mockMvc.perform(get("/api/rest/announcement-type/list")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("announcement-type/list"));
        verify(announcementTypeService).list();
    }

    @Test
    @WithMockUser(authorities = {"ANNOUNCEMENT_TYPE_QUERY"})
    public void findById() throws Exception{
        when(announcementTypeService.findById(announcementType.getId())).thenReturn(
                announcementType
        );
        mockMvc.perform(get("/api/rest/announcement-type/{announcement-type-id}", announcementType.getId())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("announcement-type/findById"));
        verify(announcementTypeService).findById(announcementType.getId());
    }

    @Test
    @WithMockUser(authorities = {"ANNOUNCEMENT_TYPE_CREATE"})
    public void create() throws Exception {
        AnnouncementTypeDTO announcementTypeDTO = new AnnouncementTypeDTO(
                announcementType.getName(),
                announcementType.getParentId()
        );
        when(announcementTypeService.create(announcementTypeDTO.getName(), announcementTypeDTO.getParentId())).thenReturn(
                announcementType
        );
        mockMvc.perform(post("/api/rest/announcement-type/")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(announcementTypeDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("announcement-type/create"));
        verify(announcementTypeService).create(announcementTypeDTO.getName(), announcementTypeDTO.getParentId());
    }

    @Test
    @WithMockUser(authorities = {"ANNOUNCEMENT_TYPE_DELETE"})
    public void deleteAnnouncementType() throws Exception{
        when(announcementTypeService.delete(announcementType.getId())).thenReturn(
                announcementType
        );
        mockMvc.perform(delete("/api/rest/announcement-type/{announcement-type-id}", announcementType.getId())
                .with(csrf().asHeader())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("announcement-type/delete"));
        verify(announcementTypeService).delete(announcementType.getId());
    }
}