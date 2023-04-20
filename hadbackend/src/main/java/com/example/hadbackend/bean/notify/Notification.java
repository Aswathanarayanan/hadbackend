package com.example.hadbackend.bean.notify;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Notification {
    private ConsentDetail ConsentDetail;
    private String status;
    private String signature;
    private String consentId;
    private boolean grantAcknowledgement;
}
