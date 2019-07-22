package cn.edu.zsc.rms.spring.security;

import cn.edu.zsc.rms.spring.JsonExceptionUtil;
import cn.edu.zsc.rms.spring.SpringBeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author hsj
 */
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        ObjectMapper objectMapper = SpringBeanUtil.getBean(ObjectMapper.class);
        HttpServletResponse response = event.getResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> exceptionMap = JsonExceptionUtil.jsonExceptionResult(
                HttpStatus.UNAUTHORIZED,
                "This session has been expired (possibly due to multiple concurrent logins being attempted as the same user).",
                "/api/rest/**"
        );
        response.getWriter().print(objectMapper.writeValueAsString(exceptionMap));
        response.flushBuffer();
    }
}