package cn.edu.zsc.rms.api.rest.controller;

import cn.edu.zsc.rms.api.rest.dto.UserDTO;
import cn.edu.zsc.rms.domain.User;
import cn.edu.zsc.rms.service.UserService;
import cn.edu.zsc.rms.spring.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("me")
    public Map<String, Object> me(
            @AuthenticationPrincipal Object principal,
            CsrfToken csrfToken
    ) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("user", principal);
        map.put("csrf", csrfToken);
        return map;
    }

    @GetMapping("me/info")
    public User getUserSelfInfo(
            @AuthenticationPrincipal UserDetailsImpl principal
    ) {
        return userService.findById(principal.getUuid());
    }

    @PatchMapping("me")
    public User updateUserSelfInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserDTO.UpdateUserSelfDTO updateUserSelfDTO
    ) {
        return userService.update(userDetails.getUuid(),
                updateUserSelfDTO.getName(),
                updateUserSelfDTO.getPhone(),
                updateUserSelfDTO.getEmail(),
                updateUserSelfDTO.getAvatarUrl(),
                null,
                null,
                null
        );
    }

    @PatchMapping("me/password")
    public User setUserSelfPassword(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @RequestParam(required = false) String oldPassword,
            @RequestParam(required = false) String password
    ) {
        return userService.setPassword(principal.getUuid(), oldPassword, password);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_QUERY')")
    public Page<User> query(
            @RequestParam(required = false) String number,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Set<UUID> roles,
            @RequestParam(required = false) UUID departmentId,
            @RequestParam(required = false) User.UserType type,
            @RequestParam(required = false) User.UserStatus status,
            @PageableDefault(
                    sort = {"updateTime"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable

    ) {
        return userService.query(number, name, phone, email, roles, departmentId, type, status, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public User create(
            @RequestBody UserDTO.CreateDTO createDTO
    ) {
        return userService.create(createDTO.getNumber(), createDTO.getPassword(), createDTO.getName(),
                createDTO.getPhone(), createDTO.getEmail(), createDTO.getAvatarUrl(), createDTO.getRoles(),
                createDTO.getDepartmentId(), createDTO.getStatus()
        );
    }

    @PatchMapping("{user-id}/password")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public User setPassword(
            @PathVariable("user-id") UUID userId,
            @RequestParam(required = false) String password
    ) {
        return userService.setPassword(userId, password);
    }

    @PatchMapping("{user-id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public User update(
            @PathVariable("user-id") UUID userId,
            @RequestBody UserDTO.UpdateDTO updateDTO
    ) {
        return userService.update(userId, updateDTO.getName(),
                updateDTO.getPhone(), updateDTO.getEmail(), updateDTO.getAvatarUrl(), updateDTO.getRoles(),
                updateDTO.getDepartmentId(), updateDTO.getStatus()
        );
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public void excelImport(
        MultipartFile file
    ) {
        try {
            InputStream in = file.getInputStream();
            userService.excelImport(in);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("excel")
    @PreAuthorize("hasAuthority('USER_QUERY')")
    public ResponseEntity<StreamingResponseBody> excelExport(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Set<UUID> roles,
            @RequestParam(required = false) UUID departmentId,
            @RequestParam(required = false) User.UserType type,
            @RequestParam(required = false) User.UserStatus status
    ){
        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=user.xlsx"
        ).body(
                (out) -> userService.excelExport(out, id, realName, phone, email, roles, departmentId, type, status)
        );
    }
}
