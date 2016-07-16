package de.kawumtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(exclude={org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration.class})
@EnableWebMvc
public class KthomeautomationApplication {

	public static void main(String[] args) {
		SpringApplication.run(KthomeautomationApplication.class, args);
	}
}
