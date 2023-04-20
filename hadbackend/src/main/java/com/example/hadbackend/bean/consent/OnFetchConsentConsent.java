package com.example.hadbackend.bean.consent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnFetchConsentConsent {
    
    private String status;
    private OnFetchConsentconsentDetails consentDetail;        
    private String signature;
    
}
