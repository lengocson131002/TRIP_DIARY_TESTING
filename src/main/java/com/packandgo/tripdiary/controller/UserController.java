package com.packandgo.tripdiary.controller;

import com.packandgo.tripdiary.model.Trip;
import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.model.UserInfo;
import com.packandgo.tripdiary.payload.response.PagingResponse;
import com.packandgo.tripdiary.payload.response.TripResponse;
import com.packandgo.tripdiary.payload.response.UserResponse;
import com.packandgo.tripdiary.service.TripService;
import com.packandgo.tripdiary.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final TripService tripService;

    public UserController(UserService userService, TripService tripService) {
        this.userService = userService;
        this.tripService = tripService;
    }

    @GetMapping("/{username}/trips")
    public ResponseEntity<?> getUserWithAllTrips(
            @PathVariable(name = "username", required = true) String username,
            @RequestParam(name = "target", required = false) String me) {
        User user = userService.findUserByUsername(username);
        UserInfo userInfo = userService.getInfo(user);
        List<Trip> trips = userService.getTripsForUser(user, me);
        List<TripResponse> tripResponses = trips
                .stream()
                .map(trip -> trip.toResponse())
                .collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();

        userResponse.setUsername(username);
        userResponse.setAboutMe(userInfo.getAboutMe());
        userResponse.setCountry(userInfo.getCountry());
        userResponse.setProfileImageUrl(userInfo.getProfileImageUrl());
        userResponse.setCoverImageUrl(userInfo.getCoverImageUrl());
        userResponse.setTrips(tripResponses);

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/trips")
    public ResponseEntity<?> getTripsForAllUser(@RequestParam(defaultValue = "1", required = false) int page,
                                                @RequestParam(defaultValue = "10", required = false) int size) {

        page = page <= 0 ? 1 : page;
        Page<UserResponse> result =  userService.getUsersAndAllTrips(page, size);
        PagingResponse<UserResponse> response = new PagingResponse<>(page, size, result.getTotalPages(), result.getContent());
        return ResponseEntity.ok(response);
    }


}
