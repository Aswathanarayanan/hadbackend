package com.example.hadbackend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.hadbackend.bean.auth.Resp;
import com.example.hadbackend.bean.consent.FetchConsent;
import com.example.hadbackend.bean.notify.HIUNotify;
import com.example.hadbackend.bean.notify.NotifyResponse;
import com.example.hadbackend.bean.notify.OnNotify;
import com.example.hadbackend.bean.notify.OnNotifyAcknowledgement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@RestController
public class NotifyController {
    
    private String consentID,requestID;

    FetchModeController fetchModeController=new FetchModeController();

    String token;
    
    @PostMapping("/v0.5/consents/hip/notify")
    public void Notify(@RequestBody NotifyResponse root) throws JsonProcessingException{

        System.out.println("Received HIP Notify");
        
        consentID = root.getNotification().getConsentId();
        requestID = root.getRequestId();
        
        System.out.println(consentID);
        System.out.println(requestID);
        System.out.println(root.getNotification().getConsentDetail().getHip().getId());

        token =fetchModeController.getsession();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.add("X-CM-ID","sbx");
        headers.add("Authorization",token);
        
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
       
        TimeZone timeZone=TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS");
        dateFormat.setTimeZone(timeZone);
        String asISO= dateFormat.format(new Date());

        OnNotify onNotify=new OnNotify();

        onNotify.setRequestId(randomUUIDString);
        onNotify.setTimestamp(asISO);

        OnNotifyAcknowledgement onNotifyAcknowledgement=new OnNotifyAcknowledgement();

        onNotifyAcknowledgement.setStatus("OK");
        onNotifyAcknowledgement.setConsentId(consentID);

        onNotify.setAcknowledgement(onNotifyAcknowledgement);

        Resp resp=new Resp();
        
        resp.setRequestId(requestID);

        onNotify.setResp(resp);

        String curr_body=new ObjectMapper().writeValueAsString(onNotify);
        HttpEntity<String> httpEntity = new HttpEntity<>(curr_body, headers);
        
        System.out.println("Calling OnNotify");

        ResponseEntity<Object> objectResponseEntity=restTemplate.exchange("https://dev.abdm.gov.in/gateway/v0.5/consents/hip/on-notify", HttpMethod.POST, httpEntity,Object.class);
        
        System.out.println(objectResponseEntity.getStatusCode());

        FetchConsent fetchConsent=new FetchConsent();

        fetchConsent.setConsentId(consentID);

        UUID uuid2 = UUID.randomUUID();
        String randomUUIDString2 = uuid2.toString();
       
        TimeZone timeZone2=TimeZone.getTimeZone("UTC");
        DateFormat dateFormat2 = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS");
        dateFormat2.setTimeZone(timeZone2);
        String asISO2= dateFormat2.format(new Date());

        fetchConsent.setRequestId(randomUUIDString2);
        fetchConsent.setTimestamp(asISO2);

        String curr_body2=new ObjectMapper().writeValueAsString(fetchConsent);
        HttpEntity<String> httpEntity2 = new HttpEntity<>(curr_body2, headers);
        
        System.out.println("Calling Fetch Consent");

        ResponseEntity<Object> objectResponseEntity2=restTemplate.exchange("https://dev.abdm.gov.in/gateway/v0.5/consents/fetch", HttpMethod.POST, httpEntity2,Object.class);

        System.out.println(objectResponseEntity2.getStatusCode());

    }

    @PostMapping("/v0.5/consents/hiu/notify")
    public void consentHIUNotify(HIUNotify root){

    }

    @PostMapping("/v0.5/consents/hiu/on-notify")
    public void consentHIUOnNotify()
    {

    }


}
