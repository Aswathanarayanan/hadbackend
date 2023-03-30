package com.example.hadbackend.DAOimplement;

import com.example.hadbackend.bean.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginRepository extends JpaRepository<Login,Integer> {

    Login findAllByEmailAndPasswordAndRole(String email,String password,String role);
    Login findAllById(Integer id);

    List<Login> findAllByRole(String role);

    Login findAllByEmail(String email);
    Login findAllByIdAndRole(Integer id,String role);

}
