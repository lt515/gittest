package cn.edu.zsc.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author hsj
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities;

    private String remark;

    @Enumerated(EnumType.STRING)
    private DataScope dataScope;

    private LocalDateTime updateTime;

    @Id
    private UUID id;

    public enum RoleType {
        /**
         * 内置角色
         */
        BUILD_IN,
        /**
         * 自定义角色
         */
        CUSTOM
    }

    public enum DataScope {
        /**
         * 全校
         */
        SCHOOL,
        /**
         * 二级学院
         */
        COLLEGE,
        /**
         * 自己
         */
        SELF
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

