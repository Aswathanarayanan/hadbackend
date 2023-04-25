package com.example.hadbackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.hadbackend.DAOimplement.ConsentRepository;
import com.example.hadbackend.DAOimplement.TransferedDataRepository;
import com.example.hadbackend.bean.consent.HIUConsentTable;
import com.example.hadbackend.bean.dataTransfer.DataPush;
import com.example.hadbackend.bean.dataTransfer.TransferedData;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RestController
public class DataTransferController {

    @Autowired
    ConsentRepository consentRepository;

    @Autowired
    TransferedDataRepository transferedDataRepository;

    @GetMapping("/gettransfereddata")
    public List<TransferedData> getTransferedData(@RequestParam String abhaid,@RequestParam String consentid){

        List<TransferedData> transferedDataList = transferedDataRepository.getTransferedData(abhaid, consentid);
        return transferedDataList;

    }

    @GetMapping("/getrecenttransfereddata")
    public List<TransferedData> getRecentTransferedData(@RequestParam String abhaid,@RequestParam String dateFrom, @RequestParam String dateTo){

        List<String> consentID = consentRepository.getConsentIDFromDate(dateFrom, dateTo);
        List<TransferedData> transferedDataList = transferedDataRepository.getTransferedData(abhaid, consentID.get(consentID.size()-1));
        return transferedDataList;
    }

    @GetMapping("/getconsentlist")
    public List<HIUConsentTable> getConsentList(){

        List<HIUConsentTable> listConsent= consentRepository.findAll();

        return listConsent;

    }
    
}
