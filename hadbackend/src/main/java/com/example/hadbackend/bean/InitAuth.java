package com.example.hadbackend.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class InitAuth {
    private String transactionId;
    private String mode;
    private Meta meta;

    public String getTransactionid(){
        return this.transactionId;
    }

    public String getMode(){
        return this.mode;
    }
}
