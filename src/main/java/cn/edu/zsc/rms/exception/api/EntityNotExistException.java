package cn.edu.zsc.rms.exception.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author hsj
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotExistException extends ApiException {

    public EntityNotExistException(Class<? extends Object> entity) {
        super(String.format("target entity %s doesn't exists", entity.getSimpleName()));
    }
}