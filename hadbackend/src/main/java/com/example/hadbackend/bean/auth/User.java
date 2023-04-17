package com.example.hadbackend.bean.auth;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name= "User")
public class User {

    @Column(name = "name", unique = true)
    @Id
    @NonNull
    private String username;

    @Column(name = "password", unique = true)
    @NonNull
    private String password;

    @Column(name = "role")
    private String role;

    public User(@NonNull String username, @NonNull String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User() {

    }



    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
