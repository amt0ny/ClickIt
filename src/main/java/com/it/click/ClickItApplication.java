package com.it.click;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableSwagger2
@EnableWebMvc
@CrossOrigin(origins = "*")
public class ClickItApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClickItApplication.class, args);
	}
	

}
