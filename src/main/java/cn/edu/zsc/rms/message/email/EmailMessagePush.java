package cn.edu.zsc.rms.message.email;


import cn.edu.zsc.rms.domain.Message;
import cn.edu.zsc.rms.domain.User;
import cn.edu.zsc.rms.message.MessagePush;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author hsj
 */
@Component
public class EmailMessagePush implements MessagePush {

    private JavaMailSender mailSender;
    private EmailConfig config;

    public EmailMessagePush(JavaMailSender mailSender, EmailConfig config) {
        this.mailSender = mailSender;
        this.config = config;
    }

    @Override
    public void sendMessage(User user, Message message) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(config.getUsername());
            helper.setTo(user.getEmail());
            helper.setSubject(message.getTitle());
            helper.setText(message.getBriefContent());
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
