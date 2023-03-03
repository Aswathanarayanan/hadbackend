package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ConfirmAuth {
    private String accessToken;
    private ConfirmValidity validity;
    private String expiry;
    private int limit;
}
