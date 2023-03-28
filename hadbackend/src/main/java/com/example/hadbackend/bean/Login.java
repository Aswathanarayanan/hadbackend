package com.example.hadbackend.bean;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity

public class Login {

    @Column
    @Id
    @GeneratedValue
    private int id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String role;
}
