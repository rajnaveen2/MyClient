package com.example.demo;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SslClientApplication {

	/*
	 * static { System.setProperty("javax.net.debug", "all");
	 * System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
	 * System.setProperty("https.protocols", "TLSv1.2");
	 * System.setProperty("javax.net.ssl.trustStore", "d://certs//MyClient.jks");
	 * System.setProperty("javax.net.ssl.trustStorePassword", "password");
	 * System.setProperty("javax.net.ssl.keyStore", "d://certs//MyClient.jks");
	 * System.setProperty("javax.net.ssl.keyStorePassword", "password");
	 * 
	 * javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier( new
	 * javax.net.ssl.HostnameVerifier() {
	 * 
	 * public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
	 * if (hostname.equals("localhost")) { return true; } return false; } }); }
	 */

	public static void main(String[] args) {
		SpringApplication.run(SslClientApplication.class, args);
		/*
		 * RestTemplate template = new RestTemplate();
		 * 
		 * ResponseEntity<String> response =
		 * template.getForEntity("https://localhost:9002/server-app/hello",
		 * String.class); System.out.println(response.getBody());
		 * 
		 * 
		 * 
		 * CloseableHttpClient httpClient = HttpClients.custom()
		 * .setSSLHostnameVerifier(new NoopHostnameVerifier()) .build();
		 * HttpComponentsClientHttpRequestFactory requestFactory = new
		 * HttpComponentsClientHttpRequestFactory();
		 * requestFactory.setHttpClient(httpClient);
		 * 
		 * ResponseEntity<String> response = new RestTemplate(requestFactory).exchange(
		 * "https://localhost:9002/server-app/hello", HttpMethod.GET, null,
		 * String.class); System.out.println(response.getBody());
		 */
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		
		KeyStore keyStore;
		HttpComponentsClientHttpRequestFactory requestFactory = null;
		
		try {
			keyStore = KeyStore.getInstance("jks");
			ClassPathResource classPathResource = new ClassPathResource("MyClient.jks");
			InputStream inputStream = classPathResource.getInputStream();
			keyStore.load(inputStream, "password".toCharArray());

			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
					.loadTrustMaterial(null, new TrustSelfSignedStrategy())
					.loadKeyMaterial(keyStore, "password".toCharArray()).build(),
					NoopHostnameVerifier.INSTANCE);

			HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
					.setMaxConnTotal(Integer.valueOf(5))
					.setMaxConnPerRoute(Integer.valueOf(5))
					.build();

			requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			requestFactory.setReadTimeout(Integer.valueOf(10000));
			requestFactory.setConnectTimeout(Integer.valueOf(10000));
			
			restTemplate.setRequestFactory(requestFactory);
		} catch (Exception exception) {
			System.out.println("Exception Occured while creating restTemplate "+exception);
			exception.printStackTrace();
		}
		return restTemplate;
	}
}
