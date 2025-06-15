package dev.lunna.mayte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

public record UserCreateCardRequest(
    @NotBlank(message = "El nombre del titular no puede estar vacío, tiene que tener entre 1 y 24 caracteres")
    @Length(min = 1, max = 24, message = "El nombre del titular tiene que tener entre 1 y 24 caracteres")
    String cardHolderName,

    @NotBlank(message = "La dedicatoria no puede estar vacía, tiene que tener entre 16 y 244 caracteres")
    @Length(min = 16, max = 244, message = "La dedicatoria tiene que tener entre 16 y 244 caracteres")
    String dedication,

    @NotNull(message = "Tienes que subir una imagen")
    MultipartFile image,

    @NotNull(message = "El tema no puede estar vacío")
    String theme
) {
}
