package pl.igormaculewicz.pastex.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ObjectMapperConfiguration {


  @Bean
  public ObjectMapper objectMapper() {

    ObjectMapper objectMapper = new ObjectMapper();

    objectMapper.findAndRegisterModules();

    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    return objectMapper;
  }

}
