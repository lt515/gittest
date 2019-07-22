package cn.edu.zsc.rms.repository;

import cn.edu.zsc.rms.domain.Message;
import cn.edu.zsc.rms.domain.QMessage;
import cn.edu.zsc.rms.domain.User;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

/**
 * @author hsj
 */
public interface MessageRepository extends JpaRepository<Message, UUID>
        , QuerydslPredicateExecutor<Message> {

    /**
     * 为系统消息提供查询功能
     * @param title
     * @param type
     * @param createBy
     * @param pushMethod
     * @param pageable
     * @return
     */
    default Page<Message> query(String title, Message.MessageType type,
                                Message.CreateBy createBy, Message.PushMethod pushMethod,
                                Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if(title != null){
            query.and(QMessage.message.title.like(title));
        }
        if(type != null){
            query.and(QMessage.message.type.eq(type));
        }
        if (createBy != null) {
            query.and(QMessage.message.createBy.eq(createBy));
        }
        if (pushMethod != null) {
            query.and(QMessage.message.pushMethods.any().eq(pushMethod));
        }
        return this.findAll(query, pageable);
    }
}
