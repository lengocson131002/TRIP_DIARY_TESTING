package com.packandgo.tripdiary.service;

import com.packandgo.tripdiary.model.Trip;
import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.payload.request.trip.TripRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface TripService {
    public Trip insertTrip(TripRequest request);
    public Page<Trip> getTrips(int page, int size);
    public void removeTrip(Long id);
    public Trip updateTrip(Long tripId, TripRequest request);
    public Trip get(Long id);
    public List<Trip> getTripsForUser(User user);
    public void likeTrip(Long tripId);
    public boolean existedTrip(Long tripId);
    public boolean existedLike(Long tripId);
    public List<Trip> getNotifiedTripsForDay();


}
