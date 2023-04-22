package com.example.hadbackend.DAOimplement;

import com.example.hadbackend.bean.consent.HIUConsentTable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ConsentRepository extends JpaRepository<HIUConsentTable,Integer> {
    List<HIUConsentTable> findAllByConsentId(String id);
    void deleteAllByExpiryDateBefore(Date d1);
}