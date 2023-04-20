package com.example.hadbackend.bean.fhirBundle;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class EntryResourceCustodian {
    
    private String reference;
    private String display;
}
