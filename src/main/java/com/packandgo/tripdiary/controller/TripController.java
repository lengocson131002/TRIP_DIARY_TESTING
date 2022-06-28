package com.packandgo.tripdiary.controller;

import com.packandgo.tripdiary.exception.TripNotFoundException;
import com.packandgo.tripdiary.model.Trip;

import com.packandgo.tripdiary.payload.request.trip.TripRequest;
import com.packandgo.tripdiary.payload.response.MessageResponse;
import com.packandgo.tripdiary.payload.response.PagingResponse;
import com.packandgo.tripdiary.payload.response.TripResponse;
import com.packandgo.tripdiary.repository.UserRepository;
import com.packandgo.tripdiary.service.TripService;
import com.packandgo.tripdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trips")
public class TripController {
    private final TripService tripService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public TripController(TripService tripService, UserRepository userRepository, UserService userService) {
        this.tripService = tripService;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @GetMapping("/{id}")
    @ExceptionHandler(value = {TripNotFoundException.class})
    public ResponseEntity<?> getTrip(@PathVariable(name = "id", required = true) Long tripId) {
        Trip trip = tripService.get(tripId);
        TripResponse tripResponse = trip.toResponse();
        return ResponseEntity.ok(tripResponse);
    }


    @GetMapping("")
    public ResponseEntity<?> getAllTrips(@RequestParam(defaultValue = "1", required = false) int page,
                                         @RequestParam(defaultValue = "10", required = false) int size) {

        page = page <= 0 ? 1 : page;
        Page<Trip> trips = tripService.getTrips(page, size);
        List<TripResponse> tripResponses = trips
                .stream()
                .map(t -> t.toResponse())
                .collect(Collectors.toList());

        PagingResponse<TripResponse> response = new PagingResponse<>(page, size, trips.getTotalPages(), tripResponses);
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<?> insertTrip(@RequestBody TripRequest tripRequest) {

        Trip savedTrip = tripService.insertTrip(tripRequest);
        TripResponse tripResponse = savedTrip.toResponse();
        return ResponseEntity.ok(tripResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrip(@PathVariable(name = "id", required = true) Long tripId) {
        tripService.removeTrip(tripId);
        return ResponseEntity.ok(new MessageResponse("Trip was removed successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTrip(@PathVariable(name = "id", required = true) Long tripId,
                                        @RequestBody TripRequest request) {

        Trip savedTrip = tripService.updateTrip(tripId, request);
        TripResponse tripResponse = savedTrip.toResponse();
        return ResponseEntity.ok(tripResponse);
    }

    @PostMapping("/like/{id}")

    public ResponseEntity<?> likeTrip(@PathVariable(name = "id", required = true) Long tripId) {
        tripService.likeTrip(tripId);
        return ResponseEntity.ok(new MessageResponse("OK"));
    }


}