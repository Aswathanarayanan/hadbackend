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
    public static void main(String[] args) {
        SpringApplication.run(HadbackendApplication.class, args);
    }

}
