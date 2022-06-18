package com.packandgo.tripdiary.model.mail;

import com.packandgo.tripdiary.constants.BaseUrl;
import com.packandgo.tripdiary.model.Trip;
import com.packandgo.tripdiary.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.context.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class NotificationMailContent extends  MailContent{
    private Trip trip;
    private String frontendUrl;
    private final String TRIP_NOTIFICATION_EMAIL_LOCATION = "notification-trip";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    public NotificationMailContent(Trip trip, User user, String frontendUrl) {
        this.toEmail = user.getEmail();
        this.trip = trip;
        this.frontendUrl = frontendUrl;
        this.subject = "TRIP NOTIFICATION ";
        this.buildBody();

    }
    @Override
    public void buildBody() {
        Context context = new Context();
        context.setVariable("message", "You are going on a trip in next " + trip.getNotifyBefore() + " days (" + formatter.format(trip.getBeginDate()) + ")");
        context.setVariable("name", trip.getName());
        String tripUrl = frontendUrl + "/trip/" + trip.getId();
        context.setVariable("tripUrl", tripUrl);
        String htmlBody = this.templateEngine.process(TRIP_NOTIFICATION_EMAIL_LOCATION, context);

        this.body = htmlBody;
    }
}
