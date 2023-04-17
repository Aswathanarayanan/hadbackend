package com.example.hadbackend.controller;

import com.example.hadbackend.DAOimplement.LoginRepository;
import com.example.hadbackend.DAOimplement.MedicalData;
import com.example.hadbackend.DAOimplement.PatientRepository;
import com.example.hadbackend.HadbackendApplication;
import com.example.hadbackend.bean.auth.*;
import com.example.hadbackend.bean.carecontext.*;
import com.example.hadbackend.service.GetPatientDetails;
import com.example.hadbackend.service.InitAuthService;
import com.example.hadbackend.service.carecontext.Appoinment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@RestController
public class FetchModeController {


    WebClient webClient=WebClient.create();
    public String token;//="Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJBbFJiNVdDbThUbTlFSl9JZk85ejA2ajlvQ3Y1MXBLS0ZrbkdiX1RCdkswIn0.eyJleHAiOjE2ODAyMDExMjksImlhdCI6MTY4MDIwMDUyOSwianRpIjoiYWFjMGE2NGItYzFjZi00ZTc5LWFhODQtZGJiZmYyYTFkYjBmIiwiaXNzIjoiaHR0cHM6Ly9kZXYubmRobS5nb3YuaW4vYXV0aC9yZWFsbXMvY2VudHJhbC1yZWdpc3RyeSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI5M2JlMjdmNS1jNDhhLTQwY2MtODQxZC03OGVmYzhhMWNhMDciLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJTQlhfMDAyODU5Iiwic2Vzc2lvbl9zdGF0ZSI6IjRiODdmYmMwLTRjMWItNDIwZS1hYWEyLWQ1ODFlY2Y0OWFmYiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo5MDA3Il0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJoaXUiLCJvZmZsaW5lX2FjY2VzcyIsImhlYWx0aElkIiwiT0lEQyIsImhpcCJdfSwicmVzb3VyY2VfYWNjZXNzIjp7IlNCWF8wMDI4NTkiOnsicm9sZXMiOlsidW1hX3Byb3RlY3Rpb24iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJjbGllbnRJZCI6IlNCWF8wMDI4NTkiLCJjbGllbnRIb3N0IjoiMTAuMjMzLjY5LjI0NyIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoic2VydmljZS1hY2NvdW50LXNieF8wMDI4NTkiLCJjbGllbnRBZGRyZXNzIjoiMTAuMjMzLjY5LjI0NyJ9.ExAytXrIptQWQmEW6BTAbGvMtV_UlY47zy1bQ50_kDbiyXTo7yrJSwovpFZCl-A7NKcYooGnfm2gpi2hhRac0Yn2Hk9VX-9EOEXQaWG9jK2R7QJPuMHgx6IwMNt7FL53W30AUHPVBuCgCPSS430DcFU4YXw0yohnrHn9WK7KGY9ZEuL-AnAJW-_vaHxCSUa__dflY6uL-9htq-NTbhg4_PnnQZU2rO6sdqcbKqlga6qxqQdDBVa2Phk39Lg-FsqxtuONm24eIQSirZEJQf_uV6aG_Ay2A22A4LgJXgAJmhT3opxrFBU0PhIuZPXCdwxPApP04E9h8rO-4HLd9KAUxw";
    //Callback
    String abhaid;

