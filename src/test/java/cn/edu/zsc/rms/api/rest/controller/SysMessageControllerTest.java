package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.MockMvcConfigBase;
import cn.edu.zsc.rms.api.rest.dto.MessageDTO;
import cn.edu.zsc.rms.domain.*;
import cn.edu.zsc.rms.service.SysMessageService;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @author hsj
 */
@WebMvcTest(SysMessageController.class)
public class SysMessageControllerTest extends MockMvcConfigBase {

    @MockBean
    private SysMessageService sysMessageService;

    private Message message;
    private User user;
    private Role role;
    private Set<Role> roles;
    private Department department;
    private UserMessage userMessage;

    @Before
    public void setUp() throws Exception {
        department = new DepartmentBuilder().build();
        role = new RoleBuilder().build();
        roles = new LinkedHashSet<>();
        roles.add(role);
        user = new UserBuilder(roles, department).email("hsj215@aliyun.com").build();
        Set<Message.PushMethod> pushMethods = new LinkedHashSet<>();
        pushMethods.add(Message.PushMethod.EMAIl);
        message = new MessageBuilder().pushMethods(pushMethods).build();
        userMessage = new UserMessageBuilder(user, message).build();

    }

    @Test
    @WithMockUser(authorities = {"MESSAGE_QUERY"})
    public void query() throws Exception {
        List<UserMessage> userMessages = new LinkedList<>();
        userMessages.add(userMessage);
        when(sysMessageService.query(
                message.getTitle(),
                message.getType(),
                user.getNumber(),
                message.getCreateBy(),
                Message.PushMethod.EMAIl,
                userMessage.getHaveRead(),
                message.getSendTime(),
                message.getSendTime().plusDays(2),
                PageRequest.of(0, 10, Sort.Direction.DESC, "message.sendTime")
        )).thenReturn(new PageImpl<>(
                userMessages,
                PageRequest.of(0, 10, Sort.Direction.DESC, "message.sendTime"), 1
        ));
        mockMvc.perform(get("/api/rest/message/")
                .param("title", message.getTitle())
                .param("type", message.getType().name())
                .param("userNameOrNumber", user.getNumber())
                .param("createBy", message.getCreateBy().name())
                .param("pushMethod", Message.PushMethod.EMAIl.name())
                .param("haveRead", userMessage.getHaveRead().toString())
                .param("sendTimeBegin", message.getSendTime().toString())
                .param("sendTimeEnd",  message.getSendTime().plusDays(2).toString())
                .param("page", "0")
                .param("size", "10")
                .param("sort", "message.sendTime,DESC")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("message/query"));

        verify(sysMessageService).query(
                message.getTitle(),
                message.getType(),
                user.getNumber(),
                message.getCreateBy(),
                Message.PushMethod.EMAIl,
                userMessage.getHaveRead(),
                message.getSendTime(),
                message.getSendTime().plusDays(2),
                PageRequest.of(0, 10, Sort.Direction.DESC, "message.sendTime")
        );
    }

    @Test
    @WithMockUser(authorities = {"MESSAGE_CREATE"})
    public void create() throws Exception {
        Set<UUID> userIds = new LinkedHashSet<>();
        userIds.add(user.getId());
        List<UserMessage> userMessages = new LinkedList<>();
        userMessages.add(userMessage);
        MessageDTO messageDTO = new MessageDTO(
                userIds,
                message.getTitle(),
                message.getType(),
                message.getCreateBy(),
                message.getContent(),
                message.getBriefContent(),
                message.getPushMethods(),
                message.getFiles()
        );
        when(sysMessageService.create(messageDTO.getUserIds(),messageDTO.getTitle(), messageDTO.getType(),
                messageDTO.getCreateBy(), messageDTO.getContent(),
                messageDTO.getBriefContent(), messageDTO.getPushMethods(),
                messageDTO.getFiles())).thenReturn(userMessages);
        mockMvc.perform(post("/api/rest/message/")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(messageDTO))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("message/create"));

        verify(sysMessageService).create(messageDTO.getUserIds(),messageDTO.getTitle(), messageDTO.getType(),
                messageDTO.getCreateBy(), messageDTO.getContent(),
                messageDTO.getBriefContent(), messageDTO.getPushMethods(),
                messageDTO.getFiles());
    }

    @Test
    @WithMockUser(authorities = {"MESSAGE_DELETE"})
    public void deleteUserMessage() throws Exception {
        when(sysMessageService.delete(new UserMessage.UserMessageId(user.getId(), message.getId()))).thenReturn(userMessage);
        mockMvc.perform(delete("/api/rest/message/{user-id}/{message-id}", user.getId(), message.getId())
                .with(csrf().asHeader())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("message/delete"));

        verify(sysMessageService).delete(new UserMessage.UserMessageId(user.getId(), message.getId()));
    }
}