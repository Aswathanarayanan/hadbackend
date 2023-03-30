package com.example.hadbackend.DAOimplement;

import com.example.hadbackend.bean.carecontext.Medicalrecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalData extends JpaRepository<Medicalrecords,Integer> {
}
