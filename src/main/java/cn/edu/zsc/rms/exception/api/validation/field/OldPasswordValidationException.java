package cn.edu.zsc.rms.exception.api.validation.field;

import cn.edu.zsc.rms.exception.api.validation.AbstractApiFieldValidationException;

/**
 * @author hsj
 */
public class OldPasswordValidationException extends AbstractApiFieldValidationException {
    public OldPasswordValidationException(String fieldName) {
        super(fieldName);
        errCode = "err.validation.OldPasswordValidation";
        defaultMessage = "Old password error";
        errArgs = new Object[]{fieldName};
    }
}
