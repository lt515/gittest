package cn.edu.zsc.rms.spring.security;

import cn.edu.zsc.rms.domain.User;
import cn.edu.zsc.rms.service.OnlineUserService;
import cn.edu.zsc.rms.service.UserService;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hsj
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private OnlineUserService onlineUserService;
    private UserService userService;
    private SessionRegistry sessionRegistry;

    public CustomAuthenticationSuccessHandler(
            OnlineUserService onlineUserService,
            UserService userService,
            SessionRegistry sessionRegistry
    ) {
        this.onlineUserService = onlineUserService;
        this.userService = userService;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sessionId = request.getRequestedSessionId();
        String remoteAddr = request.getRemoteAddr();
        User user = userService.findById(((UserDetailsImpl) principal).getUuid());
        removeSameSession(user, sessionId);
        String agent = request.getHeader("User-Agent");
        UserAgent userAgent = new UserAgent(agent);
        onlineUserService.create(
                sessionId,
                user.getName(),
                user.getNumber(),
                user.getDepartment().getName(),
                user.getDepartment().getNumber(),
                remoteAddr,
                userAgent.getBrowser().getName(),
                userAgent.getOperatingSystem().getName(),
                LocalDateTime.now()
        );
    }

    private void removeSameSession(User user, String newSessionId) {
        sessionRegistry.getAllPrincipals().stream()
                .map(principal -> (UserDetailsImpl) principal)
                .filter(userDetails -> userDetails.getUsername().equals(user.getNumber()))
                .map(principal -> sessionRegistry.getAllSessions(principal, false))
                .flatMap(List::stream)
                .filter(sessionInformation -> !sessionInformation.getSessionId().equals(newSessionId))
                .forEach(SessionInformation::expireNow);
    }
}
