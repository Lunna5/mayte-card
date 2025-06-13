package dev.lunna.mayte.database.repository;

import dev.lunna.mayte.database.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @NotNull Optional<User> findByEmail(@NotNull final String email);

  @NotNull Optional<User> findById(@NotNull final Long id);

  boolean existsByEmail(@NotNull final String email);
}