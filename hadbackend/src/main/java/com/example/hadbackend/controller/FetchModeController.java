package com.example.hadbackend.controller;

import com.example.hadbackend.bean.*;
import com.example.hadbackend.service.GetPatientDetails;
import com.example.hadbackend.service.InitAuthService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.parser.Authorization;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@RestController
public class FetchModeController {


    WebClient webClient=WebClient.create();
    public String token;
    //Callback
    String abhaid;

    String transactionid;

    SessionResponse sessionResponse;

    FetchMode fetchMode;
    public OnConfirmPatient patient;

    public InitAuthService initAuthService=new InitAuthService();
    public GetPatientDetails getPatientDetails=new GetPatientDetails();

    @PostMapping("/v0.5/users/auth/on-fetch-modes")
    @CrossOrigin(origins = "*")
    @Retryable(value=NullPointerException.class, maxAttempts = 3, backoff = @Backoff(value=2000))
    public void Fetchcallback(@RequestBody FetchMode root){
        System.out.println(root.getRequestId());
        System.out.println("on-fetch-modes callback");
        fetchMode=root;
    }

    @PostMapping("/v0.5/users/auth/on-init")
    @Retryable(value=NullPointerException.class, maxAttempts = 3, backoff = @Backoff(value=2000))
    public void Initcallback(@RequestBody OnInitResponse root){
//        System.out.println(root.getInitAuth().getTransactionid());
        transactionid=root.getInitAuth().getTransactionid();
        System.out.println("hi");
    }

    @PostMapping("/v0.5/users/auth/on-confirm")
    @Retryable(value=NullPointerException.class, maxAttempts = 3, backoff = @Backoff(value=2000))
    public void Confirmcallback(@RequestBody OnConfirmResponse root){
//        System.out.println(root.getRequestid());
//        System.out.println(root.getAuth().getPatient().getName());
//        System.out.println(root.getAuth().getPatient().getAddress().getState());
        patient=root.getAuth().getPatient();
        //System.out.println(root.getAuth().getPatient().getIdentifiersArrayList().get(0).getValue());
    }


    //actual api

