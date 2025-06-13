package dev.lunna.mayte.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.jetbrains.annotations.NotNull;

public record UserOtpValidationDTO(
    @NotBlank(message = "El código OTP no puede estar vacío")
    @Length(min= 6, max = 6, message = "El código OTP tiene que ser de 6 caracteres")
    @NotNull String otpCode,
    @NotBlank(message = "El email no puede estar vacío y debe de pertenecer a g.educaand.es")
    @Email(message = "El email debe de ser válido")
    @NotNull String email
) {
}
