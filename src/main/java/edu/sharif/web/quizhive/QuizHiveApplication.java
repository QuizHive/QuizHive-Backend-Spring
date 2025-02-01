package edu.sharif.web.quizhive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class QuizHiveApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuizHiveApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// localhost:8081 and quizhive.ahmz.ir
				registry.addMapping("/**").allowedOrigins("http://localhost:8081", "http://quizhive.ahmz.ir");
			}
		};
	}
}
