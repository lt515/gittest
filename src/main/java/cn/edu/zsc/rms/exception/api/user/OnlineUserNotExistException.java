package cn.edu.zsc.rms.exception.api.user;

import cn.edu.zsc.rms.exception.api.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author hsj
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OnlineUserNotExistException extends ApiException {
    public OnlineUserNotExistException() {
        super("User has been offline");
    }
}
