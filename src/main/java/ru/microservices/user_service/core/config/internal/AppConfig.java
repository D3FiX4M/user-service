package ru.microservices.user_service.core.config.internal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Slf4j
@Configuration
public class AppConfig {
	@PostConstruct
	void initTimezone() {
		log.info("Default timezone initialized");
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Bean
	public ObjectMapper objectMapper() {
		log.info("Default object mapper initialized");
		return new ObjectMapper()
				.registerModules()
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
}
