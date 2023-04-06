package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnConfirmRequest {
    private String requestId;
    private String timestamp;
    private String transactionId;
    private Credential credential;

    public void setTimestamp(String timestamp){
        this.timestamp=timestamp;
    }
    public void setRequestid(String requestid){
        this.requestId=requestid;
    }
    public void setTransactionid(String transactionid){
        this.transactionId=transactionid;
    }
    public void setCredential(Credential credential){
        this.credential=credential;
    }
}
