package com.topseeker.mailutil;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Value;

public class MailManager {

    Session mailSession;

    public MailManager(
        @Value("${mail.pwd}") String mailServerPwd,
        @Value("${mail.user}") String mailServerUser
    ){
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.connectiontimeout", "10000");
        properties.setProperty("mail.smtp.timeout", "10000");
        properties.setProperty("mail.smtp.writetimeout", "10000");
        properties.setProperty("mail.smtp.starttls.enable", "true"); //安全協議 

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailServerUser, mailServerPwd);
            }
        };

        this.mailSession = Session.getInstance(properties, auth);
    }

    public void sentMail(String from, List<String> sendTo,List<String> cc,
    	       List<String> bcc, String personal, String subject, String context) {

    	    try {

    	        Message message = new MimeMessage(this.mailSession);
    	        message.setFrom(new InternetAddress(from, personal,  "UTF-8"));
    	        message.setRecipients(RecipientType.TO, InternetAddress.parse(String.join(",", sendTo)));
    	        message.setRecipients(RecipientType.CC, InternetAddress.parse(String.join(",", cc)));
    	        message.setRecipients(RecipientType.BCC, InternetAddress.parse(String.join(",", bcc)));
    	        message.setSubject(subject);
    	        message.setText(context);
    	        Transport.send(message);

    	    } catch (MessagingException e) {
    	        e.printStackTrace();
    	        throw new RuntimeException("寄信失敗！");
    	    } catch (UnsupportedEncodingException e) {
    	        e.printStackTrace();
    	    }
    	}
}
