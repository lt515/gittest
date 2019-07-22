package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.dto.MessageDTO;
import cn.edu.zsc.rms.domain.Message;
import cn.edu.zsc.rms.domain.UserMessage;
import cn.edu.zsc.rms.service.SysMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/message")
public class SysMessageController {

    private SysMessageService messageService;

    public SysMessageController(SysMessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MESSAGE_QUERY')")
    public Page<UserMessage> query(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Message.MessageType type,
            @RequestParam(required = false) String userNameOrNumber,
            @RequestParam(required = false) Message.CreateBy createBy,
            @RequestParam(required = false) Message.PushMethod pushMethod,
            @RequestParam(required = false) Boolean haveRead,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime sendTimeBegin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime sendTimeEnd,
            @PageableDefault(
                    sort = {"message.sendTime"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return messageService.query(title,
                type,
                userNameOrNumber,
                createBy,
                pushMethod,
                haveRead,
                sendTimeBegin,
                sendTimeEnd,
                pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MESSAGE_CREATE')")
    public List<UserMessage> create(
            @RequestBody MessageDTO messageDTO
    ) {
        return messageService.create(messageDTO.getUserIds(), messageDTO.getTitle(), messageDTO.getType(),
                messageDTO.getCreateBy(), messageDTO.getContent(),
                messageDTO.getBriefContent(), messageDTO.getPushMethods(),
                messageDTO.getFiles());
    }

    @DeleteMapping("/{user-id}/{message-id}")
    @PreAuthorize("hasAuthority('MESSAGE_DELETE')")
    public UserMessage delete(
            @PathVariable("user-id") UUID userId,
            @PathVariable("message-id") UUID messageId
    ) {
        return messageService.delete(new UserMessage.UserMessageId(userId, messageId));
    }
}
