package com.example.hadbackend.service.carecontext;

import com.example.hadbackend.DAOimplement.MedicalData;
import com.example.hadbackend.DAOimplement.PatientRepository;
import com.example.hadbackend.bean.carecontext.Medicalrecords;
import com.example.hadbackend.bean.carecontext.Patient;

//import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicalRecordsService {

    @Autowired
    MedicalData medicalData;

    @Autowired
    PatientRepository patientRepository;
    
    public void savingMedicalrecords(Medicalrecords medicalrecords){
        medicalData.save(medicalrecords);

    }

    public List<Medicalrecords> carecontexts(String abhaid){

        Patient p=patientRepository.findPatientsById(abhaid);
        return medicalData.findByPatient(p);
    }
}
