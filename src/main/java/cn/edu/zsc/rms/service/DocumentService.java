package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.*;
import cn.edu.zsc.rms.repository.DocumentRepository;
import cn.edu.zsc.rms.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DocumentService {
    private UserService userService;
    private DocumentRepository documentRepo;

    public Page<Document> query(String name, String type, Document.DocumentsStatus status,
                                Pageable pageable) {
        return documentRepo.query(name, type, status, pageable);
    }

    public Document create(UUID userId, String name, String type, String remark,
                           List<FileInfo> files) {
        User user = userService.findById(userId);
        return documentRepo.save(
                new Document(
                        name,
                        type,
                        Document.DocumentsStatus.UNPUBLISHED,
                        user,
                        LocalDateTime.now(),
                        null,
                        null,
                        remark,
                        files,
                        0,
                        UUID.randomUUID()

                )
        );
    }

}
