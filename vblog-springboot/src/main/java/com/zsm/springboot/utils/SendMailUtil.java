package com.zsm.springboot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件发送工具类
 */
@Component
public class SendMailUtil {

    @Autowired
    JavaMailSender javaMailSender;

    Logger logger = LoggerFactory.getLogger(getClass());

    public void sendMail(String from, String to, String subject, String content) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true); // 可以发送附件

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content,true); // 支持发送 html 代码

        javaMailSender.send(message);
        logger.info("邮件发送成功 >>> " + to);
    }
}
