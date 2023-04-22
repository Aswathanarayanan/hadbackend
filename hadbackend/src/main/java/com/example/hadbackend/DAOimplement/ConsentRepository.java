package com.example.hadbackend.DAOimplement;

import com.example.hadbackend.bean.consent.HIUConsentTable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ConsentRepository extends JpaRepository<HIUConsentTable,Integer> {
    List<HIUConsentTable> findAllByConsentId(String id);
    void deleteAllByExpiryDateBefore(Date d1);

    @Transactional
    @Modifying
    @Query("delete from HIUConsentTable b where DATE(b.expiryDate)<DATE(:expiryDate)")
    void deleteoldconsents(@Param(value = "expiryDate") String expiryDate);

}