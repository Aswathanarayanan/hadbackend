package com.example.hadbackend.DAO;

//import org.springframework.stereotype.Repository;

import com.example.hadbackend.bean.carecontext.Patient;

public interface ObjectRepository<T> {
    public void insert(T t);
    public T select(int id);
}
