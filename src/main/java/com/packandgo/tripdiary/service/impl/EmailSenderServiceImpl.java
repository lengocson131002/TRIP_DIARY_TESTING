package com.packandgo.tripdiary.service.impl;

import com.packandgo.tripdiary.model.mail.MailContent;
import com.packandgo.tripdiary.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    public JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String FROM_EMAIL_SENDER;

    public void sendEmail(MailContent mailContent) {
        try {
            MimeMessage email = constructEmail(mailContent);
            mailSender.send(email);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("Can't resolve email");
        }
    }

    private MimeMessage constructEmail(MailContent mailContent) throws MessagingException {
        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email, true, "utf-8");

        ClassPathResource logoResource = new ClassPathResource(mailContent.getLogoImage());

        helper.setText(mailContent.getBody(), true);
        helper.addInline("logoImage", logoResource);
        helper.setFrom(FROM_EMAIL_SENDER);
        helper.setTo(mailContent.getToEmail());
        helper.setSubject(mailContent.getSubject());

        return email;
    }
}