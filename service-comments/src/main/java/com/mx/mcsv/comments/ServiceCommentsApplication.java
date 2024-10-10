package com.mx.mcsv.comments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceCommentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceCommentsApplication.class, args);
	}

}
