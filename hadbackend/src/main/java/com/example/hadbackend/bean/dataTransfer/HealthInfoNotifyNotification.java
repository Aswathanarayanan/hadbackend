package com.example.hadbackend.bean.dataTransfer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class HealthInfoNotifyNotification {
    
    private String consentId;
    private String transactionId;
    private String doneAt;
    private HealthInfoNotifyNotifier notifier;
    private HealthInfoNotifyStatus statusNotification;

}
