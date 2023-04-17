package com.example.hadbackend.bean.carecontext;

import com.example.hadbackend.bean.auth.Err;
import com.example.hadbackend.bean.auth.Resp;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AddContextResponse {
    private String requestId;
    private  String timestamp;
    private Acknowledgement acknowledgement;
    private Err error;
    private Resp resp;
}
