package com.example.hadbackend.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.hadbackend.DAOimplement.ConsentRepository;
import com.example.hadbackend.DAOimplement.HIPConsentRepository;
import com.example.hadbackend.DAOimplement.MedicalData;
import com.example.hadbackend.DAOimplement.PatientRepository;
import com.example.hadbackend.bean.auth.Resp;
import com.example.hadbackend.bean.consent.HIPConsentTable;
import com.example.hadbackend.bean.consent.HIUConsentTable;
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
import com.example.hadbackend.bean.carecontext.Medicalrecords;
import com.example.hadbackend.bean.carecontext.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RestController
public class RequestController {
    
    @Autowired
    ConsentRepository consentRepository;

    @Autowired
    HIPConsentRepository hipconsentRepository;

    @Autowired 
    PatientRepository patientRepository;

    @Autowired
    MedicalData medicalData;

    FetchModeController fetchModeController=new FetchModeController();

    String token;

    int flag=0;
    
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

        String expDate = onFetchConsent.getConsent().getConsentDetail().getPermission().getDataEraseAt();
        String fromDate = onFetchConsent.getConsent().getConsentDetail().getPermission().getDateRange().getFrom();
        String toDate = onFetchConsent.getConsent().getConsentDetail().getPermission().getDateRange().getTo();
      
        HIUConsentTable consentTable = new HIUConsentTable();

        String strPattern = "\\d{4}-\\d{2}-\\d{2}";
         
        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(expDate);
         
        Date date = new Date();
 
        while(matcher.find()){
            System.out.println(matcher.group());
            date = new SimpleDateFormat("yyyy-MM-dd").parse(matcher.group());
        }

        consentTable.setExpiryDate(date);

        matcher = pattern.matcher(fromDate);
         
        while(matcher.find()) {
            System.out.println(matcher.group());
            date = new SimpleDateFormat("yyyy-MM-dd").parse(matcher.group());
        }
        
        consentTable.setDateFrom(date);

        matcher = pattern.matcher(toDate);
         
        while(matcher.find()) {
            System.out.println(matcher.group());
            date = new SimpleDateFormat("yyyy-MM-dd").parse(matcher.group());
        }
        
        consentTable.setDateTo(date);

        consentTable.setAbhaid(onFetchConsent.getConsent().getConsentDetail().getPatient().getId());
        consentTable.setConsentId(consentID);
        
        consentRepository.save(consentTable);

        System.out.println("Consent Artifact saved in HIU table DB");

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

        if(flag==0){
            flag=1;
            String consent = hipRequest.getHiRequest().getConsent().getId();
            
            List<HIPConsentTable> consentTable = hipconsentRepository.findAllByConsentId(consent);

            for(int i=0;i<consentTable.size();i++)
            {
                HIPConsentTable row = consentTable.get(i);
                
                String abhaid = row.getAbhaid();

                Patient patient = patientRepository.findPatientsById(abhaid);

                List<Medicalrecords> listData = medicalData.findAllByPatientAndDateBetween(patient, row.getDateFrom(), row.getDateTo());
                
                for(int j=0;j<listData.size();j++)
                {
                    Medicalrecords cur = listData.get(i);
                    System.out.println(cur.getPrescription());
                }

                System.out.println("IN Data");
            }
        }

        flag=0;

    }

    //Test
    @PostMapping("/saveConsent")
    public void saveConsent() throws ParseException{
        String str = "2020-10-06T10:50:37.764Z";
        
        String strPattern = "\\d{4}-\\d{2}-\\d{2}";
        
        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(str);
        
        Date date = new Date();

        while( matcher.find() ) {
            System.out.println( matcher.group());
            date = new SimpleDateFormat("yyyy-MM-dd").parse(matcher.group());
        }
        // Date date = new SimpleDateFormat("yyyy-MM-dd").parse(matcher.group(1));

        HIUConsentTable consentTable = new HIUConsentTable();
        consentTable.setAbhaid("ashish");
        consentTable.setConsentId("1234");
        consentTable.setExpiryDate(date);

        consentRepository.save(consentTable);

    }   

    @PostMapping("/deleteConsents")
    public void deleteConsents() throws ParseException{
        
         //consentRepository.deleteByID(consentRepository.);
        
    }   

}
