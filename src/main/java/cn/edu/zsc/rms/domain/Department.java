package cn.edu.zsc.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author hsj
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Column(unique = true)
    private String number;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String abbreviation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeptType type;

    private UUID parentId;

    private LocalDateTime updateTime;

    @Id
    private UUID id;

    public enum DeptType{
        /**
         * 教学院系
         */
        COLLEGE,
        /**
         * 行政部门
         */
        ADMINISTRATION
    }
}
