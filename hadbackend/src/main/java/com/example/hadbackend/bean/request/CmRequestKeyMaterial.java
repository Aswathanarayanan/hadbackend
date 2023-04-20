package com.example.hadbackend.bean.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CmRequestKeyMaterial {
   
    private String cryptoAlg;
    private String curve;
    private CmRequestdhPublicKey dhPublicKey;
    private String nonce;

}
