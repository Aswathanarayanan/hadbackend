package com.example.hadbackend.bean.carecontext;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "Patient")
public class Patient {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientid;

    @Column
    private String id;

    @Column()
    private String name;

    @Column
    private String gender;

    @Column
    private int yearOfBirth;

    @Column
    private int monthOfBirth;

    @Column
    private int dayOfBirth;

//    @OneToOne
//    @JoinColumn(name = "addressid",referencedColumnName = "id")
//    @Embedded
//    private Address address;

    @Column
    private Long mobilenum;

    @Column
    private String line;

    @Column
    private String district;

    @Column
    private String state;

    @Column
    private String pincode;

//    @OneToMany
//    @JoinColumn(name = "identifierid"
    @Column
    private int appoinement;

}
