package com.example.hadbackend.controller;

import com.example.hadbackend.DAOimplement.LoginRepository;
import com.example.hadbackend.DAOimplement.MedicalData;
import com.example.hadbackend.DAOimplement.PatientRepository;
import com.example.hadbackend.HadbackendApplication;
import com.example.hadbackend.bean.*;
import com.example.hadbackend.bean.carecontext.*;
import com.example.hadbackend.service.GetPatientDetails;
import com.example.hadbackend.service.InitAuthService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.http.parser.Authorization;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
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

    OnConfirmResponse onConfirmResponse;

    HadbackendApplication hadbackendApplication;

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
        onConfirmResponse=root;
        patient=root.getAuth().getPatient();
        System.out.println("Onconform complete");
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

        //getsession();
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
            return patient;
    }


    @Autowired
    LoginRepository loginRepository;
    @PostMapping("/adduser")
    public void addUser(@RequestBody Login login){
        loginRepository.save(login);
    }

    @PostMapping("/login")
    public void loginUser(@RequestBody Login login){
        System.out.println("login");
        Login l=loginRepository.findAllByEmailAndPasswordAndRole(login.getEmail(),login.getPassword(),login.getRole());
        //System.out.println(l.getRole());
        if(l==null)
            System.out.println("ivalid login");
        else
            System.out.println(l.getRole());

    }

    @Autowired
    MedicalData medicalData;

    @Autowired
    PatientRepository patientRepository;



    @PostMapping("/savedata")
    public void saveData(@RequestParam String email , @RequestParam String abhaid, @RequestBody Medicalrecords medicalrecords) {
        Login doctor = loginRepository.findAllByEmail(email);
        Patient patient = patientRepository.findPatientsById(abhaid);
        System.out.println(doctor.getEmail());
        System.out.println(patient.getId());
        System.out.println(patient.getPatientid());
        medicalrecords.setDoctor(doctor);
        medicalrecords.setPatient(patient);
        medicalrecords.setVistid(patient.getVisitid());
        medicalData.save(medicalrecords);

        // medicalrecords=

        // fetchModeController.initAuthService()
    }
    @PostMapping("/carecontext")
    public void carecontext(){

        //String token1="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJBbFJiNVdDbThUbTlFSl9JZk85ejA2ajlvQ3Y1MXBLS0ZrbkdiX1RCdkswIn0.eyJleHAiOjE2ODAxNDUwNjcsImlhdCI6MTY4MDE0NDQ2NywianRpIjoiMjIwNWUwZjEtYzg1Yy00ZDNjLTg1Y2ItYTFiZmUxNTYyNzQ2IiwiaXNzIjoiaHR0cHM6Ly9kZXYubmRobS5nb3YuaW4vYXV0aC9yZWFsbXMvY2VudHJhbC1yZWdpc3RyeSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5M2JlMjdmNS1jNDhhLTQwY2MtODQxZC03OGVmYzhhMWNhMDciLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJTQlhfMDAyODU5Iiwic2Vzc2lvbl9zdGF0ZSI6IjYzZDk4Njc2LWM3NmEtNDUxYi05MWYzLTc1NzA1N2VjYjIyMSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo5MDA3Il0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJoaXUiLCJvZmZsaW5lX2FjY2VzcyIsImhlYWx0aElkIiwiT0lEQyIsImhpcCJdfSwicmVzb3VyY2VfYWNjZXNzIjp7IlNCWF8wMDI4NTkiOnsicm9sZXMiOlsidW1hX3Byb3RlY3Rpb24iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJjbGllbnRJZCI6IlNCWF8wMDI4NTkiLCJjbGllbnRIb3N0IjoiMTAuMjMzLjY5LjI0NyIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoic2VydmljZS1hY2NvdW50LXNieF8wMDI4NTkiLCJjbGllbnRBZGRyZXNzIjoiMTAuMjMzLjY5LjI0NyJ9.D6f4VJKNbT6eNe_-X_VjIaUANnebkhn48ORmXFhg3ZDfrS-bdlW2Y0zkBSk3mhJFC5eWFkV-BSZA6E2qZbT1saXlAomN2oPFrVXpjI3Obemgf0lmmDXnpx2Noi7dAmsVBMQcDEDkWFlALhLA6ih-tBlaqjfV3Wgf-SYv7s-vWaaJmwo-5c8VPcn8Y86V02HRTFg1bzc9LurUe0_5U7XNe47dwokceSkXooXTO6Y5uUrecrUsPWlQNcUAd-8u_1TXZWbHAQN7cqXKvR2RgaoM_pdSgD0USnBQTSekdmGowEz-aQgIjaPh-NIFNSrIynVHBIEcEbZurNLN-rRHs7TjKg";
        AddContextRequest addContextRequest=new AddContextRequest();

        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        addContextRequest.setRequestId(randomUUIDString);

        TimeZone timeZone=TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS");
        dateFormat.setTimeZone(timeZone);
        String asISO= dateFormat.format(new Date());
        addContextRequest.setTimestamp(asISO);

        //link
        AddContectLinkRequest addContectLinkRequest=new AddContectLinkRequest();
        addContectLinkRequest.setAccessToken(onConfirmResponse.getAuth().getAccessToken());
        //addContectLinkRequest.setAccessToken(token1);

        //link-->patient
        AddContextPatientRequest addContextPatientRequest=new AddContextPatientRequest();

        addContextPatientRequest.setReferenceNumber("1esf");
        addContextPatientRequest.setDisplay("Aswatha Narayanan");


        //link-->patient-->carecontext
        AddContextcareContextsRequest addContextcareContextsRequest=new AddContextcareContextsRequest();
        addContextcareContextsRequest.setReferenceNumber("20230329170723000142");
        String displaymessage= "Consluted by " + asISO;
        addContextcareContextsRequest.setDisplay(displaymessage);

        //patient<--carecontext
        ArrayList<AddContextcareContextsRequest> listcarecontext=new ArrayList<>();
        listcarecontext.add(addContextcareContextsRequest);
        addContextPatientRequest.setCareContexts(listcarecontext);

        //link<--patient
        addContectLinkRequest.setPatient(addContextPatientRequest);
        //requestbody<--link
        addContextRequest.setLink(addContectLinkRequest);

        System.out.println(addContextRequest.getRequestId());
        System.out.println(addContextRequest.getTimestamp());
        //link
        System.out.println(addContextRequest.getLink().getAccessToken());
        //patient
        System.out.println(addContextRequest.getLink().getPatient().getReferenceNumber());
        System.out.println(addContextRequest.getLink().getPatient().getDisplay());
        //carecontexts
        System.out.println(addContextRequest.getLink().getPatient().getCareContexts().get(0).getReferenceNumber());
        System.out.println(addContextRequest.getLink().getPatient().getCareContexts().get(0).getDisplay());

        Mono<Object> res = webClient.post()
                .uri("https://dev.abdm.gov.in/gateway/v0.5/links/link/add-contexts")
                .header(HttpHeaders.AUTHORIZATION,token)
                .header("X-CM-ID","sbx")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(addContextRequest), AddContextRequest.class)
                .retrieve().bodyToMono(Object.class);
        if(res==null)
            System.out.println("callbackfalied");
        else
            System.out.println("callbacksuccess");
        System.out.println("addded care context");
        //return res;
    }

    @PostMapping("/v0.5/consents/hip/notify")
    public void Notify(NotifyResponse root){

    }
}
