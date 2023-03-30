package com.example.hadbackend.service.carecontext;

import com.example.hadbackend.DAOimplement.LoginRepository;
import com.example.hadbackend.DAOimplement.PatientRepository;
import com.example.hadbackend.bean.Login;
import com.example.hadbackend.bean.carecontext.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class Appoinment {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    LoginRepository loginRepository;

    public void addappoinment(String abhaid,Patient p){
        Patient patient=patientRepository.findPatientsById(abhaid);
        patient.setName(p.getName());
        patient.setGender(p.getGender());
        patient.setYear(p.getYear());
        patient.setMonth(p.getMonth());
        patient.setDay(p.getDay());
        patient.setDistrict(p.getDistrict());
        patient.setState(p.getState());
        patient.setMobile(p.getMobile());
        patient.setAppoinement(1);

        TimeZone timeZone=TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS");
        dateFormat.setTimeZone(timeZone);
        String asISO= dateFormat.format(new Date());

        patient.setVisitid(asISO);

        patientRepository.save(patient);
    }

    public List<Patient> getpatientlist(String doctoremail){
        Login l=loginRepository.findAllByEmail(doctoremail);
        return patientRepository.findPatientsByAppoinementAndDoctor(1,l);
    }

    public void endappointment(String abhaid){
        Patient p=patientRepository.findPatientsById(abhaid);
        p.setAppoinement(0);
        patientRepository.save(p);
    }
}
