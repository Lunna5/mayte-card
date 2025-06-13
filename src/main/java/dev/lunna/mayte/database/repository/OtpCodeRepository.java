package dev.lunna.mayte.database.repository;

import dev.lunna.mayte.database.model.OtpCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
  Optional<OtpCode> findByEmailAndCodeAndUsedFalse(
      @NotNull String email,
      @NotNull String code);

  void deleteByEmail(@NotNull String email);
}