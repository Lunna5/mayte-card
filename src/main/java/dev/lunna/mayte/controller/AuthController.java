package dev.lunna.mayte.controller;

import dev.lunna.mayte.database.model.User;
import dev.lunna.mayte.dto.UserLoginDTO;
import dev.lunna.mayte.dto.UserOtpRequestResponseDTO;
import dev.lunna.mayte.dto.UserOtpValidationDTO;
import dev.lunna.mayte.dto.UserTokenResponseDTO;
import dev.lunna.mayte.exception.TokenExpiredException;
import dev.lunna.mayte.service.EmailOTPService;
import dev.lunna.mayte.service.JwtService;
import dev.lunna.mayte.service.UserService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final UserService userService;
  private final JwtService jwtService;
  private final EmailOTPService emailOTPService;

  public AuthController(
      @NotNull final UserService userService,
      @NotNull final JwtService jwtService,
      @NotNull final EmailOTPService emailOTPService
  ) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.emailOTPService = emailOTPService;
  }

  @PostMapping("/request-otp")
  public UserOtpRequestResponseDTO requestOtp(
      @Valid @RequestBody @NotNull final UserLoginDTO userLoginDTO
  ) {
    return userService.login(userLoginDTO);
  }

  @PostMapping("/login")
  public UserTokenResponseDTO login(
      @Valid @RequestBody @NotNull final UserOtpValidationDTO userLoginDTO
  ) {
    boolean result = this.emailOTPService.validateOTP(
        userLoginDTO.email(),
        userLoginDTO.otpCode()
    );

    if (!result) {
      throw new TokenExpiredException();
    }

    String token = jwtService.generateToken(userLoginDTO.email());

    return new UserTokenResponseDTO(token);
  }

  @GetMapping("/me")
  public User getCurrentUser(@AuthenticationPrincipal final User user) {
    if (user == null) {
      throw new IllegalStateException("User not authenticated");
    }

    return user;
  }
}
