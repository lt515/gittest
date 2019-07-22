package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.dto.AnnouncementTypeDTO;
import cn.edu.zsc.rms.domain.AnnouncementType;
import cn.edu.zsc.rms.service.AnnouncementTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/announcement-type")
public class AnnouncementTypeController {

    AnnouncementTypeService announcementTypeService;

    public AnnouncementTypeController(AnnouncementTypeService announcementTypeService) {
        this.announcementTypeService = announcementTypeService;
    }

    @GetMapping
    public Page<AnnouncementType> query(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID parentId,
            @PageableDefault(
                    sort = {"updateTime"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return announcementTypeService.query(name, parentId, pageable);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_TYPE_QUERY')")
    public List<AnnouncementType> list(
    ) {
        return announcementTypeService.list();
    }

    @GetMapping("/{announcement-type-id}")
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_TYPE_QUERY')")
    public AnnouncementType findById(
            @PathVariable("announcement-type-id") UUID announcementTypeId
    ) {
        return announcementTypeService.findById(announcementTypeId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_TYPE_CREATE')")
    public AnnouncementType create(
            @RequestBody AnnouncementTypeDTO announcementTypeDTO
    ) {
        return announcementTypeService.create(announcementTypeDTO.getName(), announcementTypeDTO.getParentId());
    }

    @DeleteMapping("/{announcement-type-id}")
    @PreAuthorize("hasAuthority('ANNOUNCEMENT_TYPE_DELETE')")
    public AnnouncementType delete(
            @PathVariable("announcement-type-id") UUID announcementTypeId
    ) {
        return announcementTypeService.delete(announcementTypeId);
    }


}
