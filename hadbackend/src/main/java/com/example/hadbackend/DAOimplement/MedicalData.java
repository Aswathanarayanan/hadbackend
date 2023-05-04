package com.example.hadbackend.DAOimplement;

import com.example.hadbackend.bean.carecontext.Medicalrecords;
import com.example.hadbackend.bean.carecontext.Patient;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hadbackend.bean.carecontext.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalData extends JpaRepository<Medicalrecords,Integer> {

    List<Medicalrecords> findByPatient(Patient patient);
    List<Medicalrecords> findAllByPatientAndDateBetween(Patient p,Date d1,Date d2);

    @Query(value="select * from medicalrecords b where b.patient_patientid =:patient and b.date BETWEEN :fromDate AND :toDate",nativeQuery = true)
    List<Medicalrecords> getCareContexts(@Param(value = "patient") int patient,@Param(value = "fromDate") String fromDate, @Param(value = "toDate") String toDate);
   

}
