package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.Authority;
import cn.edu.zsc.rms.domain.Role;
import cn.edu.zsc.rms.exception.api.EntityNotExistException;
import cn.edu.zsc.rms.exception.api.validation.field.UniquenessConstraintValidationException;
import cn.edu.zsc.rms.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * @author hsj
 */
@Service
public class RoleService {
    private RoleRepository roleRepo;

    public RoleService(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role findById(UUID roleId) {
        return roleRepo.findById(roleId).orElseThrow(() ->
                new EntityNotExistException(Role.class)
        );
    }

    public Page<Role> query(String name, Role.RoleType type, Pageable pageable) {
        return roleRepo.query(name, type, pageable);
    }

    public Role create(String name, Set<Authority> authorities, Role.DataScope dataScope,String remark) {
        if (roleRepo.existsByName(name)) {
            throw new UniquenessConstraintValidationException("name", name);
        }
        Role role = new Role(name, Role.RoleType.CUSTOM, authorities, remark, dataScope, LocalDateTime.now(), UUID.randomUUID());
        return roleRepo.save(role);
    }

    public Role update(UUID roleId, String name, Set<Authority> authorities, Role.DataScope dataScope, String remark) {
        Role role = findById(roleId);
        if (!role.getName().equals(name) && roleRepo.existsByName(name)) {
            throw new UniquenessConstraintValidationException("name", name);
        }
        role.setName(name);
        role.setAuthorities(authorities);
        role.setRemark(remark);
        role.setDataScope(dataScope);
        role.setUpdateTime(LocalDateTime.now());
        return roleRepo.save(role);
    }
}
