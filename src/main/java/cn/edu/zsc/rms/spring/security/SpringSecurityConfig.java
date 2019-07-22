package cn.edu.zsc.rms.spring.security;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author hsj
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private SessionRegistry sessionRegistry;

    public SpringSecurityConfig(UserDetailsService userDetailsService,
                                CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
                                CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                                SessionRegistry sessionRegistry) {
        this.userDetailsService = userDetailsService;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/rest/user/me")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/rest/user/login")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll()
                .and()
                .logout().logoutUrl("/api/rest/user/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                })
                .and()
                .rememberMe()
                .userDetailsService(userDetailsService)
                .and()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**")
                )
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry)
                .expiredSessionStrategy(new CustomSessionInformationExpiredStrategy());
    }
}
