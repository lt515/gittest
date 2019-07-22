package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.MockMvcConfigBase;
import cn.edu.zsc.rms.api.rest.dto.AnnouncementDTO;
import cn.edu.zsc.rms.domain.*;
import cn.edu.zsc.rms.service.AnnouncementService;
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

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @author hsj
 */
@WebMvcTest(AnnouncementController.class)
public class AnnouncementControllerTest extends MockMvcConfigBase {

    @MockBean
    private AnnouncementService announcementService;
    private User publishUser;
    private User closeUser;
    private User editUser;
    private Role role;
    private Set<Role> roles;
    private Department department;
    private AnnouncementType announcementType;

    @Before
    public void setUp() throws Exception {
        announcementType = new AnnouncementTypeBuilder().build();
        department = new DepartmentBuilder().build();
        role = new RoleBuilder().build();
        roles = new LinkedHashSet<>();
        roles.add(role);
        editUser = new UserBuilder(roles, department).build();
        publishUser = new UserBuilder(roles, department).build();
        closeUser = new UserBuilder(roles, department).build();
    }

    @Test
    @WithMockUser(authorities = {"ANNOUNCEMENT_QUERY"})
    public void query() throws Exception {
        Announcement announcement = new AnnouncementBuilder(editUser, announcementType).build();
        List<Announcement> announcements = new LinkedList<>();

        LocalDateTime publishTimeBegin = LocalDateTime.now().minusDays(2);
        LocalDateTime publishTimeEnd = LocalDateTime.now().plusDays(2);

        announcements.add(announcement);
        when(announcementService.query(
                announcement.getTitle(),
                announcement.getAnnouncementType().getId(),
                publishTimeBegin,
                publishTimeEnd,
                PageRequest.of(0, 10, Sort.Direction.DESC, "topping", "updateTime")
        )).thenReturn(new PageImpl<>(
                announcements,
                PageRequest.of(0, 10, Sort.Direction.DESC, "topping", "updateTime"), 2
        ));
        mockMvc.perform(get("/api/rest/announcement/")
                .param("title", announcement.getTitle())
                .param("announcementTypeId", announcement.getAnnouncementType().getId().toString())
                .param("publishTimeBegin", publishTimeBegin.toString())
                .param("publishTimeEnd", publishTimeEnd.toString())
                .param("page", "0")
                .param("size", "10")
                .param("sort", "topping,updateTime,DESC")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("announcement/query"));
        verify(announcementService).query(
                announcement.getTitle(),
                announcement.getAnnouncementType().getId(),
                publishTimeBegin,
                publishTimeEnd,
                PageRequest.of(0, 10, Sort.Direction.DESC, "topping", "updateTime")
        );
    }

    @Test
    public void create() throws Exception {
        Announcement announcement = new AnnouncementBuilder(editUser, announcementType).build();
        AnnouncementDTO announcementDTO = new AnnouncementDTO(
                announcement.getTitle(),
                announcement.getAnnouncementType().getId(),
                announcement.getTopping(),
                announcement.getContent(),
                announcement.getFiles()
        );
        when(announcementService.create(
                editUser.getId(),
                announcement.getTitle(),
                announcement.getAnnouncementType().getId(),
                announcement.getTopping(),
                announcement.getContent(),
                announcement.getFiles()
        )).thenReturn(announcement);
        mockMvc.perform(post("/api/rest/announcement/")
                .with(user(editUser.toUserDetail()))
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(announcementDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("announcement/create"));
        verify(announcementService).create(
                editUser.getId(),
                announcement.getTitle(),
                announcement.getAnnouncementType().getId(),
                announcement.getTopping(),
                announcement.getContent(),
                announcement.getFiles()
        );
    }

    @Test
    public void update() throws Exception {
        Announcement announcement = new AnnouncementBuilder(editUser, announcementType).build();
        AnnouncementDTO announcementDTO = new AnnouncementDTO(
                announcement.getTitle(),
                announcement.getAnnouncementType().getId(),
                announcement.getTopping(),
                announcement.getContent(),
                announcement.getFiles()
        );
        when(announcementService.update(
                announcement.getId(),
                editUser.getId(),
                announcement.getTitle(),
                announcement.getAnnouncementType().getId(),
                announcement.getTopping(),
                announcement.getContent(),
                announcement.getFiles()
        )).thenReturn(announcement);
        mockMvc.perform(patch("/api/rest/announcement/{announcement-id}", announcement.getId())
                .with(user(editUser.toUserDetail()))
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(announcementDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("announcement/update"));
        verify(announcementService).update(
                announcement.getId(),
                editUser.getId(),
                announcement.getTitle(),
                announcement.getAnnouncementType().getId(),
                announcement.getTopping(),
                announcement.getContent(),
                announcement.getFiles()
        );
    }

    @Test
    public void deleteAnnouncement() throws Exception {
        Announcement announcement = new AnnouncementBuilder(editUser, announcementType).build();
        when(announcementService.delete(
                announcement.getId()
        )).thenReturn(announcement);
        mockMvc.perform(delete("/api/rest/announcement/{announcement-id}", announcement.getId())
                .with(user(editUser.toUserDetail()))
                .with(csrf().asHeader())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("announcement/delete"));
        verify(announcementService).delete(
                announcement.getId()
        );
    }

    @Test
    public void publish() throws Exception {
        Announcement announcement = new AnnouncementBuilder(editUser, announcementType).publishUser(publishUser)
                .status(Announcement.AnnouncementStatus.PUBLISHED)
                .publishTime(LocalDateTime.now().plusDays(2)).build();
        when(announcementService.publish(
                announcement.getId(),
                publishUser.getId()
        )).thenReturn(announcement);
        mockMvc.perform(patch("/api/rest/announcement/{announcement-id}/publish", announcement.getId())
                .with(user(publishUser.toUserDetail()))
                .with(csrf().asHeader())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("announcement/publish"));
        verify(announcementService).publish(
                announcement.getId(),
                publishUser.getId()
        );
    }

    @Test
    public void close() throws Exception {
        Announcement announcement = new AnnouncementBuilder(editUser, announcementType).publishUser(publishUser).closeUser(closeUser)
                .status(Announcement.AnnouncementStatus.CLOSED)
                .publishTime(LocalDateTime.now().plusDays(2))
                .closeTime(LocalDateTime.now().plusDays(5)).build();
        when(announcementService.close(
                announcement.getId(),
                closeUser.getId()
        )).thenReturn(announcement);
        mockMvc.perform(patch("/api/rest/announcement/{announcement-id}/close", announcement.getId())
                .with(user(closeUser.toUserDetail()))
                .with(csrf().asHeader())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("announcement/close"));
        verify(announcementService).close(
                announcement.getId(),
                closeUser.getId()
        );
    }
}