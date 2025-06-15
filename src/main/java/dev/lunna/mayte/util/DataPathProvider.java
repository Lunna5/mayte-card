package dev.lunna.mayte.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataPathProvider {
  private static final Logger logger = LoggerFactory.getLogger(DataPathProvider.class);

  private static final String ENV_VAR_DATA_PATH = "MAYTE_DATA_PATH";

  private static final String LINUX_DEFAULT_PATH = "/var/lib/lunna/mayte/data/";
  private static final String WINDOWS_RELATIVE_PATH = "\\Lunna\\Mayte\\data\\";

  public static String getDataPath() {
    String envPath = System.getenv(ENV_VAR_DATA_PATH);
    if (envPath != null && !envPath.trim().isEmpty()) {
      logger.info("Usando ruta personalizada desde la variable de entorno: {}", envPath);
      return envPath;
    }

    String osName = System.getProperty("os.name").toLowerCase();
    if (osName.contains("win")) {
      String appData = System.getenv("LOCALAPPDATA");
      if (appData == null) {
        appData = System.getProperty("user.home") + "\\AppData\\Local";
      }
      return appData + WINDOWS_RELATIVE_PATH;
    } else {
      return LINUX_DEFAULT_PATH;
    }
  }
}
