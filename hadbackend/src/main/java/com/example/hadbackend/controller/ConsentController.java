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

import com.example.hadbackend.bean.consent.Consent;
import com.example.hadbackend.bean.consent.ConsentDateRange;
import com.example.hadbackend.bean.consent.ConsentFrequency;
import com.example.hadbackend.bean.consent.ConsentHIU;
import com.example.hadbackend.bean.consent.ConsentPatient;
import com.example.hadbackend.bean.consent.ConsentPermission;
import com.example.hadbackend.bean.consent.ConsentPurpose;
import com.example.hadbackend.bean.consent.ConsentRequest;
import com.example.hadbackend.bean.consent.ConsentRequestFromFrontend;
import com.example.hadbackend.bean.consent.ConsentRequester;
import com.example.hadbackend.bean.consent.ConsentRequesterIdentifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.client.RestTemplate;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RestController
public class ConsentController {
  
    FetchModeController fetchModeController=new FetchModeController();

    String token;

    @PostMapping("/generateconsent")
    public void generateConsent(@RequestBody ConsentRequestFromFrontend consentRequestFromFrontend) throws JsonProcessingException{

            token =fetchModeController.getsession();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.ALL));
            headers.add("X-CM-ID","sbx");
            headers.add("Authorization",token);

            System.out.println(consentRequestFromFrontend.getAbhaid());
            
            Consent consent = new Consent();

            ConsentRequest consentRequest= new ConsentRequest();

            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();
            consentRequest.setRequestId(randomUUIDString);

            TimeZone timeZone=TimeZone.getTimeZone("UTC");
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS");
            dateFormat.setTimeZone(timeZone);
            String asISO= dateFormat.format(new Date());
            consentRequest.setTimestamp(asISO);

            ConsentPurpose consentpurpose = new ConsentPurpose();
            consentpurpose.setText(consentRequestFromFrontend.getPurpose());
            consentpurpose.setCode("CAREMGT");
        
            consent.setPurpose(consentpurpose);

            ConsentPatient consentPatient=new ConsentPatient();
            consentPatient.setId(consentRequestFromFrontend.getAbhaid());
            
            consent.setPatient(consentPatient);

            ConsentHIU consentHIU = new ConsentHIU();
            consentHIU.setId("ashish-hiu-1");

            consent.setHiu(consentHIU);

            ConsentRequester consentRequester=new ConsentRequester();
            consentRequester.setName("DR. Ashish");

            ConsentRequesterIdentifier consentRequesterIdentifier=new ConsentRequesterIdentifier();
            consentRequesterIdentifier.setType("HIU");
            consentRequesterIdentifier.setValue("Ashish IIITB HIU");
            consentRequesterIdentifier.setSystem("");

            consentRequester.setIdentifier(consentRequesterIdentifier);
            
            consent.setRequester(consentRequester);

            ArrayList<String> listHIU = new ArrayList<>();
            listHIU.add("OPConsultation");
            consent.setHiTypes(listHIU);
            
            ConsentPermission consentPermission=new ConsentPermission();
            consentPermission.setAccessMode("VIEW");
            
            ConsentDateRange consentDateRange=new ConsentDateRange();
            
            consentDateRange.setFrom(consentRequestFromFrontend.getDateFrom()+"T12:52:34.925Z");
            consentDateRange.setTo(consentRequestFromFrontend.getDateTo()+"T12:52:34.925Z");
            
            consentPermission.setDateRange(consentDateRange);

            consentPermission.setDataEraseAt(consentRequestFromFrontend.getExpirayDate()+"T12:52:34.925Z");
            
            ConsentFrequency consentFrequency=new ConsentFrequency();
            consentFrequency.setUnit("HOUR");
            consentFrequency.setValue(1);
            consentFrequency.setRepeats(0);;

            consentPermission.setFrequency(consentFrequency);

            consent.setPermission(consentPermission);

            consentRequest.setConsent(consent);
            
            String curr_body=new ObjectMapper().writeValueAsString(consentRequest);
            HttpEntity<String> httpEntity = new HttpEntity<>(curr_body, headers);
            
            ResponseEntity<Object> objectResponseEntity=restTemplate.exchange("https://dev.abdm.gov.in/gateway/v0.5/consent-requests/init", HttpMethod.POST, httpEntity,Object.class);
            
            System.out.println(objectResponseEntity.getStatusCode());

    }
}
