package cn.edu.zsc.rms.exception.api.validation.field;

import cn.edu.zsc.rms.exception.api.validation.AbstractApiFieldValidationException;

/**
 * @author hsj
 */
public class UniquenessConstraintValidationException extends AbstractApiFieldValidationException {

    public UniquenessConstraintValidationException(String fieldName, Object fieldValue) {
        super(fieldName);
        errCode = "err.validation.UniquenessConstraintValidation";
        defaultMessage = "Field has been used";
        errArgs = new Object[]{fieldName, fieldValue};
    }
}
