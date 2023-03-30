package com.example.hadbackend.bean.carecontext;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AddContextPatientRequest {

    private Integer referenceNumber;
    private  String display;
    private ArrayList<AddContextcareContextsRequest> careContexts;
}
