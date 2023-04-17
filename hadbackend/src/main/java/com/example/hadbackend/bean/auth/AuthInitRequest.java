package com.example.hadbackend.bean.auth;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)

public class AuthInitRequest {
    private String requestId;

    private String timestamp;

    private InitRequestQuery query;

    public void setRequestId(String id){
        this.requestId=id;
    }

    public void setTimestamp(String timestamp){
        this.timestamp=timestamp;
    }

    public void setQuery(InitRequestQuery query){
        this.query=query;
    }

    public String getRequestId(){
        return this.requestId;
    }

    public String getTimestamp(){
        return this.timestamp;
    }

    public InitRequestQuery getQuery(){
        return this.query;
    }
}
