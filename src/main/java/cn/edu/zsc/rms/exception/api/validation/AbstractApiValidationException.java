package cn.edu.zsc.rms.exception.api.validation;

import cn.edu.zsc.rms.exception.api.ApiException;
import cn.edu.zsc.rms.spring.i18n.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author hsj
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
abstract public class AbstractApiValidationException extends ApiException {
    protected String errCode;
    protected Object[] errArgs;
    protected String defaultMessage;

    public AbstractApiValidationException(String errCode, Object[] errArgs, String defaultMessage) {
        this.errCode = errCode;
        this.defaultMessage = defaultMessage;
        this.errArgs = errArgs;
    }

    public AbstractApiValidationException(String errCode, Object[] errArgs) {
        this(errCode, errArgs, null);
    }

    public AbstractApiValidationException(String errCode) {
        this(errCode, null, null);
    }

    @Override
    public String getMessage() {
        if(defaultMessage == null) {
            return MessageUtil.getMessage(errCode, errArgs);
        }
        return MessageUtil.getMessage(errCode, errArgs, defaultMessage);
    }

    public AbstractApiValidationException() {
    }
}
