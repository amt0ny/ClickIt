package com.it.click;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@CrossOrigin(origins = "*")
public class ClickItApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClickItApplication.class, args);
	}
	
//    @Bean
//    public MongoClientOptions mongoOptions() {
//        return MongoClientOptions
//            .builder()
//            .maxConnectionIdleTime(60000)
//            .build();
//    }
    
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH").allowedOrigins("*")
						.allowedHeaders("*");
			}
		};
	}

}
