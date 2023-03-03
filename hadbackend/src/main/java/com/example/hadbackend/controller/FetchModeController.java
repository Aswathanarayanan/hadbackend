package com.example.hadbackend.controller;

import com.example.hadbackend.bean.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@RestController
public class FetchModeController {


    WebClient webClient=WebClient.create();
    String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJBbFJiNVdDbThUbTlFSl9JZk85ejA2ajlvQ3Y1MXBLS0ZrbkdiX1RCdkswIn0.eyJleHAiOjE2Nzc4Njk0OTgsImlhdCI6MTY3Nzg2ODg5OCwianRpIjoiZWQ1ZWMwZjctN2E5ZS00MDg1LWI3NjEtNjQyODM1MTVkYWQxIiwiaXNzIjoiaHR0cHM6Ly9kZXYubmRobS5nb3YuaW4vYXV0aC9yZWFsbXMvY2VudHJhbC1yZWdpc3RyeSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJmYmMzNjA5ZC0yNjAzLTQ4MjUtYjcxMi1hMzk5MTVlYTI0OTEiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJTQlhfMDAyNzU3Iiwic2Vzc2lvbl9zdGF0ZSI6ImQxNzM5ZDBiLWRjY2YtNGZmMC1iZDczLWE4ZjcxMzA4MjhhNSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo5MDA3Il0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJoaXUiLCJvZmZsaW5lX2FjY2VzcyIsImhlYWx0aElkIiwiT0lEQyIsImhpcCJdfSwicmVzb3VyY2VfYWNjZXNzIjp7IlNCWF8wMDI3NTciOnsicm9sZXMiOlsidW1hX3Byb3RlY3Rpb24iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJjbGllbnRIb3N0IjoiMTAuMjMzLjY3LjIzNyIsImNsaWVudElkIjoiU0JYXzAwMjc1NyIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoic2VydmljZS1hY2NvdW50LXNieF8wMDI3NTciLCJjbGllbnRBZGRyZXNzIjoiMTAuMjMzLjY3LjIzNyJ9.f5XRujNjojlLbX34LSBC_L52XQKFbwrltkCzSyqiKLJuoxLD6H3oHrkVr96ICpyVJLdPQ7_6-_NkoAgG95q3mNUK0tRI9OHGUt9wEbkOp-_RFv1FKXwNXY8pWceO7AzShDWz4pTwS98ciQFH36Ro444WLcqsUX-4VGYxDr6yMMNlHgppdCu2uUGYrxMIIj9cjHxmiBYoEbJWRSyfot9FhTKBqLZoV76DTWO9yrZ3o-gsnXZpIFE4AgXAIoiAUlOZlJ351hWuhZIoRfeBr8v_mPd9jN5MXA9PjDZDwdsVEV4CaZevqMrfcftt_zAltN0y01bYZpfoG2JNQBLV6vCMbQ";
    //Callback
    String abhaid;

    String transactionid;

    OnConfirmPatient patient;
    @PostMapping("/v0.5/users/auth/on-fetch-modes")
    @CrossOrigin(origins = "*")
    public void Fetchcallback(@RequestBody FetchMode root){
        System.out.println(root.getRequestId());
    }

    @PostMapping("/v0.5/users/auth/on-init")
    public void Initcallback(@RequestBody OnInitResponse root){
        System.out.println(root.getInitAuth().getTransactionid());
        transactionid=root.getInitAuth().getTransactionid();
        System.out.println("hi");
    }

    @PostMapping("/v0.5/users/auth/on-confirm")
    public void Confirmcallback(@RequestBody OnConfirmResponse root){
        System.out.println(root.getRequestid());
        System.out.println(root.getAuth().getPatient().getName());
        System.out.println(root.getAuth().getPatient().getAddress().getState());
        patient=root.getAuth().getPatient();
        //System.out.println(root.getAuth().getPatient().getIdentifiersArrayList().get(0).getValue());
    }


    //actual api

    @PostMapping("/session")
    public void getsession(){
        SessionRequest sessiontoken=new SessionRequest();
        sessiontoken.setClientId("SBX_002757");
        sessiontoken.setClientsecret("e5744b75-8089-47bf-a810-8bf604c39939");

        SessionResponse token_res = (SessionResponse) webClient.post()
                .uri("https://dev.abdm.gov.in/gateway/v0.5/sessions")
                .body(Mono.just(sessiontoken),SessionRequest.class)
                .retrieve().bodyToMono(Object.class).block();
    }

    @PostMapping("/fetchdetails")
    public Mono<Object> fetchmodeABHA(@RequestParam String id) {


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
        requester.setId("demo-hip-6");
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

        Mono<Object> res1=OTP();
        System.out.println("outofinit");
        return res1;
    }

   // ,@RequestParam String id

    @PostMapping("/fetch")
    public Mono<Object> fetchmodeABHA(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody FetchModeRequest fetchModeRequest1) {

        System.out.println(fetchModeRequest1.getRequestId());
        System.out.println(fetchModeRequest1.getTimestamp());
        System.out.println(fetchModeRequest1.getQuery().getId());
        System.out.println(fetchModeRequest1.getQuery().getPurpose());
        System.out.println(fetchModeRequest1.getQuery().getRequester().getId());
        System.out.println(fetchModeRequest1.getQuery().getRequester().getType());
        System.out.println(token);


        Mono<Object> res = webClient.post()
                .uri("https://dev.abdm.gov.in/gateway/v0.5/users/auth/fetch-modes")
                .header(HttpHeaders.AUTHORIZATION,token)
                .header("X-CM-ID","sbx")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(fetchModeRequest1),FetchModeRequest.class)
                .retrieve().bodyToMono(Object.class);
        return res;
    }

    //@PostMapping("/otp")
    public Mono<Object> OTP(){

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
        initRequestQuery.setAuthMode("MOBILE_OTP");

        Requester requester=new Requester();
        requester.setType("HIP");
        requester.setId("demo-hip-6");

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
                .bodyToMono(Object.class);
        return res;
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
        return patient;
    }
//    public void fectchmodabdm(@RequestBody FetchModeRequest root){
//    }.
}
