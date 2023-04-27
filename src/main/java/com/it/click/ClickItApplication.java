package com.it.click;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableSwagger2
public class ClickItApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClickItApplication.class, args);
	}
	
	@Configuration
	public class CorsConfig implements WebMvcConfigurer {
	 
	  @Override
	  public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/**")
	      .allowedOrigins("*")
	      .allowedMethods("GET", "POST", "PUT", "DELETE")
	      .allowedHeaders("*")
	      .allowCredentials(true)
	      .maxAge(3600);
	  }
	}

}
