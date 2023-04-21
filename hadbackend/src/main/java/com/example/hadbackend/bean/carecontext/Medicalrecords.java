package com.example.hadbackend.bean.carecontext;

import java.util.Date;

import com.example.hadbackend.bean.auth.Login;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
public class Medicalrecords {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String vistid;

    @Column
    private String prescription;

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
    private Date date; //java util or java sql

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Login doctor;

    @Column
    private Date date;

}
