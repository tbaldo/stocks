package br.com.baldo.stocks;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
public class StocksApplication {

	public static void main(String[] args) {
		SpringApplication.run(StocksApplication.class, args);
	}

	@Bean
	public RestTemplate retTemplateProxy(RestTemplateBuilder builder) {

		RestTemplate restTemplate = builder.build();

		HttpHost proxy = new HttpHost("spobrproxy.serasa.intranet", 3128);
		HttpClient httpClient = HttpClientBuilder.create().setRoutePlanner(new DefaultProxyRoutePlanner(proxy) {

			@Override
			public HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {

				if (target.getHostName().equals("stg-br-api.experian.com") ||
						target.getHostName().equals("dev-br-api.experian.com")) {
					return super.determineProxy(target, request, context);
				}
				return null;
			}
		}).build();

		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient)));

		return restTemplate;
	}

}
