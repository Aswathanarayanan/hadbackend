package com.example.hadbackend.bean.notify;

import com.example.hadbackend.bean.Resp;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnNotify {
    
    private String requestId;
    private String timestamp;
    private OnNotifyAcknowledgement acknowledgement;
    //private OnNotifyError error;
    private Resp resp;
    
}
