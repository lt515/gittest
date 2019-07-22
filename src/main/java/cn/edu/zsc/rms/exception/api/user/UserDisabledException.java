package cn.edu.zsc.rms.exception.api.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author hsj
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserDisabledException extends DisabledException {

    public UserDisabledException() {
        super("user is disabled");
    }

}
