package com.uitility.personal.work.Service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class AutoReplyService {
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.properties.mail.smtp.auth:true}")
    private String Auth;

     public void sendAutoReply(Message originalMessage , String replytext) throws MessagingException {

         Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
         Session session = Session.getInstance(properties, new Authenticator(){
             @Override
             protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                 return new jakarta.mail.PasswordAuthentication(username, password);
             }
         });

         MimeMessage replyMessage = (MimeMessage) originalMessage.reply(false);
         replyMessage.setFrom(new InternetAddress(username));
         replyMessage.setText(replytext);
        // Transport.send(replyMessage);
            Transport transport = session.getTransport("smtp");
                transport.connect(host, username, password);
                transport.sendMessage(replyMessage, replyMessage.getAllRecipients());
                transport.close();
         System.out.println("Auto-reply sent to: " + replyMessage.getAllRecipients()[0].toString());
    }
}
