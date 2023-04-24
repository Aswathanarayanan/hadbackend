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

    @Transactional
    @Modifying
    @Query("update HIUConsentTable u set u.transactionId = :transactionId where u.consentId = :consentId")
    void updateTransactionId(@Param(value = "transactionId") String transactionId,@Param(value = "consentId") String consentId);

    @Query(value = "SELECT consent_id FROM hiuconsent_table u WHERE u.transaction_id  = :transaction_id", nativeQuery = true)
    List<String> getConsentID(@Param(value = "transaction_id") String transaction_id );

    @Query(value = "SELECT abhaid FROM hiuconsent_table u WHERE u.transaction_id  = :transaction_id", nativeQuery = true)
    List<String> getAbhaID(@Param(value = "transaction_id") String transaction_id );

    @Query(value = "SELECT consent_id FROM hiuconsent_table u WHERE TIMESTAMP(u.date_from)  = TIMESTAMP(:date_from) and TIMESTAMP(u.date_to) = TIMESTAMP(:date_to)", nativeQuery = true)
    List<String> getConsentIDFromDate(@Param(value = "date_from") String date_from, @Param(value = "date_to") String date_to);

    List<HIUConsentTable> findAll();

    // @Transactional
    // @Modifying
    // @Query("update HIUConsentTable u set u.bundleData = :bundleData where u.consentId = :consentId")
    // void updateBundleData(@Param(value = "bundleData") String bundleData,@Param(value = "consentId") String consentId);

}