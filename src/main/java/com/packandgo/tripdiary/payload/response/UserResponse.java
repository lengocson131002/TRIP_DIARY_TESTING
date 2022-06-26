package com.packandgo.tripdiary.payload.response;

import com.packandgo.tripdiary.model.Trip;

import java.util.List;

public class UserResponse {
    private String username;
    private String avatar;
    private String aboutMe;
    private String country;
    private String profileImageUrl;
    private String coverImageUrl;
    private List<TripResponse> trips;

    public UserResponse() {}

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public List<TripResponse> getTrips() {
        return trips;
    }

    public void setTrips(List<TripResponse> trips) {
        this.trips = trips;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
