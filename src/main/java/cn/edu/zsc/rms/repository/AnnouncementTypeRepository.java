package cn.edu.zsc.rms.repository;

import cn.edu.zsc.rms.domain.AnnouncementType;
import cn.edu.zsc.rms.domain.QAnnouncementType;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

/**
 * @author hsj
 */
public interface AnnouncementTypeRepository extends JpaRepository<AnnouncementType, UUID>
        , QuerydslPredicateExecutor<AnnouncementType> {

    /**
     * 为公告类型提供查询功能
     * @param name
     * @param parentId
     * @param pageable
     * @return
     */
    default Page<AnnouncementType> query(String name, UUID parentId, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (name != null) {
            query.and(QAnnouncementType.announcementType.name.like(name));
        }
        if (parentId != null) {
            query.and(QAnnouncementType.announcementType.parentId.eq(parentId));
        }
        return this.findAll(query, pageable);
    }
}
