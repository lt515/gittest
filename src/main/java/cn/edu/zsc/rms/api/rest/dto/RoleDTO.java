package cn.edu.zsc.rms.api.rest.dto;

import cn.edu.zsc.rms.domain.Authority;
import cn.edu.zsc.rms.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author hsj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private String name;
    private Set<Authority> authorities;
    private Role.DataScope dataScope;
    private String remark;
}
