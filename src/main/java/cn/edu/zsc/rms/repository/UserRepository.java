package cn.edu.zsc.rms.repository;

import cn.edu.zsc.rms.domain.QUser;
import cn.edu.zsc.rms.domain.User;
import cn.edu.zsc.rms.spring.security.JpaUserDetailsServiceImpl;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * @author hsj
 */
public interface UserRepository extends JpaRepository<User, UUID>,
        QuerydslPredicateExecutor<User> {

    /**
     * 为用户管理提供分页查询功能
     *
     * @param number
     * @param name
     * @param phone
     * @param email
     * @param roles
     * @param departmentId
     * @param type
     * @param status
     * @param pageable
     * @return
     */
    default Page<User> query(String number, String name, String phone, String email, Set<UUID> roles,
                             UUID departmentId, User.UserType type, User.UserStatus status, Pageable pageable
    ) {
        BooleanBuilder query = new BooleanBuilder();
        if (number != null) {
            query.and(QUser.user.number.like(number));
        }
        if (name != null) {
            query.and(QUser.user.name.like(name));
        }
        if (phone != null) {
            query.and(QUser.user.phone.like(phone));
        }
        if (email != null) {
            query.and(QUser.user.email.like(email));
        }
        if (roles != null) {
            query.and(QUser.user.roles.any().id.in(roles));
        }
        if (departmentId != null) {
            query.and(QUser.user.department.id.eq(departmentId));
        }
        if (type != null) {
            query.and(QUser.user.type.eq(type));
        }
        if (status != null) {
            query.and(QUser.user.status.eq(status));
        }
        if (pageable == null) {
            return new PageImpl<>(
                    Lists.newArrayList(this.findAll(query, Sort.by(Sort.Direction.DESC, "updateTime")))
            );
        }
        return this.findAll(query, pageable);
    }

    /**
     * 用于判断是否有重复的ID
     *
     * @param number
     * @return
     */
    Boolean existsByNumber(String number);

    /**
     * 用于判断是否有重复的email
     *
     * @param email
     * @return
     */
    Boolean existsByEmail(String email);

    /**
     * 用于判断是否有重复的phone
     *
     * @param phone
     * @return
     */
    Boolean existsByPhone(String phone);

    /**
     * 通过ID查找用户，用于{@link JpaUserDetailsServiceImpl}
     *
     * @param number
     * @return
     */
    Optional<User> findByNumber(String number);

}
