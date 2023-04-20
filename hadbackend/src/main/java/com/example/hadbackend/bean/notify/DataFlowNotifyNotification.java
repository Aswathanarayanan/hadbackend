package com.example.hadbackend.bean.notify;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DataFlowNotifyNotification {
    
    private String consentId;
    private String transactionId;
    private String doneAt;
    private DataFlowNotifyNotifier notifier;
    private DataFlowNotifyStatusNoti statusNotification;
    
}
