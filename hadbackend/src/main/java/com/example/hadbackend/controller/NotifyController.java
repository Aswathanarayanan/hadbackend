package com.example.hadbackend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.hadbackend.bean.notify.HIUNotify;
import com.example.hadbackend.bean.notify.NotifyResponse;

import org.springframework.web.bind.annotation.PostMapping;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RestController
public class NotifyController {
    
    private String consentID,requestID;

    
    @PostMapping("/v0.5/consents/hip/notify")
    public void Notify(NotifyResponse root){
        consentID = root.getNotificationObject().getConsentId();
        requestID = root.getRequestId();
    }

    @PostMapping("/v0.5/consents/hiu/notify")
    public void consentHIUNotify(HIUNotify root){

    }

    @PostMapping("/v0.5/consents/hiu/on-notify")
    public void consentHIUOnNotify()
    {

    }


}
