package cn.edu.zsc.rms.exception.api.validation.field;

import cn.edu.zsc.rms.exception.api.validation.AbstractApiFieldValidationException;

/**
 * @author hsj
 */
public class MessagePushValidationException extends AbstractApiFieldValidationException {
    public MessagePushValidationException(String fieldName) {
        super(fieldName);
        errCode = "err.validation.MessagePushValidation";
        defaultMessage = "The message has been pushed via SMS, Email, etc.";
        errArgs = new Object[]{fieldName};
    }
}
