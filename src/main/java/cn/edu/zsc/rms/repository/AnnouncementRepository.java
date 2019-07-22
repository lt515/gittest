package cn.edu.zsc.rms.repository;

import cn.edu.zsc.rms.domain.Announcement;
import cn.edu.zsc.rms.domain.QAnnouncement;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author hsj
 */
public interface AnnouncementRepository extends JpaRepository<Announcement, UUID>,
        QuerydslPredicateExecutor<Announcement> {
    /**
     * 为系统公告提供查询功能
     * @param title
     * @param announcementTypeId
     * @param publishTimeBegin
     * @param publishTimeEnd
     * @param pageable
     * @return
     */
    default Page<Announcement> query(String title, UUID announcementTypeId,
                                     LocalDateTime publishTimeBegin, LocalDateTime publishTimeEnd,
                                     Pageable pageable
    ) {
        BooleanBuilder query = new BooleanBuilder();
        if (title != null) {
            query.and(QAnnouncement.announcement.title.like(title));
        }
        if (announcementTypeId != null) {
            query.and(QAnnouncement.announcement.announcementType.id.eq(announcementTypeId));
        }
        query.and(QAnnouncement.announcement.publishTime.between(publishTimeBegin, publishTimeEnd)
                .or(QAnnouncement.announcement.publishTime.isNull()));

        return this.findAll(query, pageable);
    }
}
