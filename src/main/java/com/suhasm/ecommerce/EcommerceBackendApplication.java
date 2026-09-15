package com.suhasm.ecommerce;

import com.suhasm.ecommerce.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*@EnableScheduling*/
@EnableCaching
@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class EcommerceBackendApplication {

	public static void main(String[] args) {
		/*System.out.println("RAZORPAY_KEY_ID = "
				+ System.getenv("RAZORPAY_KEY_ID"));

		System.out.println("RAZORPAY_KEY_SECRET present = "
				+ (System.getenv("RAZORPAY_KEY_SECRET") != null));*/
		SpringApplication.run(
				EcommerceBackendApplication.class, args);
	/*	System.out.println("Pass: " +
				new BCryptPasswordEncoder().encode("password123")
		);*/
	}

}
