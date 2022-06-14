package com.packandgo.tripdiary.model.mail;

import com.packandgo.tripdiary.constants.BaseUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

public class ResetPasswordMailContent extends MailContent {

    private String resetPasswordToken;
    private final String RESET_PASSWORD_EMAIL_LOCATION = "reset-password-email";

    private final String frontendUrl;

    public ResetPasswordMailContent(String toEmail, String resetPasswordToken, String frontendUrl) {
        super();
        this.toEmail = toEmail;
        this.resetPasswordToken = resetPasswordToken;
        this.frontendUrl = frontendUrl;
        this.subject = "RESET PASSWORD REQUEST";
        this.buildBody();
    }

    @Override
    public void buildBody() {
        String verifyURL = frontendUrl + "/user/change-password?token=" + resetPasswordToken;
        Context context = new Context();
        context.setVariable("resetPasswordUrl", verifyURL);
        String htmlBody = this.templateEngine.process(RESET_PASSWORD_EMAIL_LOCATION, context);

        this.body = htmlBody;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }
}
