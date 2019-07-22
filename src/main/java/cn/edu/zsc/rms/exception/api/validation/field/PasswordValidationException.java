package cn.edu.zsc.rms.exception.api.validation.field;

import cn.edu.zsc.rms.exception.api.validation.AbstractApiFieldValidationException;

/**
 * @author hsj
 */
public class PasswordValidationException extends AbstractApiFieldValidationException {

    public PasswordValidationException(String fieldName, Object errValue) {
        super(fieldName);
        errCode = "err.validation.PasswordValidation";
        defaultMessage = "password can not be null and empty";
        errArgs = new Object[]{fieldName, errValue};
    }
}
