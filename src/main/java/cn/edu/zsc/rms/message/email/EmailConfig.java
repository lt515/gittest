package cn.edu.zsc.rms.message.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hsj
 */
@Component
@ConfigurationProperties("spring.mail")
@Data
public class EmailConfig {
    private String host;
    private String username;
    private String password;
    private String protocol;
}
