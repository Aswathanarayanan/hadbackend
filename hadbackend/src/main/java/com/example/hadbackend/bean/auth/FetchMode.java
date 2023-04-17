package com.example.hadbackend.bean.auth;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FetchMode {

    private String requestId;
    private String timestamp;
    private Auth auth;
    private Err error;
    private Resp resp;

    public String getRequestId(){
        return this.requestId;
    }

    public String getTimestamp(){
        return this.timestamp;
    }

    public Auth getAuth(){
        return this.auth;
    }

    public Err getError(){
        return this.error;
    }

    public Resp getResp(){
        return this.resp;
    }
}
