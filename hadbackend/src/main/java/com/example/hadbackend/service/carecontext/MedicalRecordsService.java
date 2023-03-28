package com.example.hadbackend.service.carecontext;

import com.example.hadbackend.DAOimplement.MedicalData;
import com.example.hadbackend.bean.carecontext.Medicalrecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class MedicalRecordsService {

    @Autowired
    MedicalData medicalData;

    public void savingMedicalrecords(Medicalrecords medicalrecords){
        medicalData.save(medicalrecords);

    }
}
