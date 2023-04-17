package com.example.hadbackend.service;

import com.example.hadbackend.bean.auth.OnConfirmPatient;
import com.example.hadbackend.controller.FetchModeController;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public class GetPatientDetails {
    @Retryable(value=NullPointerException.class, maxAttempts = 3, backoff = @Backoff(value=2000))
    public static OnConfirmPatient getpatientdetails(){
        FetchModeController fetchModeController =new FetchModeController();
        System.out.println("retry");
        System.out.println(fetchModeController.patient.getName());
        System.out.println(fetchModeController.patient.getAddress().getState());
        return fetchModeController.patient;
    }
}
