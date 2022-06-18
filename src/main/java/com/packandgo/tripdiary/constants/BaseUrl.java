package com.packandgo.tripdiary.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseUrl {
    public static final String BACK_END = "http://localhost:8080";
    public static final String FRONT_END = "http://localhost:3000";
}
