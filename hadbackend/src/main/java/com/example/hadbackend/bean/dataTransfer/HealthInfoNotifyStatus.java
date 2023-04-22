package com.example.hadbackend.bean.dataTransfer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class HealthInfoNotifyStatus {
    
    private String sessionStatus;
    private String hipId;
    private List<HealthInfoNotifyStatusResponses> statusResponses;

}
