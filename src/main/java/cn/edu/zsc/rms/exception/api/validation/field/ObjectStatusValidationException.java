package cn.edu.zsc.rms.exception.api.validation.field;

import cn.edu.zsc.rms.exception.api.validation.AbstractApiFieldValidationException;

/**
 * @author hsj
 */
public class ObjectStatusValidationException extends AbstractApiFieldValidationException {

    public ObjectStatusValidationException(String fieldName, Object currentStatus, Object correctStatus) {
        super(fieldName);
        errCode = "err.validation.ObjectStatusValidation";
        defaultMessage = "The current status does not allow this operation";
        errArgs = new Object[]{fieldName, currentStatus, correctStatus};
    }
}
