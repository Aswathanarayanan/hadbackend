package com.example.hadbackend.controller;

import com.example.hadbackend.DAOimplement.LoginRepository;
import com.example.hadbackend.DAOimplement.PatientRepository;
import com.example.hadbackend.bean.auth.Login;
import com.example.hadbackend.bean.carecontext.Medicalrecords;
import com.example.hadbackend.bean.carecontext.Patient;
import com.example.hadbackend.service.fhir.GenerateRecord;
import com.example.hadbackend.service.fhir.OPConsult;
import com.example.hadbackend.service.fhir.OPconsultation;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
public class FhirController {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    LoginRepository loginRepository;

    @PostMapping("/bundlerecords")
    void bundlerecords(@RequestParam String abhaid, @RequestParam String doctorid, @RequestBody Medicalrecords medicalrecords) throws ParseException, IOException {
//        Patient patient= patientRepository.findPatientsById(abhaid);
//        Login login=loginRepository.findAllByEmail(doctorid);
//        GenerateRecord generateRecord=new GenerateRecord();
//        Bundle records=generateRecord.createmedicalrecords(patient,login,medicalrecords);
//        OPConsult opConsult=new OPConsult();
//        Bundle records=opConsult.genereateoprecord();
        medicalrecords.setPatient(patientRepository.findPatientsById(abhaid));
        medicalrecords.setDoctor(loginRepository.findAllByEmail(doctorid));
        OPconsultation oPconsultation=new OPconsultation();
        oPconsultation.bundleoutput(medicalrecords);
//        Bundle records=oPconsultation.populateOPConsultNoteBundle(medicalrecords,null);
        //return records;
    }
}
