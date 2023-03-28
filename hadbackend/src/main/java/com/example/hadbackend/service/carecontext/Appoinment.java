package com.example.hadbackend.service.carecontext;

import com.example.hadbackend.DAOimplement.PatientRepository;
import com.example.hadbackend.bean.Login;
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

    public List<Patient> getpatientlist(String doctoremail){
        return patientRepository.findPatientsByAppoinementAndDoctor(1,doctoremail);
    }

    public void endappointment(String abhaid){
        Patient p=patientRepository.findPatientsById(abhaid);
        p.setAppoinement(0);
        patientRepository.save(p);
    }
}
