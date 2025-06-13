package dev.lunna.mayte.dto;

public record UserOtpRequestResponseDTO(
    boolean valid,
    String username,
    String email,
    boolean newAccount
) {
}
