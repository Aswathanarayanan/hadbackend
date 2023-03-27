package com.example.hadbackend.bean.carecontext;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
//@NoArgsConstructor
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
//@Entity
@Embeddable
public class Address {
//
//    @Column
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String line;


    private String district;

    private String state;

    private String pincode;
}
