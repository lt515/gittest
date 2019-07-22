package cn.edu.zsc.rms.repository;

import cn.edu.zsc.rms.domain.QRole;
import cn.edu.zsc.rms.domain.Role;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

/**
 * @author hsj
 */
public interface RoleRepository extends JpaRepository<Role, UUID>,
        QuerydslPredicateExecutor<Role> {

    /**
     * 为角色管理提供查询功能
     * @param name
     * @param type
     * @param pageable
     * @return
     */
    default Page<Role> query(String name, Role.RoleType type, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if(name != null){
            query.and(QRole.role.name.like(name));
        }
        if(type != null){
            query.and(QRole.role.type.eq(type));
        }
        return this.findAll(query, pageable);
    }

    /**
     * 用于判断是否有重复的ID
     *
     * @param name
     * @return
     */
    Boolean existsByName(String name);

    /**
     * 找到名称为name的Role
     *
     * @param name
     * @return
     */
    Optional<Role> findByName(String name);
}
