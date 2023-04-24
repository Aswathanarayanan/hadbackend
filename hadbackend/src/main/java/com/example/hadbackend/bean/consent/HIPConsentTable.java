package com.example.hadbackend.bean.consent;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
public class HIPConsentTable {
    
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String consentId;

    @Column
    private String abhaid;

    @Column
    private String expiryDate;

    @Column
    private String dateFrom;

    @Column
    private String dateTo;

    @Column
    private String transactionId;

}
