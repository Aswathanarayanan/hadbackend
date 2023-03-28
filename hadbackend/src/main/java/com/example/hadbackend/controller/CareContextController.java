package com.example.hadbackend.controller;

import com.example.hadbackend.DAOimplement.LoginRepository;
import com.example.hadbackend.DAOimplement.MedicalData;
import com.example.hadbackend.DAOimplement.PatientRepository;
import com.example.hadbackend.bean.Login;
import com.example.hadbackend.bean.carecontext.Medicalrecords;
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

    @Autowired
    LoginRepository loginRepository;

    @PostMapping("/savepatient")
    public void addPatient(@RequestParam Integer doctorid,@RequestBody Patient p){
        System.out.println("++++printing");
        System.out.println("aswath");
        Login l=loginRepository.findAllById(doctorid);
        p.setAppoinement(1);
        p.setDoctor(l);
        insertpatient.insertpatientdetails(p);
    }

    @GetMapping("/exisitingpatient")
    public void patientappointment(@RequestParam String abhaid){
        appoinment.addappoinment(abhaid);
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

    @Autowired
    PatientRepository patientRepository;
    @PostMapping("/savedata")
    public void saveData(@RequestParam Integer id , @RequestParam String abhaid, @RequestBody Medicalrecords medicalrecords){
        Login doctor=loginRepository.findAllById(id);
        Patient patient=patientRepository.findPatientsById(abhaid);
        medicalrecords.setDoctor(doctor);
        medicalData.save(medicalrecords);
    }

    @GetMapping("/getdoctors")
    public List<Login> getDoctors(){
        return loginRepository.findAllByRole("doctor");
    }
}
