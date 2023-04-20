package com.example.hadbackend.bean.fhirBundle;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class Bundle {
    private String resourceType;
    private String id;
    private Meta meta;
    private Identifier identifier;
    private String type;
    private String timestamp;
    private ArrayList<Object> entry;
    //private Signature signature;

}
