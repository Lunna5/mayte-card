package dev.lunna.mayte.service;

import dev.lunna.mayte.authority.ApplicationRole;
import dev.lunna.mayte.database.model.User;
import dev.lunna.mayte.database.repository.UserRepository;
import dev.lunna.mayte.dto.UserLoginDTO;
import dev.lunna.mayte.dto.UserOtpRequestResponseDTO;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private static final Logger log = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final EmailOTPService otpService;

  /**
   * Constructor for UserService.
   *
   * @param userRepository the repository to manage user data
   */
  public UserService(
      @NotNull final UserRepository userRepository,
      @NotNull final EmailOTPService otpService
  ) {
    this.userRepository = userRepository;
    this.otpService = otpService;
  }

  @Transactional
  public UserOtpRequestResponseDTO login(@NotNull final UserLoginDTO data) {
    User user;
    boolean newUser = false;

    if (!userRepository.existsByEmail(data.email())) {
      user = register(data);
      newUser = true;
    } else {
      user =  userRepository.findByEmail(data.email())
          .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    otpService.sendOTP(user);

    return new UserOtpRequestResponseDTO(
        true,
        user.getUsername(),
        user.getEmail(),
        newUser
    );
  }

  @Transactional
  public User register(@NotNull final UserLoginDTO data) {
    User user = new User(data.email(), ApplicationRole.ROLE_USER);

    userRepository.save(user);
    return user;
  }
}
