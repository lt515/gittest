package cn.edu.zsc.rms.api.rest;

import cn.edu.zsc.rms.repository.OnlineUserRepository;
import cn.edu.zsc.rms.service.OnlineUserService;
import cn.edu.zsc.rms.spring.security.CustomAuthenticationFailureHandler;
import cn.edu.zsc.rms.spring.security.CustomAuthenticationSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author hsj
 */
@RunWith(SpringRunner.class)
@AutoConfigureRestDocs
@Import({RestAutoDocConfig.class,})
abstract public class MockMvcConfigBase {
    @MockBean
    protected SessionRegistry sessionRegistry;

    @MockBean
    protected CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @MockBean
    protected CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
