package com.example.hadbackend.service.carecontext;

import com.example.hadbackend.DAOimplement.PatientRepository;
import com.example.hadbackend.bean.carecontext.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Appoinment {

    @Autowired
    PatientRepository patientRepository;

    public void addappoinment(String abhaid){
        Patient p=patientRepository.findPatientsById(abhaid);
        p.setAppoinement(1);
        patientRepository.save(p);
    }

    public List<Patient> getpatientlist(){
        return patientRepository.findPatientsByAppoinement(1);
    }
}
