package pl.igormaculewicz.pastex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.RepositoryDefinition;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(basePackages="pl.*")
public class PastexApplication {

  public static void main(String[] args) {
    SpringApplication.run(PastexApplication.class, args);
  }

}
