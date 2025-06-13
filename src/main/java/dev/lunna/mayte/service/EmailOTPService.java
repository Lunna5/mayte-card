package dev.lunna.mayte.service;

import dev.lunna.mayte.database.model.User;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;

public interface EmailOTPService {
  /**
   * Sends an OTP to the user's email.
   *
   * @param user the user to whom the OTP will be sent
   * @return true if the OTP was sent successfully, false otherwise
   */
  void sendOTP(@NotNull final User user);

  /**
   * Validates the OTP for the user.
   *
   * @param email the users email whose OTP is being validated
   * @param otp the OTP to validate
   * @return true if the OTP is valid, false otherwise
   */
  boolean validateOTP(@NotNull final String email, @NotNull final String otp);
}
