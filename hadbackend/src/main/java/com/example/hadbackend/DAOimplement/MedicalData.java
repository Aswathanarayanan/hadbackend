package com.example.hadbackend.DAOimplement;

import com.example.hadbackend.bean.carecontext.Medicalrecords;
import com.example.hadbackend.bean.carecontext.Patient;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalData extends JpaRepository<Medicalrecords,Integer> {

    List<Medicalrecords> findByPatient(Patient patient);
    List<Medicalrecords> findAllByPatientAndDateBetween(Patient p,Date d1,Date d2);
}
