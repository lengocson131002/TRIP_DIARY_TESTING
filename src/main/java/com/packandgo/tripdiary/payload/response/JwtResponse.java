package com.packandgo.tripdiary.payload.response;

import com.packandgo.tripdiary.model.Role;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class JwtResponse {
    private String token;
    private String username;
    private String email;
    private String tokeType = "Bearer";

    public JwtResponse(
                       String token,
                       String username,
                       String email) {
        this.token = token;
        this.username = username;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTokeType() {
        return tokeType;
    }

    public void setTokeType(String tokeType) {
        this.tokeType = tokeType;
    }

}
