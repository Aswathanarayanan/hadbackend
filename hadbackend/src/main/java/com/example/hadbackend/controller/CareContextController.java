package com.example.hadbackend.controller;

import com.example.hadbackend.bean.carecontext.Patient;
import com.example.hadbackend.service.carecontext.Addpatient;
import com.example.hadbackend.service.carecontext.Appoinment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CareContextController {

    @Autowired
    Addpatient insertpatient;

    @Autowired
    Appoinment appoinment;

    @PostMapping("/savepatient")
    public void addPatient(@RequestBody Patient p){
        System.out.println("++++printing");
        System.out.println("aswath");
        p.setAppoinement(1);
        insertpatient.insertpatientdetails(p);
    }

    @GetMapping("/exisitingpatient")
    public void patientappointment(@RequestParam String abhaid){
        appoinment.addappoinment(abhaid);
    }

    @GetMapping("/listofpatient")
    public List<Patient> waitingpatient(){
        return appoinment.getpatientlist();
    }
}
