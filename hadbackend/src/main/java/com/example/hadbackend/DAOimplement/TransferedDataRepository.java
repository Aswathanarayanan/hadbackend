package com.example.hadbackend.DAOimplement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hadbackend.bean.dataTransfer.TransferedData;
    
@Repository
public interface TransferedDataRepository extends JpaRepository<TransferedData,Integer> {

    @Query(value = "SELECT * FROM transfered_data u WHERE u.abhaid  = :abhaid and u.consentid = :consentid ", nativeQuery = true)
    List<TransferedData> getTransferedData(@Param(value = "abhaid") String abhaid, @Param(value = "consentid") String consentid);

}

