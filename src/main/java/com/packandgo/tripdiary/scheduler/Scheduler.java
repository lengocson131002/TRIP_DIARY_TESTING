package com.packandgo.tripdiary.scheduler;

import com.packandgo.tripdiary.model.Trip;
import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.model.mail.MailContent;
import com.packandgo.tripdiary.model.mail.NotificationMailContent;
import com.packandgo.tripdiary.service.EmailSenderService;
import com.packandgo.tripdiary.service.TripService;
import com.packandgo.tripdiary.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class Scheduler {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static Logger logger = LoggerFactory.getLogger(Scheduler.class);
    private final TripService tripService;
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @Value("${tripdiary.baseurl.frontend}")
    private String frontendUrl;

    @Autowired
    public Scheduler(TripService tripService,
                     UserService userService, EmailSenderService emailSenderService) {
        this.tripService = tripService;
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    /**
     * Check at every 00:01:00 every day
     */
    @Scheduled(cron = "0 1 0 * * *")
    public void sendNotificationEmailScheduler() {
        List<Trip> notifiedTrip = tripService.getNotifiedTripsForDay();

        if(notifiedTrip.size() > 0) {
            //sent Email
            for(Trip trip: notifiedTrip) {
                List<User> users = trip.getUsers();
                for (User user: users) {
                    MailContent mailContent = new NotificationMailContent(trip,user, frontendUrl);
                    ScheduledTask task = new SendNotificationMailTask(emailSenderService, mailContent);
                    task.doTask();
                }

                //save nofication;
            }
        }
        logger.info("SENT EMAIL");
    }
}
