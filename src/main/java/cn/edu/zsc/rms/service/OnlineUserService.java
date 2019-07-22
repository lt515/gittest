package cn.edu.zsc.rms.service;

import cn.edu.zsc.rms.domain.OnlineUser;
import cn.edu.zsc.rms.exception.api.user.OnlineUserNotExistException;
import cn.edu.zsc.rms.repository.OnlineUserRepository;
import cn.edu.zsc.rms.spring.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hsj
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OnlineUserService {
    private OnlineUserRepository onlineUserRepo;
    private SessionRegistry sessionRegistry;

    public OnlineUserService(
            OnlineUserRepository onlineUserRepo
            , SessionRegistry sessionRegistry
    ) {
        this.onlineUserRepo = onlineUserRepo;
        this.sessionRegistry = sessionRegistry;
    }

    public Page<OnlineUser> query(String userName, String userNumber,
                                  String departmentNumber, String ip,
                                  LocalDateTime loginTimeBegin, LocalDateTime loginTimeEnd,
                                  Pageable pageable) {
        removeNotOnlineUser();
        return onlineUserRepo.query(userName, userNumber, departmentNumber, ip, loginTimeBegin, loginTimeEnd, pageable);
    }

    public OnlineUser forceLogout(String userNumber) {
        removeNotOnlineUser();
        OnlineUser onlineUser = findByUserNumber(userNumber);
        sessionRegistry.getAllPrincipals().stream()
                .map(principal -> (UserDetailsImpl) principal)
                .filter(userDetails -> userDetails.getUsername().equals(userNumber))
                .map((principal) -> sessionRegistry.getAllSessions(principal, false))
                .flatMap(List::stream)
                .forEach(SessionInformation::expireNow);
        return onlineUser;
    }

    public OnlineUser create(String sessionId, String userName, String userNumber, String departmentName,
                             String departmentNumber, String ip, String browser, String os,
                             LocalDateTime loginTime) {
        OnlineUser onlineUser = new OnlineUser(
                userName, userNumber, departmentName, departmentNumber,
                ip, browser, os, loginTime, sessionId
        );
        return onlineUserRepo.save(onlineUser);
    }

    private void removeNotOnlineUser() {
        List<String> sessionIds = new LinkedList<>();
        sessionRegistry.getAllPrincipals().stream()
                .map((principal) -> sessionRegistry.getAllSessions(principal, false))
                .flatMap(List::stream)
                .map(SessionInformation::getSessionId)
                .forEach(sessionIds::add);
        List<String> removeSessionIds = findAllSessionId().stream().filter((sessionId) -> !sessionIds.contains(sessionId)).collect(Collectors.toList());
        onlineUserRepo.deleteAllBySessionIdIn(removeSessionIds);
    }

    private List<String> findAllSessionId() {
        return onlineUserRepo.findAll().stream().map(OnlineUser::getSessionId).collect(Collectors.toList());
    }

    public OnlineUser findByUserNumber(String userNumber) {
        return onlineUserRepo.findByUserNumber(userNumber).orElseThrow(OnlineUserNotExistException::new);
    }
}
