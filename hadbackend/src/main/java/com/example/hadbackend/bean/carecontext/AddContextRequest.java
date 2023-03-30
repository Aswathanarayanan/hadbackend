package com.example.hadbackend.bean.carecontext;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AddContextRequest {

    private String requestId;
    private String timestamp;
    private AddContectLinkRequest link;

}
