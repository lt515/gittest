package cn.edu.zsc.rms.repository;

import cn.edu.zsc.rms.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

/**
 * @author hsj
 */
public interface DepartmentRepository extends JpaRepository<Department, UUID>, QuerydslPredicateExecutor<Department> {

    /**
     * 通过部门编号查询
     * @param number
     * @return
     */
    Optional<Department> findByNumber(String number);
}
