package com.example.hadbackend.DAOimplement;

import com.example.hadbackend.bean.consent.HIPConsentTable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HIPConsentRepository extends JpaRepository<HIPConsentTable,Integer> {
    List<HIPConsentTable> findAllByConsentId(String id);
    void deleteAllByExpiryDateBefore(Date d1);
}