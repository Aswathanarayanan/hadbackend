package com.example.hadbackend.DAOimplement;

import com.example.hadbackend.DAO.ObjectRepository;
import com.example.hadbackend.bean.carecontext.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer> {

//    private Map<Integer,Patient> repository;
//
//    public PatientRepository(){
//        this.repository=new HashMap<>();
//    }
//    @Override
//    public void insert(Patient p){
//        repository.put(p.getPatientid(),p);
//    }
//    public Patient select(int id){
//        return repository.get(id);
    Patient findPatientsById(String abhaid);
    List<Patient> findPatientsByAppoinement(int ap);
}

//    }


