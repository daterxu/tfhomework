package com.demo.medium;


import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MediumApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediumApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpHost myProxy = new HttpHost("127.0.0.1", 7890);
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setProxy(myProxy).disableCookieManagement();
        HttpClient httpClient = clientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        factory.setReadTimeout(60000);
        factory.setConnectTimeout(60000);
        return new RestTemplate(factory);
    }
}
