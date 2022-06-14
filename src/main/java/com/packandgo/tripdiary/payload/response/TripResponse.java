package com.packandgo.tripdiary.payload.response;

import com.packandgo.tripdiary.payload.TripPayload;

public class TripResponse extends TripPayload {
    private long id;
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
}
