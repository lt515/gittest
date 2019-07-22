package cn.edu.zsc.rms.domain;

import org.apache.catalina.util.StandardSessionIdGenerator;

import java.time.LocalDateTime;

/**
 * @author hsj
 */
public class OnlineUserBuilder {
    private OnlineUser onlineUser;

    public OnlineUserBuilder(String userName, String userNumber, String departmentName, String departmentNumber) {
        onlineUser = new OnlineUser(
                userName,
                userNumber,
                departmentName,
                departmentNumber,
                "192.168.0.1",
                "chrome",
                "windows",
                LocalDateTime.now(),
                new StandardSessionIdGenerator().generateSessionId()
        );
    }

    public OnlineUserBuilder userName(String userName) {
        onlineUser.setUserName(userName);
        return this;
    }

    public OnlineUserBuilder userNumber(String userNumber) {
        onlineUser.setUserNumber(userNumber);
        return this;
    }

    public OnlineUserBuilder departmentNumber(String departmentNumber) {
        onlineUser.setDepartmentNumber(departmentNumber);
        return this;
    }

    public OnlineUserBuilder departmentName(String departmentName) {
        onlineUser.setDepartmentName(departmentName);
        return this;
    }

    public OnlineUserBuilder ip(String ip) {
        onlineUser.setIp(ip);
        return this;
    }

    public OnlineUserBuilder browser(String browser) {
        onlineUser.setBrowser(browser);
        return this;
    }

    public OnlineUserBuilder os(String os) {
        onlineUser.setOs(os);
        return this;
    }

    public OnlineUserBuilder loginTime(LocalDateTime loginTime) {
        onlineUser.setLoginTime(loginTime);
        return this;
    }

    public OnlineUserBuilder sessionId(String sessionId) {
        onlineUser.setSessionId(sessionId);
        return this;
    }

    public OnlineUser build() {
        return onlineUser;
    }
}
