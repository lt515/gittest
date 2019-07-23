package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.*;
import cn.edu.zsc.rms.exception.api.EntityNotExistException;
import cn.edu.zsc.rms.exception.api.validation.field.ObjectStatusValidationException;
import cn.edu.zsc.rms.repository.DocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {
    private UserService userService;
    private DocumentRepository documentRepo;

    DocumentService(UserService userService,DocumentRepository documentRepo){
        this.userService = userService;
        this.documentRepo = documentRepo;
    }
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

    public Document update(UUID documentId, String name, String type, String remark,
                           List<FileInfo> files) {
        Document document = findById(documentId);
        return documentRepo.save(
                new Document(
                        name,
                        type,
                        document.getStatus(),
                        document.getEditUser(),
                        LocalDateTime.now(),
                        document.getPublishUser(),
                        document.getPublishTime(),
                        remark,
                        files,
                        document.getDownloadTimes(),
                        documentId
                )
        );
    }

    public Document delete(UUID documentId){
        Document document = findById(documentId);
        documentRepo.deleteById(documentId);
        return document;
    }

    public Document publish(UUID documentId, UUID userId) {
        User user = userService.findById(userId);
        Document document = findById(documentId);
        checkDocumentStatus(document.getStatus(), Document.DocumentsStatus.UNPUBLISHED);
        document.setStatus(Document.DocumentsStatus.PUBLISHED);
        document.setPublishUser(user);
        document.setPublishTime(LocalDateTime.now());
        return documentRepo.save(document);
    }

    public Document close(UUID documnetId) {
        Document document = findById(documnetId);
        checkDocumentStatus(document.getStatus(), Document.DocumentsStatus.PUBLISHED);
        document.setStatus(Document.DocumentsStatus.UNPUBLISHED);
        return documentRepo.save(document);
    }


    public Document findById(UUID documentId) {
        return documentRepo.findById(documentId).orElseThrow(() -> new EntityNotExistException(User.class));
    }

    public void checkDocumentStatus(Document.DocumentsStatus currentStatus,
                                    Document.DocumentsStatus correctStatus){
        if (currentStatus != correctStatus) {
            throw new ObjectStatusValidationException("status", correctStatus,
                    correctStatus);
        }
    }

}

