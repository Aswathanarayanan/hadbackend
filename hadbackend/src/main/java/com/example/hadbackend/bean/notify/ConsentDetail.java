package com.example.hadbackend.bean.notify;


import com.example.hadbackend.bean.carecontext.Patient;
import com.example.hadbackend.bean.carecontext.Purpose;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ConsentDetail {
    private String consentId;
    private String createdAt;
    private Purpose PurposeObject;
    private Patient PatientObject;
}
