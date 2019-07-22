package cn.edu.zsc.rms.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * @author hsj
 */
public class RoleBuilder {
    private Role role;

    public RoleBuilder() {
        Set<Authority> authorities = new LinkedHashSet<>(Arrays.stream(Authority.values()).collect(Collectors.toSet()));
        role = new Role(randomAlphabetic(5),
                Role.RoleType.CUSTOM,
                authorities,
                randomAlphabetic(20),
                Role.DataScope.SCHOOL,
                LocalDateTime.now(),
                UUID.randomUUID());
    }

    public RoleBuilder name(String name) {
        role.setName(name);
        return this;
    }

    public RoleBuilder authorities(Set<Authority> authorities) {
        role.setAuthorities(authorities);
        return this;
    }

    public RoleBuilder remark(String remark) {
        role.setRemark(remark);
        return this;
    }

    public RoleBuilder id(UUID id) {
        role.setId(id);
        return this;
    }

    public RoleBuilder roleType(Role.RoleType roleType) {
        role.setType(roleType);
        return this;
    }

    public RoleBuilder dataScope(Role.DataScope dataScope) {
        role.setDataScope(dataScope);
        return this;
    }

    public Role build() {
        return role;
    }
}
