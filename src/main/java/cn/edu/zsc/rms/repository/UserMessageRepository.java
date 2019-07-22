package cn.edu.zsc.rms.repository;

import cn.edu.zsc.rms.domain.Message;
import cn.edu.zsc.rms.domain.QUserMessage;
import cn.edu.zsc.rms.domain.UserMessage;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;

/**
 * @author hsj
 */

public interface UserMessageRepository extends JpaRepository<UserMessage, UserMessage.UserMessageId>,
        QuerydslPredicateExecutor<UserMessage> {
    /**
     * 为系统提供查询功能
     * @param title
     * @param type
     * @param userNameOrNumber
     * @param createBy
     * @param pushMethod
     * @param haveRead
     * @param sendTimeBegin
     * @param sendTimeEnd
     * @param pageable
     * @return
     */
    default Page<UserMessage> query(String title, Message.MessageType type,
                                    String userNameOrNumber, Message.CreateBy createBy,
                                    Message.PushMethod pushMethod, Boolean haveRead,
                                    LocalDateTime sendTimeBegin, LocalDateTime sendTimeEnd,
                                    Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (title != null) {
            query.and(QUserMessage.userMessage.message.title.like(title));
        }
        if (type != null) {
            query.and(QUserMessage.userMessage.message.type.eq(type));
        }
        if (userNameOrNumber != null) {
            query.and(
                    QUserMessage.userMessage.user.name.like(userNameOrNumber)
                    .or(QUserMessage.userMessage.user.number.like(userNameOrNumber))
            );
        }
        if (createBy != null) {
            query.and(QUserMessage.userMessage.message.createBy.eq(createBy));
        }
        if (pushMethod != null) {
            query.and(QUserMessage.userMessage.message.pushMethods.any().eq(pushMethod));
        }
        if (haveRead != null) {
            query.and(QUserMessage.userMessage.haveRead.eq(haveRead));
        }
        if(sendTimeBegin != null || sendTimeEnd != null){
            query.and(QUserMessage.userMessage.message.sendTime.between(sendTimeBegin, sendTimeEnd));
        }
        return findAll(query, pageable);
    }

}
