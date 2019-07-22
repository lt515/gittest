package cn.edu.zsc.rms.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author hsj
 */
public enum Authority implements GrantedAuthority {

    /**
     * 查询用户信息列表所需权限
     */
    USER_QUERY,
    /**
     * 创建用户所需权限
     */
    USER_CREATE,
    /**
     * 更新用户信息所需权限
     */
    USER_UPDATE,

    /**
     * 查询角色信息列表所需权限
     */
    ROLE_QUERY,
    /**
     * 创建角色所需权限
     */
    ROLE_CREATE,
    /**
     * 更新角色信息所需权限
     */
    ROLE_UPDATE,

    ANNOUNCEMENT_QUERY,
    ANNOUNCEMENT_CREATE,
    ANNOUNCEMENT_UPDATE,
    ANNOUNCEMENT_DELETE,
    ANNOUNCEMENT_PUBLISH,
    ANNOUNCEMENT_CLOSE,

    ANNOUNCEMENT_TYPE_CREATE,
    ANNOUNCEMENT_TYPE_DELETE,

    MESSAGE_QUERY,
    MESSAGE_CREATE,
    MESSAGE_DELETE,

    JOB_QUERY,
    JOB_CREATE,
    JOB_UPDATE,
    JOB_DELETE,
    JOB_TRIGGER,

    JOB_LOG_QUERY,

    ONLINE_USER_QUERY,
    ONLINE_USER_DELETE;

    @Override
    public String getAuthority() {
        return name();
    }}
