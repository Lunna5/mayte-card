package dev.lunna.mayte.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
    @NotBlank(message = "El email no puede estar vacío y debe de pertenecer a g.educaand.es")
    @Email(message = "El email debe de ser válido")
    String email
) {
}
