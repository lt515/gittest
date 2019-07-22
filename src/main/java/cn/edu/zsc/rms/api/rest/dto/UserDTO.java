package cn.edu.zsc.rms.api.rest.dto;

import cn.edu.zsc.rms.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

/**
 * @author hsj
 */
@ToString
public class UserDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class CreateDTO {
        private String number;
        private String password;
        private String name;
        private String phone;
        private String email;
        private String avatarUrl;
        private User.UserStatus status;
        private Set<UUID> roles;
        private UUID departmentId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class UpdateDTO {
        private String name;
        private String phone;
        private String email;
        private String avatarUrl;
        private User.UserStatus status;
        private Set<UUID> roles;
        private UUID departmentId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class UpdateUserSelfDTO {
        private String name;
        private String phone;
        private String email;
        private String avatarUrl;
    }
}
