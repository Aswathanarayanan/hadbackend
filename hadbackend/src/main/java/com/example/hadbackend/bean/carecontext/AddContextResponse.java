package com.example.hadbackend.bean.carecontext;

import com.example.hadbackend.bean.Resp;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jms.JmsProperties;


@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AddContextResponse {
    private String requestId;
    private  String timestamp;
    private Acknowledgement acknowledgement;
    private Error error;
    private Resp resp;
}
