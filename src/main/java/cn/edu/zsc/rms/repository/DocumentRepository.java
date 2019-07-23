package cn.edu.zsc.rms.repository;

import cn.edu.zsc.rms.domain.Document;
import cn.edu.zsc.rms.domain.QDocument;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID>,
        QuerydslPredicateExecutor<Document>{

    /**
     * 为科研文档提供查询功能
     */
    default Page<Document> query(String name,String type,Document.DocumentsStatus status,
                                 Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (name != null) {
            query.and(QDocument.document.name.like(name));
        }
        if (type != null) {
            query.and(QDocument.document.type.like(type));
        }
        if (status != null) {
            query.and(QDocument.document.status.eq(status));
        }
        return this.findAll(query, pageable);
    }




}
