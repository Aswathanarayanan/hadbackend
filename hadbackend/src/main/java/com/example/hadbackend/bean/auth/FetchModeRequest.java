package com.example.hadbackend.bean.auth;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FetchModeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String requestId;
    private String timestamp;
    private Query query;

    public String getRequestId(){
        return this.requestId;
    }

    public String getTimestamp(){
        return this.timestamp;
    }

    public Query getQuery(){
        return this.query;
    }

    public void setRequestId(String id){
        this.requestId=id;
    }

    public void setTimestamp(String timestamp){
        this.timestamp=timestamp;
    }

    public void setQuery(Query query){
        this.query=query;
    }
}
