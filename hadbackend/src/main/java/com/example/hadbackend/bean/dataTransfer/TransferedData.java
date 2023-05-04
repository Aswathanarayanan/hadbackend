package com.example.hadbackend.bean.dataTransfer;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Service
@Entity
public class TransferedData {
    
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String consentID;

    @Column
    private String abhaid;

    @Column
    private String symptoms;

    @Column
    private String medicine;

    @Column
    private String dosage;

    @Column
    private String pattern;

    @Column
    private String timings;

    @Column
    private String instruction;

    @Column
    private String expirayDate;
    
}
