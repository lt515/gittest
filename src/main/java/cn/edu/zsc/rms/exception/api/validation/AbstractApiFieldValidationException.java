package cn.edu.zsc.rms.exception.api.validation;

/**
 * @author hsj
 */
abstract public class AbstractApiFieldValidationException extends AbstractApiValidationException {
    protected String fieldName;

    public AbstractApiFieldValidationException(String errCode, Object[] errArgs, String defaultMessage, String fieldName) {
        super(errCode, errArgs, defaultMessage);
        this.fieldName = fieldName;
    }

    public AbstractApiFieldValidationException(String errCode, Object[] errArgs, String fieldName) {
        super(errCode, errArgs);
        this.fieldName = fieldName;
    }

    public AbstractApiFieldValidationException(String errCode, String fieldName) {
        super(errCode);
        this.fieldName = fieldName;
    }

    public AbstractApiFieldValidationException(String fieldName) {
        this.fieldName = fieldName;
    }
}

