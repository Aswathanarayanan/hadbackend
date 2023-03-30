package com.example.hadbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class HadbackendApplication {

    @Configuration
    @EnableWebMvc
    public class Corsconfig implements WebMvcConfigurer{
        @Override
        public void addCorsMappings(CorsRegistry registry){
            registry.addMapping("/**");
        }
    }

    //saving the accesstoken for care context

//    String accesstoken;
//
//    public void setAccesstoken(String accesstoken){
//        this.accesstoken=accesstoken;
//    }
//    public String getAccesstoken(){
//        return this.accesstoken;
//    }
    public static void main(String[] args) {
        SpringApplication.run(HadbackendApplication.class, args);
    }

}
