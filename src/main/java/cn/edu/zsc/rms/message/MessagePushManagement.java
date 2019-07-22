package cn.edu.zsc.rms.message;

import cn.edu.zsc.rms.domain.Message;
import cn.edu.zsc.rms.domain.User;
import cn.edu.zsc.rms.spring.SpringBeanUtil;
import com.mysema.commons.lang.Assert;

import java.util.List;

/**
 * @author hsj
 */
public class MessagePushManagement {
    public static void pushMessage(User user, Message message) {
        message.getPushMethods().forEach(pushMethod ->
                {
                    MessagePush messagePush = SpringBeanUtil.getBean(pushMethod.getMessagePushClass());
                    messagePush.sendMessage(user, message);
                }
        );
    }

    public static void pushMessage(List<User> users, Message message) {
        message.getPushMethods().forEach(pushMethod ->
                {
                    MessagePush messagePush = SpringBeanUtil.getBean(pushMethod.getMessagePushClass());
                    users.forEach(user -> messagePush.sendMessage(user, message));
                }
        );
    }

}
