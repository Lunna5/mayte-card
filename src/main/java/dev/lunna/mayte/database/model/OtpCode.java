package dev.lunna.mayte.database.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes")
public class OtpCode {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private LocalDateTime expiryDate;

  @Column(nullable = false)
  private boolean used = false;

  public OtpCode() {
  }

  public OtpCode(String code, String email, LocalDateTime expiryDate) {
    this.code = code;
    this.email = email;
    this.expiryDate = expiryDate;
  }

  public Long getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDateTime getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDateTime expiryDate) {
    this.expiryDate = expiryDate;
  }

  public boolean isUsed() {
    return used;
  }

  public void setUsed(boolean used) {
    this.used = used;
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(this.expiryDate);
  }
}
