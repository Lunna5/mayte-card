package dev.lunna.mayte.dto;

import org.jetbrains.annotations.NotNull;

public record UserTokenResponseDTO(
    @NotNull String token
) {
}
