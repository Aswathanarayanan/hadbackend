package com.example.hadbackend.bean.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CmRequesthiRequest {
    
    private CmRequestConsent consent;
    
    private CmRequestDateRange dateRange;
    private String dataPushUrl;
    private CmRequestKeyMaterial keyMaterial;
}
