package cn.edu.zsc.rms.spring;

import org.springframework.http.HttpStatus;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hsj
 */
public class JsonExceptionUtil {
    public static Map<String, Object> jsonExceptionResult(HttpStatus code, String message, String path){
        Map<String, Object> exceptionMap = new LinkedHashMap<>();
        exceptionMap.put("timestamp", Calendar.getInstance().getTime());
        exceptionMap.put("message", message);
        exceptionMap.put("path", path);
        exceptionMap.put("code", code.value());
        return exceptionMap;
    }
}
