package com.packandgo.tripdiary.service;

import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.model.mail.MailContent;

public interface EmailSenderService {
    public void sendEmail(MailContent mailContent);
}
