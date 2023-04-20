package com.example.hadbackend.bean.consent;

import java.util.ArrayList;

import com.example.hadbackend.bean.notify.NotifycareContext;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OnFetchConsentconsentDetails {
    
    private String schemaVersion;
    private String consentId;
    private String createdAt;
    private ConsentPatient patient;
    private ArrayList<NotifycareContext> careContexts;
    private OnFetchConsentPurpose purpose;
    private OnFetchConsentHIP hip;
    private OnFetchConsentHIP hiu;
    private OnFetchConsentConsentManager consentManager;
    private ConsentRequester requester;
    private ArrayList<String> hiTypes;
    private ConsentPermission permission;
}
