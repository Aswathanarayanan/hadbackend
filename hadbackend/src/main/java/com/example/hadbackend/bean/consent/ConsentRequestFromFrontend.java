package com.example.hadbackend.bean.consent;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ConsentRequestFromFrontend {
    
    private String abhaid;
    private String dateFrom;
    private String dateTo;
    private String expirayDate;
    private String purpose;
    private String docName;
    
}
