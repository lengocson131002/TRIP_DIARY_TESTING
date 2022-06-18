package com.packandgo.tripdiary.model.mail;

import com.packandgo.tripdiary.model.Trip;
import com.packandgo.tripdiary.model.User;
import org.thymeleaf.context.Context;

public class InviteJoinTripContent extends MailContent {

    private Trip trip;
    private User joiner;
    private String frontendUrl;
    private final String INVITE_JOIN_TRIP_EMAIL_LOCATION = "notification-trip";

    public InviteJoinTripContent(Trip trip, User joiner, String frontendUrl) {
        this.trip = trip;
        this.joiner = joiner;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public void buildBody() {
        Context context = new Context();
        context.setVariable("message", trip.getOwner() + " invites you to join a trip diary");
        context.setVariable("name", trip.getName());
        String tripUrl = frontendUrl + "/trips/" + trip.getId();
        context.setVariable("tripUrl", tripUrl);

        String htmlBody = this.templateEngine.process(INVITE_JOIN_TRIP_EMAIL_LOCATION, context);

        this.body = htmlBody;
    }
}
