package cn.edu.zsc.rms.domain;

import cn.edu.zsc.rms.excel.model.ExcelUserModel;
import cn.edu.zsc.rms.spring.security.UserDetailsImpl;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author hsj
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = "password")
public class User {
    @Column(nullable = false,unique = true)
    private String number;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String phone;

    @Column(unique = true)
    private String email;

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private UserType type;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToOne
    private Department department;

    private LocalDateTime updateTime;

    @Id
    private UUID id;

    public enum UserType {
        /**
         * 校内用户
         */
        STAFF,
        /**
         * 校外用户
         */
        NON_STAFF
    }

    public enum UserStatus {
        /**
         * 可用
         */
        ENABLED(true),
        /**
         * 禁用
         */
        DISABLED(false);

        private Boolean enabled;

        UserStatus(Boolean enabled) {
            this.enabled = enabled;
        }

        public Boolean isEnabled() {
            return enabled;
        }
    }

    public UserDetails toUserDetail() {
        Set<Authority> authorities = this.roles.stream()
                .map(Role::getAuthorities)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        return new UserDetailsImpl(this.getId(), this.getNumber(), this.getPassword(), authorities, this.getStatus().isEnabled());
    }

    public ExcelUserModel toExcelModel(){
        return new ExcelUserModel(
                getNumber(),
                getName(),
                getPhone(),
                getEmail(),
                department.getNumber()
        );
    }
}

