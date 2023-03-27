package com.example.hadbackend.service.carecontext;

import com.example.hadbackend.DAOimplement.PatientRepository;
import com.example.hadbackend.bean.carecontext.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Addpatient {

    @Autowired
    PatientRepository patientRepository;
    public void insertpatientdetails(Patient p){
        //PatientRepository patientRepository= new PatientRepository();
        //patientRepository.insert(p);
        patientRepository.save(p);
        }
}
