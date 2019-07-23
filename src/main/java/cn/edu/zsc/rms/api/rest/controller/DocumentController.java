package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.dto.AnnouncementDTO;
import cn.edu.zsc.rms.api.rest.dto.DocumentDTO;
import cn.edu.zsc.rms.domain.Announcement;
import cn.edu.zsc.rms.domain.Document;
import cn.edu.zsc.rms.service.DocumentService;
import cn.edu.zsc.rms.spring.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/rest/document")
public class DocumentController {

    private DocumentService documentService;

    public DocumentController(DocumentService documentService){
        this.documentService = documentService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('DOCUMENT_QUERY')")
    public Page<Document> query(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Document.DocumentsStatus status,
            @PageableDefault(
                    sort = { "publishTime"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return documentService.query(name, type, status, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('DOCUMENT_CREATE')")
    public Document create(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @RequestBody DocumentDTO documentDTO
    ) {
        return documentService.create(
                principal.getUuid(),
                documentDTO.getName(),
                documentDTO.getType(),
                documentDTO.getRemark(),
                documentDTO.getFiles()
        );
    }
}
