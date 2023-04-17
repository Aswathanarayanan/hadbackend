package com.example.hadbackend.bean.request;

import com.example.hadbackend.bean.Err;
import com.example.hadbackend.bean.Resp;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnRequest {
    
    private String requestId;
    private String timestamp;
    private OnRequesthiRequest hiRequest;
    private Err error;
    private Resp resp;
}
