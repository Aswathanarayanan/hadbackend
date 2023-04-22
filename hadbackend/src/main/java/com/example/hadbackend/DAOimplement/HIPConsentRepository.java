package com.example.hadbackend.DAOimplement;

import com.example.hadbackend.bean.consent.HIPConsentTable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface HIPConsentRepository extends JpaRepository<HIPConsentTable,Integer> {
    List<HIPConsentTable> findAllByConsentId(String id);
    void deleteAllByExpiryDateBefore(Date d1);
    //String findConsentIdByTransactionId(String txnId);

    @Transactional
    @Modifying
    @Query("update HIPConsentTable u set u.transactionId = :transactionId where u.consentId = :consentId")
    void updateTransactionId(@Param(value = "transactionId") String transactionId,@Param(value = "consentId") String consentId);

    @Query(value = "SELECT consent_id FROM hipconsent_table u WHERE u.transaction_id  = transaction_id ", nativeQuery = true)
    List<String> getConsentID(@Param(value = "transaction_id ") String transaction_id );

    @Transactional
    @Modifying
    @Query("delete from HIPConsentTable b where DATE(b.expiryDate)<DATE(:expiryDate)")
    void deleteoldconsents(@Param(value = "expiryDate") String expiryDate);

}