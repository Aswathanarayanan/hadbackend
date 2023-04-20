package com.example.hadbackend.bean.consent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.example.hadbackend.bean.auth.Err;
import com.example.hadbackend.bean.auth.Resp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnInitConsentRequest {
    
    private String requestId;
    private String timestamp;
    private OnConsentRequestID consentRequest;
    private Err error;
    private Resp resp;
}
