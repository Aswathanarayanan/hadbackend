package com.example.hadbackend.bean.notify;

import java.util.ArrayList;

import com.example.hadbackend.bean.consent.ConsentPatient;
import com.example.hadbackend.bean.consent.ConsentPermission;
import com.example.hadbackend.bean.consent.OnFetchConsentConsentManager;
import com.example.hadbackend.bean.consent.OnFetchConsentPurpose;
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
    private OnFetchConsentPurpose PurposeObject;
    private ConsentPatient PatientObject;
    private OnFetchConsentConsentManager consentManager;
    private NotifyHIP hip;
    private ArrayList<String> hiTypes;
    private ConsentPermission permission;
    private ArrayList<NotifycareContext> careContexts;

}