package com.packandgo.tripdiary.payload.request.trip;

public class LikeRequest {
    private Long id;

    public void LikeRequest(){}

    public Long getTripId() {
        return id;
    }

    public void setTripId(Long id) {
        this.id = id;
    }
}
