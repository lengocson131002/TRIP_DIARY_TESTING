package com.packandgo.tripdiary.payload.response;

import com.packandgo.tripdiary.model.Trip;

import java.util.List;

public class SearchResponse {
    private String keyword;
    private List<Trip> trips;
    private List<UserResponse> users;

    public SearchResponse() {

    }
    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public List<UserResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponse> users) {
        this.users = users;
    }
}
