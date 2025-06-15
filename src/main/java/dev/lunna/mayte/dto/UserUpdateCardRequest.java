package dev.lunna.mayte.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

public record UserUpdateCardRequest(
    @Nullable
    @Length(min = 1, max = 24, message = "El nombre del titular tiene que tener entre 1 y 24 caracteres")
    String cardHolderName,

    @Nullable
    @Length(min = 16, max = 244, message = "La dedicatoria tiene que tener entre 16 y 244 caracteres")
    String dedication,

    @Nullable
    MultipartFile image,

    @Nullable
    String theme
) {
}
