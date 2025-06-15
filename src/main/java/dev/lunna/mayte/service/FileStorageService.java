package dev.lunna.mayte.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
  String storeFile(@NotNull final MultipartFile file, @NotNull final String path, @NotNull final String fileName, boolean overwrite);

  Resource getFile(@NotNull final String path);

  void deleteFile(@NotNull final String path);

  boolean fileExists(@NotNull final String path);
}
