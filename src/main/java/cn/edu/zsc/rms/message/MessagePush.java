package cn.edu.zsc.rms.message;

import cn.edu.zsc.rms.domain.Message;
import cn.edu.zsc.rms.domain.User;

/**
 * @author hsj
 */
public interface MessagePush {
    /**
     * 根据message内定义的推送方式推送message
     * @param user
     * @param message
     * @throws Exception
     */
    void sendMessage(User user, Message message);
}
