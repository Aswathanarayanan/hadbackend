package com.example.hadbackend.bean.consent;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Consent {
    private ConsentPurpose purpose;
    private ConsentPatient patient;
    private ConsentHIU hiu;
    private ConsentRequester requester;
    private ArrayList<String> hiTypes;
    private ConsentPermission permission;


}