    @PostMapping("/session")
    public void getsession(){
        SessionRequest sessiontoken=new SessionRequest();
        sessiontoken.setClientId("SBX_002859");
        sessiontoken.setClientsecret("7fd0134e-9311-41b5-a378-02d743f9f3b0");

        SessionResponse token_res = webClient.post()
                .uri("https://dev.abdm.gov.in/gateway/v0.5/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(sessiontoken),SessionRequest.class)
                .retrieve().bodyToMono(SessionResponse.class).block();
        sessionResponse = token_res;
        token="Bearer "+sessionResponse.getAccesstoken();
        //System.out.println(sessionResponse.getAccesstoken());
    }

    @PostMapping("/fetchdetails")
    public Mono<Object> fetchmodeABHA(@RequestParam String id) {
        getsession();
        abhaid=id;
        FetchModeRequest fetchModeRequest =new FetchModeRequest();
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        fetchModeRequest.setRequestId(randomUUIDString);

//        Instant instant = Instant.now();
//        Timestamp timestamp = Timestamp.from(instant);
        TimeZone timeZone=TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS");
        dateFormat.setTimeZone(timeZone);
        String asISO= dateFormat.format(new Date());
        fetchModeRequest.setTimestamp(asISO);

        Query newQuery=new Query();
        newQuery.setId(id);
        newQuery.setPurpose("KYC_AND_LINK");

        Requester requester=new Requester();
        requester.setId("iiitbteam18");
        requester.setType("HIP");

        newQuery.setRequester(requester);
        fetchModeRequest.setQuery(newQuery);


        System.out.println(fetchModeRequest.getRequestId());
        System.out.println(fetchModeRequest.getTimestamp());
        System.out.println(fetchModeRequest.getQuery().getId());
        System.out.println(fetchModeRequest.getQuery().getPurpose());
        System.out.println(fetchModeRequest.getQuery().getRequester().getId());
        System.out.println(fetchModeRequest.getQuery().getRequester().getType());
        System.out.println(token);

        Mono<Object> res = webClient.post()
                .uri("https://dev.abdm.gov.in/gateway/v0.5/users/auth/fetch-modes")
                .header(HttpHeaders.AUTHORIZATION,token)
                .header("X-CM-ID","sbx")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                 .body(Mono.just(fetchModeRequest),FetchModeRequest.class)
                .retrieve().bodyToMono(Object.class);
        return res;
    }

    @GetMapping("/getmodes")
    @Retryable(value=NullPointerException.class, maxAttempts = 3, backoff = @Backoff(value=2000))
    public List<String> getModes(){
        System.out.println("getmodes"+ fetchMode.getAuth().getModes().get(0));
        return fetchMode.getAuth().getModes();
    }


    @PostMapping("/otp")
    @Retryable(value= WebClientResponseException.class, maxAttempts = 3, backoff = @Backoff(value=2000))
    public Mono<Object> OTP(@RequestParam String mode){

        AuthInitRequest authInitRequest=new AuthInitRequest();
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        authInitRequest.setRequestId(randomUUIDString);

//        Instant instant = Instant.now();
//        Timestamp timestamp = Timestamp.from(instant);
        TimeZone timeZone=TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS");
        dateFormat.setTimeZone(timeZone);
        String asISO= dateFormat.format(new Date());
        authInitRequest.setTimestamp(asISO);


        InitRequestQuery initRequestQuery=new InitRequestQuery();
        initRequestQuery.setId(abhaid);
        initRequestQuery.setPurpose("KYC_AND_LINK");
        initRequestQuery.setAuthMode(mode);

        Requester requester=new Requester();
        requester.setType("HIP");
        requester.setId("iiitbteam18");

        initRequestQuery.setRequester(requester);

        authInitRequest.setQuery(initRequestQuery);

        System.out.println(authInitRequest.getRequestId());
        System.out.println(authInitRequest.getTimestamp());
        System.out.println(abhaid);

        Mono<Object> res = webClient.post()
                .uri("https://dev.abdm.gov.in/gateway/v0.5/users/auth/init")
                .header(HttpHeaders.AUTHORIZATION,token)
                .header("X-CM-ID","sbx")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(authInitRequest), AuthInitRequest.class)
                .retrieve()
                .bodyToMono(Object.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)));;

        System.out.println("INitcomplete");
        return res;
        //return initAuthService.initauthservice(authInitRequest);
    }

    @PostMapping("/init")
    public Mono<Object> initOTP(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody AuthInitRequest authInitRequest){
        Mono<Object> res = webClient.post()
                .uri("https://dev.abdm.gov.in/gateway/v0.5/users/auth/init")
                .header(HttpHeaders.AUTHORIZATION,token)
                .header("X-CM-ID","sbx")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(authInitRequest), AuthInitRequest.class)
                .retrieve()
                .bodyToMono(Object.class);
        return res;
    }


    @PostMapping("/confirmotp")
    @Retryable(value=NullPointerException.class, maxAttempts = 3, backoff = @Backoff(value=2000))
    public Mono<Object> confirmAuth(@RequestParam String authcode) {

        OnConfirmRequest confirmRequest=new OnConfirmRequest();
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        confirmRequest.setRequestid(randomUUIDString);

//        Instant instant = Instant.now();
//        Timestamp timestamp = Timestamp.from(instant);
        TimeZone timeZone=TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS");
        dateFormat.setTimeZone(timeZone);
        String asISO= dateFormat.format(new Date());
        confirmRequest.setTimestamp(asISO);

        confirmRequest.setTransactionid(transactionid);

        Credential credential=new Credential();
        credential.setAuthCode(authcode);

        confirmRequest.setCredential(credential);

        System.out.println(transactionid);
        System.out.println(authcode);

        Mono<Object> res = webClient.post()
                .uri("https://dev.abdm.gov.in/gateway/v0.5/users/auth/confirm")
                .header(HttpHeaders.AUTHORIZATION, token)
                .header("X-CM-ID", "sbx")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(confirmRequest), OnConfirmRequest.class)
                .retrieve().bodyToMono(Object.class);
        return res;
    }

    @PostMapping("/confirm")
    public Mono<Object> confirmAuth(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody OnConfirmRequest authInitRequest) {

        Mono<Object> res = webClient.post()
                .uri("https://dev.abdm.gov.in/gateway/v0.5/users/auth/confirm")
                .header(HttpHeaders.AUTHORIZATION, token)
                .header("X-CM-ID", "sbx")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(authInitRequest), OnConfirmRequest.class)
                .retrieve().bodyToMono(Object.class);
        return res;
    }

    @GetMapping("/getpatientdata")
    public OnConfirmPatient getPatientdata(){
//        System.out.println(patient.getName());
//        System.out.println(patient.getAddress().getState());
//        return patient;
            return getPatientDetails.getpatientdetails();
    }

}
