package dev.lunna.mayte.service;

import dev.lunna.mayte.util.DataPathProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class FileSystemStorageService implements FileStorageService {

  private final Path root;

  public FileSystemStorageService() {
    this.root = Paths.get(DataPathProvider.getDataPath()).toAbsolutePath().normalize();
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException("No se pudo crear el directorio raíz de almacenamiento: " + root, e);
    }
  }

  @Override
  public String storeFile(@NotNull MultipartFile file, @NotNull String path, @NotNull String fileName, boolean overwrite) {
    try {
      Path dirPath = root.resolve(path).normalize();
      Files.createDirectories(dirPath);

      String cleanFileName = StringUtils.cleanPath(fileName);
      Path fullPath = dirPath.resolve(cleanFileName);

      if (!overwrite && Files.exists(fullPath)) {
        throw new FileAlreadyExistsException("El archivo ya existe y overwrite=false: " + fullPath);
      }

      Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
      return fullPath.toString();

    } catch (IOException e) {
      throw new RuntimeException("Error al guardar archivo", e);
    }
  }

  @Override
  public Resource getFile(@NotNull String path) {
    try {
      Path filePath = root.resolve(path).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists() && resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("No se pudo leer el archivo: " + path);
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("URL mal formada para el archivo: " + path, e);
    }
  }

  @Override
  public void deleteFile(@NotNull String path) {
    try {
      Path filePath = root.resolve(path).normalize();
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      throw new RuntimeException("Error al eliminar archivo", e);
    }
  }

  @Override
  public boolean fileExists(@NotNull String path) {
    Path filePath = root.resolve(path).normalize();
    return Files.exists(filePath);
  }
}
