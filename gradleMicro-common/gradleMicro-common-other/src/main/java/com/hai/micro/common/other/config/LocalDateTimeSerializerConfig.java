package com.hai.micro.common.other.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Configuration
public class LocalDateTimeSerializerConfig {

	private String patternLocalDateTime = "yyyy-MM-dd HH:mm:ss";

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return builder -> {
			builder.serializerByType(LocalDateTime.class,
					new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(patternLocalDateTime)));
			builder.deserializerByType(LocalDateTime.class,
					new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(patternLocalDateTime)));
		};
	}
}
