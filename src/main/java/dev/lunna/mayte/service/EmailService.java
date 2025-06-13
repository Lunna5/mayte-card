package dev.lunna.mayte.service;

import dev.lunna.mayte.database.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class EmailService {
  private final JavaMailSender mailSender;
  private final MessageSource messageSource;

  @Autowired
  public EmailService(@NotNull final JavaMailSender mailSender, @NotNull final MessageSource messageSource) {
    this.mailSender = mailSender;
    this.messageSource = messageSource;
  }

  public void sendOtpEmail(@NotNull final User user, @NotNull final String otp) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(user.getEmail());
    message.setSubject(messageSource.getMessage("email.otp.subject", null, "email.otp.subject", LocaleContextHolder.getLocale()));
    message.setText(messageSource.getMessage(
        "email.otp.body",
        new Object[]{user.getUsername(), otp},
        "email.otp.body",
        LocaleContextHolder.getLocale()
    ));

    mailSender.send(message);
  }
}