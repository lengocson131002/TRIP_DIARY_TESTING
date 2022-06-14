package com.packandgo.tripdiary.auth.oauth2;

public interface OAuth2UserInfo {
    public String getProviderId();
    public String getProvider();
    public String getEmail();
    public String getName();
    public String getProfileImageUrl();
}
