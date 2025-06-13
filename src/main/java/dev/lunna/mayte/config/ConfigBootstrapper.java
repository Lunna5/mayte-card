package dev.lunna.mayte.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

public class ConfigBootstrapper {
  private static final Logger logger = LoggerFactory.getLogger(ConfigBootstrapper.class);

  private static final String DEFAULT_CONFIG_CLASSPATH = "application-defaults.yml";
  private static final String CONFIG_ENV_VAR = "MAYTE_CONFIG_PATH";
  private static final String DEFAULT_CONFIG_NAME_ENV_VAR = "MAYTE_DEFAULT_CONFIG";

  private final String externalConfigPath;
  private final String defaultConfigName;

  public ConfigBootstrapper() {
    this.externalConfigPath = determineConfigPath();
    this.defaultConfigName = System.getenv()
        .getOrDefault(DEFAULT_CONFIG_NAME_ENV_VAR, DEFAULT_CONFIG_CLASSPATH);
  }

  public void initialize() {
    logger.info("Using external config path: {}", externalConfigPath);
    copyDefaultConfigIfNeeded();
  }

  public String getConfigLocation() {
    return "file:" + externalConfigPath;
  }

  private String determineConfigPath() {
    String envPath = System.getenv(CONFIG_ENV_VAR);
    if (envPath != null && !envPath.trim().isEmpty()) {
      return envPath;
    }

    String osName = System.getProperty("os.name").toLowerCase();
    if (osName.contains("win")) {
      String appData = System.getenv("LOCALAPPDATA");
      if (appData == null) {
        appData = System.getProperty("user.home") + "\\AppData\\Local";
      }
      return appData + "\\Lunna\\Mayte\\config\\application.yml";
    } else {
      return "/etc/lunna/mayte/application.yml";
    }
  }

  private void copyDefaultConfigIfNeeded() {
    try {
      Path externalPath = Paths.get(externalConfigPath).toAbsolutePath();

      if (Files.exists(externalPath)) {
        return;
      }

      Files.createDirectories(externalPath.getParent());

      Resource resource = new ClassPathResource(defaultConfigName);
      if (resource.exists()) {
        Files.copy(
            resource.getInputStream(),
            externalPath,
            StandardCopyOption.REPLACE_EXISTING
        );
        setFilePermissions(externalPath);
      } else {
        logger.error("Could not copy default config file: {}", externalConfigPath);
        throw new RuntimeException("Default config file not found: " + defaultConfigName);
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new RuntimeException("Error copying default config file", e);
    }
  }

  private void setFilePermissions(Path path) throws IOException {
    if (!System.getProperty("os.name").toLowerCase().contains("win")) {
      try {
        Files.setPosixFilePermissions(path,
            Set.of(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.GROUP_READ
            ));
      } catch (UnsupportedOperationException e) {
        logger.warn("Setting POSIX file permissions is not supported on this OS: {}", e.getMessage());
      } catch (IOException e) {
        logger.error("Failed to set file permissions for {}: {}", path, e.getMessage());
        throw e;
      }
    }
  }
}
