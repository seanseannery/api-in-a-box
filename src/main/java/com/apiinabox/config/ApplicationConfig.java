package com.apiinabox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Spring application configuration beans. */
@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

  /**
   * Registers protobuf JSON serialization support so Spring MVC can serialize and deserialize
   * protobuf message objects as JSON using protobuf's native JsonFormat.
   */
  @Bean
  public ProtobufJsonFormatHttpMessageConverter protobufJsonFormatHttpMessageConverter() {
    return new ProtobufJsonFormatHttpMessageConverter();
  }

  /** Defaults content negotiation to JSON so clients without an Accept header get JSON. */
  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(MediaType.APPLICATION_JSON);
  }
}
