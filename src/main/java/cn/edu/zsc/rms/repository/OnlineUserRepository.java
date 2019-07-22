package cn.edu.zsc.rms.repository;

import cn.edu.zsc.rms.domain.OnlineUser;
import cn.edu.zsc.rms.domain.QOnlineUser;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author hsj
 */
public interface OnlineUserRepository extends JpaRepository<OnlineUser, String>,
        QuerydslPredicateExecutor<OnlineUser> {
    /**
     * 为在线用户提供查询功能
     *
     * @param userNumber
     * @param userName
     * @param departmentNumber
     * @param ip
     * @param loginTimeBegin
     * @param loginTimeEnd
     * @param pageable
     * @return
     */
    default Page<OnlineUser> query(String userName, String userNumber,
                                   String departmentNumber, String ip,
                                   LocalDateTime loginTimeBegin, LocalDateTime loginTimeEnd,
                                   Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (userName != null) {
            query = query.and(QOnlineUser.onlineUser.userName.like(userName));
        }
        if (userNumber != null) {
            query = query.and(QOnlineUser.onlineUser.userNumber.like(userNumber));
        }
        if (departmentNumber != null) {
            query = query.and(QOnlineUser.onlineUser.departmentNumber.eq(departmentNumber));
        }
        if (ip != null) {
            query = query.and(QOnlineUser.onlineUser.ip.like(ip));
        }
        if (loginTimeBegin != null || loginTimeEnd != null) {
            query = query.and(QOnlineUser.onlineUser.loginTime.between(loginTimeBegin, loginTimeEnd));
        }

        return findAll(query, pageable);
    }

    /**
     * 批量删除指定的OnlineUser
     * @param sessionId
     */
    void deleteAllBySessionIdIn(List<String> sessionId);

    /**
     * 根据用户名获取在线用户信息
     * @param userNumber
     * @return
     */
    Optional<OnlineUser> findByUserNumber(String userNumber);
}
