package com.packandgo.tripdiary.model.mail;

import com.packandgo.tripdiary.constants.BaseUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

public class VerifyEmailMailContent extends MailContent {
    private String verifyToken;
    private final String REGISTRATION_EMAIL_LOCATION = "registration-email";

    private final String backendUrl;
    @Autowired
    public VerifyEmailMailContent(String toEmail, String verifyToken, String backendUrl) {
        this.toEmail = toEmail;
        this.verifyToken = verifyToken;
        this.backendUrl = backendUrl;
        this.subject = "VERIFY REGISTRATION";
        this.buildBody();
    }

    @Override
    public void buildBody() {
        String verifyURL = backendUrl + "/api/auth/verify?token=" + this.verifyToken;
        Context context = new Context();
        context.setVariable("registrationUrl", verifyURL);
        String htmlBody = this.templateEngine.process(REGISTRATION_EMAIL_LOCATION, context);

        this.body = htmlBody;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }
}
