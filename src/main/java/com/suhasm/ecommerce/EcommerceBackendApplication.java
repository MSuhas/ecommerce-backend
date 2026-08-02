package com.suhasm.ecommerce;

import com.suhasm.ecommerce.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class EcommerceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				EcommerceBackendApplication.class, args);
		System.out.println("Pass: " +
				new BCryptPasswordEncoder().encode("password123")
		);
	}

}
