package dev.lunna.mayte.service;

import dev.lunna.mayte.database.model.OtpCode;
import dev.lunna.mayte.database.model.User;
import dev.lunna.mayte.database.repository.OtpCodeRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Future;

@Service
public class SMTPEmailOTPService implements EmailOTPService {

  private static final Logger log = LoggerFactory.getLogger(SMTPEmailOTPService.class);
  private final OtpCodeRepository otpRepository;
  private final EmailService emailService;

  @Autowired
  public SMTPEmailOTPService(OtpCodeRepository otpRepository, EmailService emailService) {
    this.otpRepository = otpRepository;
    this.emailService = emailService;
  }

  @Async
  @Override
  @Transactional
  public void sendOTP(@NotNull final User user) {
    String email = user.getEmail();
    otpRepository.deleteByEmail(email);

    String otpCode = generateOtp();
    OtpCode otpEntity = new OtpCode(otpCode, email, LocalDateTime.now().plusMinutes(10));
    otpRepository.save(otpEntity);

    emailService.sendOtpEmail(user, otpCode);
    log.info("OTP sent");
  }

  @Override
  @Transactional
  public boolean validateOTP(@NotNull final String email, @NotNull final String otp) {
    return otpRepository.findByEmailAndCodeAndUsedFalse(email, otp)
        .map(otpCode -> {
          if (otpCode.isExpired()) {
            return false;
          }
          otpCode.setUsed(true);
          otpRepository.save(otpCode);
          return true;
        })
        .orElse(false);
  }

  private String generateOtp() {
    Random random = new Random();
    int code = 100000 + random.nextInt(900000); // Genera un código de 6 dígitos
    return String.valueOf(code);
  }
}