package com.it.click;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.mongodb.MongoClientOptions;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ClickItApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClickItApplication.class, args);
	}
	
    @Bean
    public MongoClientOptions mongoOptions() {
        return MongoClientOptions
            .builder()
            .maxConnectionIdleTime(60000)
            .build();
    }

}
