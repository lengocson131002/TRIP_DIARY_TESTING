package com.packandgo.tripdiary.controller;

import com.packandgo.tripdiary.payload.response.MessageResponse;
import com.packandgo.tripdiary.service.TripService;
import com.packandgo.tripdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitation")
public class InvitationController {

    private final TripService tripService;
    private final UserService userService;

    @Autowired
    public InvitationController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @PostMapping("/{tripId}/invite/{username}")
    public ResponseEntity<?> inviteToJoinTrip(@PathVariable(required = true, name = "tripId") Long tripId,
                                    @PathVariable(required = true, name = "username") String username) {
        tripService.inviteToJoinTrip(tripId, username);
        return ResponseEntity.ok(new MessageResponse("Invitation was sent successfully"));
    }

    @DeleteMapping("/{tripId}/uninvite/{username}")
    public ResponseEntity<?> removeTripMate(
            @PathVariable(required = true, name = "tripId") Long tripId,
            @PathVariable(required = true, name = "username") String username
    ) {
        tripService.removeTripMate(tripId, username);
        return ResponseEntity.ok(new MessageResponse("Trip mate was removed from this trip successfully"));
    }
}
