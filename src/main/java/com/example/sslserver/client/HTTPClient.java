package com.example.sslserver.client;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class HTTPClient {

    public static final String GET_STATUS_REMOTE_URL = "https://uatsky.texasbank.in:444/app/uat/fundtransfer2R/getstatus";

    private static void texasBankService(String requestBody) throws Exception {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(new File("path/to/jks/file.jks")), "changeit".toCharArray());
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy())
                        .loadKeyMaterial(keyStore, "file.jks".toCharArray()).build());
        HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpEntity<String> request = new HttpEntity<String>(requestBody, getRequestHeader());
        ResponseEntity<String> response = restTemplate.postForEntity(GET_STATUS_REMOTE_URL, request, String.class);
        System.out.println(response.getBody());

    }

    private static MultiValueMap<String, String> getRequestHeader() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", "Basic " + "Base64EncodedUserNamePassword");
        headers.add("Content-Type", "application/json");
        return headers;
    }
}