    String accesstoken;
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
        accesstoken=root.getAuth().getAccessToken();
        patient=root.getAuth().getPatient();
        System.out.println("Onconform complete");
        //System.out.println(root.getAuth().getPatient().getIdentifiersArrayList().get(0).getValue());
    }


    //actual api

    @PostMapping("/session")
    public String getsession(){
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
        return "Bearer "+sessionResponse.getAccesstoken();
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
    public Mono<Object> confirmAuthdemo(@RequestParam String abhaid) {

        Patient patient = patientRepository.findPatientsById(abhaid);
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
        Demographic demographic=new Demographic();

        demographic.setName(patient.getName());
        demographic.setGender(patient.getGender());
        String s=patient.getYear()+"-"+patient.getMonth()+"-"+patient.getDay();
        demographic.setDateOfBirth(s);

        PatientIdentifiers patientIdentifier=new PatientIdentifiers();
        patientIdentifier.setValue(String.valueOf(patient.getMobile()));
        patientIdentifier.setType("MOBILE");

        demographic.setPatientIdentifiers(patientIdentifier);
        credential.setDemographic(demographic);

        confirmRequest.setCredential(credential);

        System.out.println(transactionid);
        System.out.println(credential.getDemographic().getDateOfBirth());

        Mono<Object> res = webClient.post()
                .uri("https://dev.abdm.gov.in/gateway/v0.5/users/auth/confirm")
                .header(HttpHeaders.AUTHORIZATION, token)
                .header("X-CM-ID", "sbx")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(confirmRequest), OnConfirmRequest.class)
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
    public String loginUser(@RequestBody Login login){
        System.out.println("login");
        Login l=loginRepository.findAllByEmailAndPasswordAndRole(login.getEmail(),login.getPassword(),login.getRole());
        //System.out.println(l.getRole());
        if(l!=null)
            return "Success";
        return "Failure";
    }

    @Autowired
    MedicalData medicalData;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    Appoinment appoinment;

    @GetMapping("/exisitingpatient")
    public Patient patientappointment(@RequestParam String abhaid){
        Patient patient = patientRepository.findPatientsById(abhaid);
        this.abhaid=abhaid;
        getsession();
        return patientRepository.findPatientsById(abhaid);
    }
    @PostMapping("/updateexisitingpatient")
    public void patientupdateappointment(@RequestParam String abhaid,@RequestBody Patient p){
        appoinment.addappoinment(abhaid,p);
    }

    @PostMapping("/savedata")
    public void saveData(@RequestParam String email , @RequestParam String abhaid, @RequestBody Medicalrecords medicalrecords) throws JsonProcessingException {
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

        // fetchModeController.initAuthService(

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
        Link addContectLinkRequest=new Link();
        addContectLinkRequest.setAccessToken(accesstoken);
        //addContectLinkRequest.setAccessToken(token1);

        //link-->patient
        AddContextPatientRequest addContextPatientRequest=new AddContextPatientRequest();

        addContextPatientRequest.setReferenceNumber("PUID-"+patient.getPatientid());
        addContextPatientRequest.setDisplay(patient.getName());


        //link-->patient-->carecontext
        AddContextcareContextsRequest addContextcareContextsRequest=new AddContextcareContextsRequest();
        addContextcareContextsRequest.setReferenceNumber(patient.getVisitid());
        String displaymessage= "Consluted by "+asISO;
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

        System.out.println("Bearer "+token);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.add("X-CM-ID","sbx");
        headers.add("Authorization", token);

        String curr_body=new ObjectMapper().writeValueAsString(addContextRequest);
        HttpEntity<String> httpEntity = new HttpEntity<>(curr_body, headers);

        ResponseEntity<Object> objectResponseEntity=restTemplate.exchange("https://dev.abdm.gov.in/gateway/v0.5/links/link/add-contexts", HttpMethod.POST, httpEntity,Object.class);

        System.out.println("addded care context");
    }

    @PostMapping("/v0.5/links/link/on-add-contexts")
    public void onCareContext(@RequestBody AddContextResponse response){
        System.out.println(response.getRequestId());
        System.out.println(response.getTimestamp());
        System.out.println(response.getAcknowledgement().getStatus());
//        if(response.getError()!=null){
//            System.out.println();
//        }
        System.out.println(response.getResp().getRequestId());
        System.out.println("ON-Care-Context-callback");
    }

    @PostMapping("/carecontext")
    public void carecontext(){

        //String token1="eyJhbGciOiJSUzUxMiJ9hdGhhbmFyYXlhbmFuc0BzYngiLCJyZXF1ZXN0ZXJUeXBlIjoiSElQIiwicmVxdWVzdGVySWQiOiJpaWl0YnRlYW0xOCIsInBhdGllbnRJZCI6ImFzd2F0aGFuYXJheWFuYW5zQHNieCIsInNlc3Npb25JZCI6IjA0ZDUzNWEyLTNhNmMtNDg2NC1hNDU5LWU4YzllNWI2ZmIyNCIsImV4cCI6MTY4MDI4MDY0NSwiaWF0IjoxNjgwMTk0MjQ1fQ.IN_M177NqtC-UeeUbKzHFTCOwquL_mWWvCkERp7m2-TI75w9CKRhhayBUOXlDf-57GkCdbBtPCgmN0QoZ12qzflCQBXb4CXCpaOLx4HT5lGmeTJgDOqQEst5-uJg4tYdvIDG2xn_6H4oNUFhObMqY63JpUkgh-F2enZv0HAixH6gb4KdGXw9TO243m4JG7rdBnUnqTJe5pUVisT-oGGQzM4xGwMlUyp1RF8bfzJ74UbgABgPwRGnlqtsCyqFM3_HqZFda4BrfGfSZbtsTuSDSXlZIYXkJRFEAQKdB9NbypJACoEQ8KFBKULIFmCGijB0wC0QoufZ1YKnHj43uO42TA";
        getsession();
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
        Link addContectLinkRequest=new Link();
        addContectLinkRequest.setAccessToken(accesstoken);
        //addContectLinkRequest.setAccessToken(token1);

        //link-->patient
        AddContextPatientRequest addContextPatientRequest=new AddContextPatientRequest();

        addContextPatientRequest.setReferenceNumber("3223e");
        addContextPatientRequest.setDisplay("Aswath");


        //link-->patient-->carecontext
        AddContextcareContextsRequest addContextcareContextsRequest=new AddContextcareContextsRequest();
        addContextcareContextsRequest.setReferenceNumber("20200asd");
        String displaymessage= "Consluted by ";
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

        Mono<ClientResponse> res = webClient.post()
                .uri("https://dev.abdm.gov.in/gateway/v0.5/links/link/add-contexts")
                .header(HttpHeaders.AUTHORIZATION,token)
                .header("X-CM-ID","sbx")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(addContextRequest), AddContextRequest.class)
                .exchange();
        if(res==null)
            System.out.println("callbackfalied");
        else
            System.out.println("callbacksuccess");
        System.out.println("addded care context");
        //return res;
    }
    @PostMapping("/carecontext1")
    public void carecontext1() throws JsonProcessingException {

        //String token1="eyJhbGciOiJSUzUxMiJ9hdGhhbmFyYXlhbmFuc0BzYngiLCJyZXF1ZXN0ZXJUeXBlIjoiSElQIiwicmVxdWVzdGVySWQiOiJpaWl0YnRlYW0xOCIsInBhdGllbnRJZCI6ImFzd2F0aGFuYXJheWFuYW5zQHNieCIsInNlc3Npb25JZCI6IjA0ZDUzNWEyLTNhNmMtNDg2NC1hNDU5LWU4YzllNWI2ZmIyNCIsImV4cCI6MTY4MDI4MDY0NSwiaWF0IjoxNjgwMTk0MjQ1fQ.IN_M177NqtC-UeeUbKzHFTCOwquL_mWWvCkERp7m2-TI75w9CKRhhayBUOXlDf-57GkCdbBtPCgmN0QoZ12qzflCQBXb4CXCpaOLx4HT5lGmeTJgDOqQEst5-uJg4tYdvIDG2xn_6H4oNUFhObMqY63JpUkgh-F2enZv0HAixH6gb4KdGXw9TO243m4JG7rdBnUnqTJe5pUVisT-oGGQzM4xGwMlUyp1RF8bfzJ74UbgABgPwRGnlqtsCyqFM3_HqZFda4BrfGfSZbtsTuSDSXlZIYXkJRFEAQKdB9NbypJACoEQ8KFBKULIFmCGijB0wC0QoufZ1YKnHj43uO42TA";
        //getsession();
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
        Link addContectLinkRequest=new Link();
        addContectLinkRequest.setAccessToken(accesstoken);
        //addContectLinkRequest.setAccessToken(token1);

        //link-->patient
        AddContextPatientRequest addContextPatientRequest=new AddContextPatientRequest();

        addContextPatientRequest.setReferenceNumber("3223e");
        addContextPatientRequest.setDisplay("Aswath");


        //link-->patient-->carecontext
        AddContextcareContextsRequest addContextcareContextsRequest=new AddContextcareContextsRequest();
        addContextcareContextsRequest.setReferenceNumber("20200asd");
        String displaymessage= "Consluted by ";
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

        System.out.println("Bearer "+token);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.add("X-CM-ID","sbx");
        headers.add("Authorization", token);

        String curr_body=new ObjectMapper().writeValueAsString(addContextRequest);
        HttpEntity<String> httpEntity = new HttpEntity<>(curr_body, headers);

        ResponseEntity<Object> objectResponseEntity=restTemplate.exchange("https://dev.abdm.gov.in/gateway/v0.5/links/link/add-contexts", HttpMethod.POST, httpEntity,Object.class);

        System.out.println("addded care context");
        //return res;
    }
}
