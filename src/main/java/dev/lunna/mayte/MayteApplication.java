package dev.lunna.mayte;

import dev.lunna.mayte.config.ConfigBootstrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Map;

@EnableAsync
@SpringBootApplication
@ConfigurationPropertiesScan
public class MayteApplication {
  public static void main(String[] args) {
    ConfigBootstrapper bootstrapper = new ConfigBootstrapper();
    bootstrapper.initialize();

    SpringApplication app = new SpringApplication(MayteApplication.class);

    app.setDefaultProperties(
        Map.of("spring.config.location", bootstrapper.getConfigLocation())
    );

    app.run(args);
  }
}
