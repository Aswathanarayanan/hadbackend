package com.example.hadbackend.bean.dataTransfer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class HealthInfoNotifyStatusResponses {
    
    private String careContextReference;
    private String hiStatus;
    private String description;
}
