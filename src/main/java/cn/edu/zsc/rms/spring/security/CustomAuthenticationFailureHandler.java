package cn.edu.zsc.rms.spring.security;

import cn.edu.zsc.rms.spring.JsonExceptionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hsj
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private ObjectMapper objectMapper;

    public CustomAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> exceptionMap = JsonExceptionUtil.jsonExceptionResult(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                "/api/rest/user/login"
        );
        response.getWriter().print(objectMapper.writeValueAsString(exceptionMap));
        response.flushBuffer();
    }
}
