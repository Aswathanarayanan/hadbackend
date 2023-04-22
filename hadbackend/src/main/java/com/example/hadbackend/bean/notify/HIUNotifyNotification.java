package com.example.hadbackend.bean.notify;

import java.util.ArrayList;

import com.example.hadbackend.bean.consent.OnConsentRequestID;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class HIUNotifyNotification {
    
   private String consentRequestId;
   private String status;
   private ArrayList<OnConsentRequestID> consentArtefacts;
   
}
