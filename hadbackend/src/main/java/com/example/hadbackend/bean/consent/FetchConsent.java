package com.example.hadbackend.bean.consent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FetchConsent {
    
    private String requestId;
    private String timestamp;
    private String consentId;
      
}
