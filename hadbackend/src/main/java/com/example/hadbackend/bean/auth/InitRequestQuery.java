package com.example.hadbackend.bean.auth;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class InitRequestQuery {
    private String id;

    private String purpose;

    private String authMode;

    private Requester requester;

    public void setId(String id){
        this.id=id;
    }

    public void setPurpose(String purpose){
        this.purpose=purpose;
    }
    public void setAuthMode(String authMode){
        this.authMode=authMode;
    }
    public void setRequester(Requester requester){
        this.requester=requester;
    }

    public String getId(){
        return this.id;
    }
    public String getPurpose(){
        return this.purpose;
    }

    public String getAuthMode(String authMode){
        return this.authMode;
    }

    public Requester getRequester(){
        return this.requester;
    }

}