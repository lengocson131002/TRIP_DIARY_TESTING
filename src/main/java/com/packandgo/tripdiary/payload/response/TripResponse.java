package com.packandgo.tripdiary.payload.response;

import com.packandgo.tripdiary.payload.TripPayload;

import java.util.ArrayList;
import java.util.List;

public class TripResponse extends TripPayload {
    private long id;
    private String owner;
    private List<String> tripMates;
    public TripResponse() {
        super();
        tripMates = new ArrayList<>();
    }
    private int numOfLikes;
//    private int List<Comment> comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getTripMates() {
        return tripMates;
    }

    public void setTripMates(List<String> tripMates) {
        this.tripMates = tripMates;
    }
}
