package cn.edu.zsc.rms.exception.api.validation.field;

import cn.edu.zsc.rms.exception.api.validation.AbstractApiFieldValidationException;

/**
 * @author hsj
 */
public class AssociationEntityDoseNotExistException extends AbstractApiFieldValidationException {

    public AssociationEntityDoseNotExistException(String fieldName, Object identifier) {
        super(fieldName);
        errCode = "err.validation.TargetObjectDoseNotExist";
        defaultMessage = "The associated entity does not exist";
        errArgs = new Object[]{fieldName, identifier};
    }
}