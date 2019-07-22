package cn.edu.zsc.rms.exception.api.validation.field;

import cn.edu.zsc.rms.exception.api.validation.AbstractApiFieldValidationException;

/**
 * @author hsj
 */
public class ImmutableConstraintValidationException extends AbstractApiFieldValidationException {
    public ImmutableConstraintValidationException(String fieldName) {
        super(fieldName);
        errCode = "err.validation.ImmutableConstraintValidation";
        defaultMessage = "Field cannot be updated";
        errArgs = new Object[]{fieldName};
    }
}
