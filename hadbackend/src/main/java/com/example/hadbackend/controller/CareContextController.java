package com.example.hadbackend.controller;

import com.example.hadbackend.DAOimplement.LoginRepository;
import com.example.hadbackend.DAOimplement.MedicalData;
import com.example.hadbackend.DAOimplement.PatientRepository;
import com.example.hadbackend.HadbackendApplication;
import com.example.hadbackend.bean.FetchModeRequest;
import com.example.hadbackend.bean.Login;
import com.example.hadbackend.bean.SessionRequest;
import com.example.hadbackend.bean.SessionResponse;
import com.example.hadbackend.bean.carecontext.*;
import com.example.hadbackend.service.carecontext.Addpatient;
import com.example.hadbackend.service.carecontext.Appoinment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@Getter
@Setter
@RestController
public class CareContextController {


    WebClient webClient=WebClient.create();
    @Autowired
    Addpatient insertpatient;

    @Autowired
    Appoinment appoinment;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    PatientRepository patientRepository;

    @PostMapping("/savepatient")
    public void addPatient(@RequestParam Integer doctorid,@RequestBody Patient p){
        System.out.println("++++printing");
        System.out.println("aswath");
        Login l=loginRepository.findAllById(doctorid);
        p.setAppoinement(1);
        p.setDoctor(l);
        insertpatient.insertpatientdetails(p);
    }

    @GetMapping("/listofpatient")
    public List<Patient> waitingpatient(@RequestParam String email){
        return appoinment.getpatientlist(email);
    }

    @GetMapping("/exitappoinment")
    public void exitAppoinment(@RequestParam String abhaid){
        appoinment.endappointment(abhaid);
    }

    @Autowired
    MedicalData medicalData;

    FetchModeController fetchModeController;

    //HadbackendApplication hadbackendApplication;
    //String accesstoken= fetchModeController.getOnConfirmResponse().getAuth().getAccessToken();
    String token;

    SessionResponse sessionResponse=new SessionResponse();

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

    @GetMapping("/getdoctors")
    public List<Login> getDoctors(){
        return loginRepository.findAllByRole("doctor");
    }

    @PostMapping("/v0.5/consents/hip/notify")
    public void Notify(NotifyResponse root){

    }
}
