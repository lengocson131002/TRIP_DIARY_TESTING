package com.packandgo.tripdiary.scheduler;

import com.packandgo.tripdiary.model.mail.MailContent;
import com.packandgo.tripdiary.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class SendNotificationMailTask implements  ScheduledTask{

    private EmailSenderService emailSenderService;
    private MailContent content;

    public SendNotificationMailTask(EmailSenderService service, MailContent content) {
        this.content = content;
        this.emailSenderService = service;
    }

    public void doTask() {
        emailSenderService.sendEmail(content);
    }
}
