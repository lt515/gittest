package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.domain.OnlineUser;
import cn.edu.zsc.rms.service.OnlineUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/online-user")
public class OnlineUserController {

    private OnlineUserService onlineUserService;

    public OnlineUserController(OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ONLINE_USER_QUERY')")
    public Page<OnlineUser> query(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String userNumber,
            @RequestParam(required = false) String departmentNumber,
            @RequestParam(required = false) String ip,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime loginTimeBegin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime loginTimeEnd,
            @PageableDefault(
                    sort = {"loginTime"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return onlineUserService.query(userName, userNumber, departmentNumber, ip, loginTimeBegin, loginTimeEnd, pageable);
    }

    @DeleteMapping("{user-number}")
    @PreAuthorize("hasAuthority('ONLINE_USER_DELETE')")
    public OnlineUser forceLogout(
            @PathVariable("user-number") String userNumber
    ){
        return onlineUserService.forceLogout(userNumber);
    }

}
