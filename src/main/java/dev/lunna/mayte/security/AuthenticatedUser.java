package dev.lunna.mayte.security;

import dev.lunna.mayte.database.model.User;
import dev.lunna.mayte.database.repository.UserRepository;

public class AuthenticatedUser {
  private final long id;
  private final UserRepository userRepository;

  public AuthenticatedUser(final long id, final UserRepository userRepository) {
    this.id = id;
    this.userRepository = userRepository;
  }

  /**
   * Retrieves the authenticated user from the repository.
   *
   * @return the authenticated User object
   */
  public User getUser() {
    return userRepository.findById(id)
        .orElseThrow(() -> new IllegalStateException("User not found with ID: " + id));
  }
}
