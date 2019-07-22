package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.dto.RoleDTO;
import cn.edu.zsc.rms.domain.Role;
import cn.edu.zsc.rms.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/user/role")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_QUERY')")
    public Page<Role> query(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Role.RoleType type,
            @PageableDefault(
                    sort = {"updateTime"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return roleService.query(name, type, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    public Role create(
            @RequestBody RoleDTO roleDTO
    ) {
        return roleService.create(roleDTO.getName(), roleDTO.getAuthorities(), roleDTO.getDataScope(), roleDTO.getRemark());
    }

    @PatchMapping("/{role-id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public Role update(
            @PathVariable("role-id") UUID roleId,
            @RequestBody RoleDTO roleDTO
    ) {
        return roleService.update(roleId, roleDTO.getName(), roleDTO.getAuthorities(), roleDTO.getDataScope(), roleDTO.getRemark());
    }
}
