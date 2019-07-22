package cn.edu.zsc.rms.domain;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.*;

/**
 * @author hsj
 */
public class UserBuilder {

    private User user;

    public UserBuilder(Set<Role> roles, Department department) {
        user = new User(randomNumeric(7),
                randomNumeric(10),
                roles,
                randomAlphabetic(5),
                "139" + randomAlphanumeric(8),
                randomAlphanumeric(8) + "@" + randomAlphanumeric(6) + ".com",
                "/api/rest/file/download/" + randomNumeric(32),
                User.UserType.STAFF,
                User.UserStatus.ENABLED,
                department,
                LocalDateTime.now(),
                UUID.randomUUID());
    }

    public UserBuilder number(String number) {
        user.setNumber(number);
        return this;
    }

    public UserBuilder password(String password) {
        user.setPassword(password);
        return this;
    }

    public UserBuilder name(String name){
        user.setName(name);
        return this;
    }
    public UserBuilder phone(String phone){
        user.setPhone(phone);
        return this;
    }
    public UserBuilder email(String email){
        user.setEmail(email);
        return this;
    }
    public UserBuilder avatarUrl(String avatarUrl){
        user.setAvatarUrl(avatarUrl);
        return this;
    }

    public UserBuilder type(User.UserType type) {
        user.setType(type);
        return this;
    }

    public UserBuilder status(User.UserStatus status) {
        user.setStatus(status);
        return this;
    }

    public UserBuilder id(UUID id) {
        user.setId(id);
        return this;
    }

    public User build() {
        return user;
    }
}
