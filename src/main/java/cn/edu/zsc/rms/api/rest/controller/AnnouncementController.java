package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.dto.AnnouncementDTO;
import cn.edu.zsc.rms.domain.Announcement;
import cn.edu.zsc.rms.service.AnnouncementService;
import cn.edu.zsc.rms.spring.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/announcement")
public class AnnouncementController {

    private AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_QUERY')")
    public Page<Announcement> query(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) UUID announcementTypeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime publishTimeBegin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime publishTimeEnd,
            @PageableDefault(
                    sort = {"topping", "updateTime"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return announcementService.query(title, announcementTypeId, publishTimeBegin, publishTimeEnd, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_CREATE')")
    public Announcement create(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @RequestBody AnnouncementDTO announcementDTO
    ) {
        return announcementService.create(
                principal.getUuid(),
                announcementDTO.getTitle(),
                announcementDTO.getAnnouncementTypeId(),
                announcementDTO.getTopping(),
                announcementDTO.getContent(),
                announcementDTO.getFiles()
        );
    }

    @PatchMapping("/{announcement-id}")
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_UPDATE')")
    public Announcement update(
            @PathVariable("announcement-id") UUID announcementId,
            @AuthenticationPrincipal UserDetailsImpl principal,
            @RequestBody AnnouncementDTO announcementDTO
    ) {
        return announcementService.update(
                announcementId,
                principal.getUuid(),
                announcementDTO.getTitle(),
                announcementDTO.getAnnouncementTypeId(),
                announcementDTO.getTopping(),
                announcementDTO.getContent(),
                announcementDTO.getFiles()
        );
    }

    @DeleteMapping("/{announcement-id}")
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_DELETE')")
    public Announcement delete(
            @PathVariable("announcement-id") UUID announcementId
    ) {
        return announcementService.delete(
                announcementId
        );
    }

    @PatchMapping("/{announcement-id}/publish")
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_PUBLISH')")
    public Announcement publish(
            @PathVariable("announcement-id") UUID announcementId,
            @AuthenticationPrincipal UserDetailsImpl principal
    ) {
        return announcementService.publish(
                announcementId,
                principal.getUuid()
        );
    }

    @PatchMapping("/{announcement-id}/close")
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_CLOSE')")
    public Announcement close(
            @PathVariable("announcement-id") UUID announcementId,
            @AuthenticationPrincipal UserDetailsImpl principal
    ) {
        return announcementService.close(
                announcementId,
                principal.getUuid()
        );
    }
}
