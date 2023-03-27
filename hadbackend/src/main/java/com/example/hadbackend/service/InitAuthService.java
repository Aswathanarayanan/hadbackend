package com.example.hadbackend.service;

import com.example.hadbackend.bean.AuthInitRequest;
import com.example.hadbackend.bean.FetchModeRequest;
import com.example.hadbackend.controller.FetchModeController;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

public class InitAuthService {

    WebClient webClient=WebClient.create();
    @Retryable(value= WebClientResponseException.class, maxAttempts = 3, backoff = @Backoff(value=2000))
    public Mono<Object> initauthservice(AuthInitRequest authInitRequest) {
        FetchModeController fetchmodecontroller=new FetchModeController();
        String token=fetchmodecontroller.token;
        Mono<Object> res = webClient.post()
                .uri("https://dev.abdm.gov.in/gateway/v0.5/users/auth/init")
                .header(HttpHeaders.AUTHORIZATION,token)
                .header("X-CM-ID","sbx")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(authInitRequest), AuthInitRequest.class)
                .retrieve()
                .bodyToMono(Object.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)));

        System.out.println("INitcomplete");
        return res;
    }
}
