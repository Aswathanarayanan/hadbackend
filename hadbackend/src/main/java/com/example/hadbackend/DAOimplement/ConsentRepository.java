package com.example.hadbackend.DAOimplement;

import com.example.hadbackend.bean.auth.Login;
import com.example.hadbackend.bean.request.ConsentTable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsentRepository extends JpaRepository<ConsentTable,Integer> {
    List<ConsentTable> findAllByConsentId(String id);
}