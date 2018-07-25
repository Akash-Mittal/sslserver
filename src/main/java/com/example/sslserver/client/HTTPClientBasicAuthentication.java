package com.example.sslserver.client;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class HTTPClientBasicAuthentication {

    // public static final String REMOTE_URL = "https://localhost:8443/secured";

    // public static final String REMOTE_URL = "https://uatsky.texasbank.in:444/app/uat/fundtransfer2R/transfer";
    public static final String REMOTE_URL = "https://uatsky.texasbank.in/";

    public static void main(String[] args) throws Exception {
        texasBankService();
    }

    private static void texasBankService() throws Exception {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(new File("path/to/jks/file.jks")), "changeit".toCharArray());
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy())
                        .loadKeyMaterial(keyStore, "file.jks.password".toCharArray()).build());
        HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ResponseEntity<String> response = restTemplate.postForEntity(REMOTE_URL, null, String.class);
        System.out.println(response.getBody());

    }

}
