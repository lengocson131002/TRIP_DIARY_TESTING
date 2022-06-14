package com.packandgo.tripdiary.auth.oauth2;

import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo{

    private Map<String, Object> attribute;

    public GoogleOAuth2UserInfo() {
    }

    public GoogleOAuth2UserInfo(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProviderId() {
        return (String)attribute.get("googleId");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String)attribute.get("email");
    }

    @Override
    public String getName() {
        return (String)attribute.get("name");
    }

    @Override
    public String getProfileImageUrl() {
        return (String)attribute.get("imageUrl");
    }
}
