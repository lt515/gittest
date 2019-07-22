package cn.edu.zsc.rms.spring.i18n;

import cn.edu.zsc.rms.spring.SpringBeanUtil;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author hsj
 */
public class MessageUtil {

    public static String getMessage(String code, Object[] args){
        return getMessage(code, args, "");
    }

    public static String getMessage(String code, Object[] args, String defaultMsg){
        MessageSource messageSource = SpringBeanUtil.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, defaultMsg, Locale.SIMPLIFIED_CHINESE);
    }
}
