package com.example.hadbackend.service.carecontext;

import com.example.hadbackend.DAOimplement.PatientRepository;
import com.example.hadbackend.bean.carecontext.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
public class Addpatient {

    @Autowired
    PatientRepository patientRepository;
    public void insertpatientdetails(Patient p){
        //PatientRepository patientRepository= new PatientRepository();
        //patientRepository.insert(p);
        TimeZone timeZone=TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS");
        dateFormat.setTimeZone(timeZone);
        String asISO= dateFormat.format(new Date());

        p.setVisitid(asISO);
        patientRepository.save(p);
        }
}
