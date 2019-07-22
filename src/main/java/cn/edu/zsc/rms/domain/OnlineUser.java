package cn.edu.zsc.rms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author hsj
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {
    private String userName;
    private String userNumber;
    private String departmentName;
    private String departmentNumber;
    private String ip;
    private String browser;
    private String os;
    private LocalDateTime loginTime;
    @Id
    private String sessionId;
}
