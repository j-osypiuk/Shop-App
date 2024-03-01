package com.shopapp.service;

import com.shopapp.model.MailDetails;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final String FROM_MAIL_ADDRESS = "";
    private final String MAIL_SUBJECT = "Thanks for shopping in our store!";
    private JavaMailSender javaMailSender;
    private Configuration config;

    public EmailService(JavaMailSender javaMailSender, Configuration config) {
        this.javaMailSender = javaMailSender;
        this.config = config;
    }

    public void sendMail(String toEmail, MailDetails mailModel) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            Template template = config.getTemplate("email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModel);

            helper.setFrom(this.FROM_MAIL_ADDRESS);
            helper.setTo(toEmail);
            helper.setSubject(this.MAIL_SUBJECT);
            helper.setText(html, true);

            javaMailSender.send(message);
        } catch (IOException | TemplateException | MailException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
