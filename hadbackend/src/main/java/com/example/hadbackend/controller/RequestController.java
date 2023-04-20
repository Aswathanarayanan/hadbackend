package com.example.hadbackend.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.hadbackend.bean.auth.Resp;
import com.example.hadbackend.bean.consent.OnFetchConsent;
import com.example.hadbackend.bean.request.CmRequest;
import com.example.hadbackend.bean.request.CmRequestConsent;
import com.example.hadbackend.bean.request.CmRequestDateRange;
import com.example.hadbackend.bean.request.CmRequestKeyMaterial;
import com.example.hadbackend.bean.request.CmRequestdhPublicKey;
import com.example.hadbackend.bean.request.CmRequesthiRequest;
import com.example.hadbackend.bean.request.HIPRequest;
import com.example.hadbackend.bean.request.OnRequest;
import com.example.hadbackend.bean.request.OnRequesthiRequest;
import com.example.hadbackend.bean.security.KeyGenerator;
import com.example.hadbackend.bean.security.KeyMaterial;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RestController
public class RequestController {
    
    FetchModeController fetchModeController=new FetchModeController();

    String token;
    
    @PostMapping("/v0.5/consents/on-fetch")
    public void requestData(@RequestBody OnFetchConsent onFetchConsent) throws Exception{

        System.out.println("Received On Fetch Consent");
        
        System.out.println(onFetchConsent.getConsent().getConsentDetail().getConsentId());
        System.out.println(onFetchConsent.getConsent().getConsentDetail().getPatient().getId());
        System.out.println(onFetchConsent.getConsent().getConsentDetail().getHip().getId());
        System.out.println(onFetchConsent.getConsent().getConsentDetail().getPermission().getDataEraseAt());

        String consentID = onFetchConsent.getConsent().getConsentDetail().getConsentId();
        String dateFrom = onFetchConsent.getConsent().getConsentDetail().getPermission().getDateRange().getFrom();
        String dateTo = onFetchConsent.getConsent().getConsentDetail().getPermission().getDateRange().getTo();
        //Store this data in DB

        //cm/request

        CmRequest cmRequest = new CmRequest();
        
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        cmRequest.setRequestId(randomUUIDString);

        TimeZone timeZone=TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS");
        dateFormat.setTimeZone(timeZone);
        String asISO= dateFormat.format(new Date());
        cmRequest.setTimestamp(asISO);

        CmRequesthiRequest hiRequest = new CmRequesthiRequest();

        CmRequestConsent consent = new CmRequestConsent();
        consent.setId(consentID);
        hiRequest.setConsent(consent);

        CmRequestDateRange cmRequestDateRange=new CmRequestDateRange();
        cmRequestDateRange.setFrom(dateFrom);
        cmRequestDateRange.setTo(dateTo);
        hiRequest.setDateRange(cmRequestDateRange);

        //Change URL (NGROK)
        hiRequest.setDataPushUrl("https://ea9c-103-156-19-229.in.ngrok.io");

        CmRequestKeyMaterial cmRequestKeyMaterial = new CmRequestKeyMaterial();
        cmRequestKeyMaterial.setCryptoAlg("ECDH");
        cmRequestKeyMaterial.setCurve("Curve25519");
        
        CmRequestdhPublicKey cmRequestdhPublicKey = new CmRequestdhPublicKey();

        System.out.println("\nGenerating Keys");
        
        KeyGenerator keyGenerator = new KeyGenerator();
        
        KeyMaterial receiverKeys = keyGenerator.generate();

        String publicKey = receiverKeys.getPublicKey();

        System.out.println("Public Key - "+publicKey);
        //String privateKey = receiverKeys.getPrivateKey();

        String nonce = receiverKeys.getNonce();

        System.out.println("Nonce - "+nonce);

        cmRequestdhPublicKey.setExpiry("2023-04-15T12:52:34.925");  //HardCoded for Now
        cmRequestdhPublicKey.setKeyValue(publicKey);
        cmRequestdhPublicKey.setParameters("Curve25519/32byte random key");

        cmRequestKeyMaterial.setDhPublicKey(cmRequestdhPublicKey);
        cmRequestKeyMaterial.setNonce(nonce);

        hiRequest.setKeyMaterial(cmRequestKeyMaterial);

        cmRequest.setHiRequest(hiRequest);

        token =fetchModeController.getsession();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.add("X-CM-ID","sbx");
        headers.add("Authorization",token);

        String curr_body=new ObjectMapper().writeValueAsString(cmRequest);
        HttpEntity<String> httpEntity = new HttpEntity<>(curr_body, headers);
        
        System.out.println("Sending cm Request (HIU)");

        ResponseEntity<Object> objectResponseEntity=restTemplate.exchange("https://dev.abdm.gov.in/gateway/v0.5/health-information/cm/request", HttpMethod.POST, httpEntity,Object.class);
            
        System.out.println(objectResponseEntity.getStatusCode());

    }


    @PostMapping("/v0.5/health-information/hiu/on-request")
    public void onRequestHIU(@RequestBody OnRequest onRequest){

        System.out.println("Received HIU On Request");

    }


    @PostMapping("/v0.5/health-information/hip/request")
    public void cmHIPRequest(@RequestBody HIPRequest hipRequest) throws JsonProcessingException{

        System.out.println("-----Received HIP Request------");

        System.out.println(hipRequest.getTransactionId());
        System.out.println(hipRequest.getHiRequest().getDataPushUrl());

        token =fetchModeController.getsession();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.add("X-CM-ID","sbx");
        headers.add("Authorization",token);

        OnRequest onRequest = new OnRequest();

        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        onRequest.setRequestId(randomUUIDString);

        TimeZone timeZone=TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS");
        dateFormat.setTimeZone(timeZone);
        String asISO= dateFormat.format(new Date());
        onRequest.setTimestamp(asISO);

        String txnID = hipRequest.getTransactionId();

        OnRequesthiRequest onRequesthiRequest = new OnRequesthiRequest();
        onRequesthiRequest.setSessionStatus("ACKNOWLEDGED");
        onRequesthiRequest.setTransactionId(txnID);

        onRequest.setHiRequest(onRequesthiRequest);

        onRequest.setError(null); //Check Really needed or not. Or else remove

        String reqID = hipRequest.getRequestId();
        
        Resp resp = new Resp();
        resp.setRequestId(reqID);

        onRequest.setResp(resp);

        String curr_body=new ObjectMapper().writeValueAsString(onRequest);
        HttpEntity<String> httpEntity = new HttpEntity<>(curr_body, headers);
        
        System.out.println("Sending On Request (HIP)");

        ResponseEntity<Object> objectResponseEntity=restTemplate.exchange("https://dev.abdm.gov.in/gateway/v0.5/health-information/hip/on-request", HttpMethod.POST, httpEntity,Object.class);
            
        System.out.println(objectResponseEntity.getStatusCode());

        System.out.println("Sending Data ......");

    }


}
