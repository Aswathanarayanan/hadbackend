package com.example.hadbackend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.hadbackend.DAOimplement.HIPConsentRepository;
import com.example.hadbackend.bean.auth.Resp;
import com.example.hadbackend.bean.consent.FetchConsent;
import com.example.hadbackend.bean.consent.HIPConsentTable;
import com.example.hadbackend.bean.consent.OnConsentRequestID;
import com.example.hadbackend.bean.notify.HIUNotify;
import com.example.hadbackend.bean.notify.NotifyResponse;
import com.example.hadbackend.bean.notify.OnNotify;
import com.example.hadbackend.bean.notify.OnNotifyAcknowledgement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
@RestController
public class NotifyController {
    
    private String consentID,requestID;

    @Autowired
    HIPConsentRepository hipConsentRepository;

    FetchModeController fetchModeController=new FetchModeController();

    String token;
    
    @PostMapping("/v0.5/consents/hip/notify")
    public void Notify(@RequestBody NotifyResponse root) throws JsonProcessingException, ParseException{

        System.out.println("Received HIP Notify");
        
        consentID = root.getNotification().getConsentId();
        requestID = root.getRequestId();
    
        System.out.println(consentID);
        System.out.println(requestID);
        String status = root.getNotification().getStatus();
        if(status.equals("GRANTED")){
            
            System.out.println(root.getNotification().getConsentDetail().getHip().getId());
            
            //Store this data in DB

            String expDate = root.getNotification().getConsentDetail().getPermission().getDataEraseAt();
            String fromDate = root.getNotification().getConsentDetail().getPermission().getDateRange().getFrom();
            String toDate = root.getNotification().getConsentDetail().getPermission().getDateRange().getTo();
        
            HIPConsentTable consentTable = new HIPConsentTable();
            
            String strPattern = "\\d{4}-\\d{2}-\\d{2}";
            
            Pattern pattern = Pattern.compile(strPattern);
            Matcher matcher = pattern.matcher(expDate);
            
            Date date = new Date();
    
            while( matcher.find()){
                System.out.println( matcher.group());
                date = new SimpleDateFormat("yyyy-MM-dd").parse(matcher.group());
            }

            consentTable.setExpiryDate(date);

            matcher = pattern.matcher(fromDate);
            
            while( matcher.find()){
                System.out.println( matcher.group());
                date = new SimpleDateFormat("yyyy-MM-dd").parse(matcher.group());
            }
            
            consentTable.setDateFrom(date);

            matcher = pattern.matcher(toDate);
            
            while( matcher.find()){
                System.out.println( matcher.group());
                date = new SimpleDateFormat("yyyy-MM-dd").parse(matcher.group());
            }
        
            consentTable.setDateTo(date);
            
            consentTable.setAbhaid(root.getNotification().getConsentDetail().getPatient().getId());
            consentTable.setConsentId(consentID);
            
            hipConsentRepository.save(consentTable);
    
            System.out.println("Consent Artifact saved in HIP DB");

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
         
        }

    }

    @PostMapping("/v0.5/consents/hiu/notify")
    public void consentHIUNotify(@RequestBody HIUNotify root) throws JsonProcessingException{
        
        FetchConsent fetchConsent=new FetchConsent();
        
        System.out.println("---------"+root.getRequestId());

        ArrayList<OnConsentRequestID> consentIDList = root.getNotification().getConsentArtefacts();

        for(int i=0;i<consentIDList.size();i++)
        {
            token =fetchModeController.getsession();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.ALL));
            headers.add("X-CM-ID","sbx");
            headers.add("Authorization",token);
            
            fetchConsent.setConsentId(consentIDList.get(i).getId());

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
    }

    @PostMapping("/v0.5/consents/hiu/on-notify")
    public void consentHIUOnNotify(){

    }


}
