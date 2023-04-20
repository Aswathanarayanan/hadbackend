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
public class EntryResourcetype {

    private ArrayList<EntryResourcetypeCoding> coding;
    private String text;
    
}
