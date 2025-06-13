package dev.lunna.mayte.database.model;

import dev.lunna.mayte.authority.ApplicationAuthority;
import dev.lunna.mayte.authority.ApplicationRole;
import jakarta.persistence.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements UserDetails {
  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "account_non_expired", nullable = false)
  private boolean accountNonExpired = true;

  @Column(name = "account_non_locked", nullable = false)
  private boolean accountNonLocked = true;

  @Column(name = "credentials_non_expired", nullable = false)
  private boolean credentialsNonExpired = true;

  @Column(name = "enabled", nullable = false)
  private boolean enabled = true;

  @ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
  @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "authority", nullable = false)
  private Set<String> authorities;

  @Column(name = "created_at", updatable = false)
  private Long createdAt = System.currentTimeMillis();

  @Column(name = "updated_at")
  private Long updatedAt = System.currentTimeMillis();

  public User(@NotNull final String email, @NotNull final Set<String> authorities) {
    this.email = email;
    this.authorities = authorities;
  }

  public User(@NotNull final String email, @NotNull final ApplicationRole role) {
    this.email = email;
    this.authorities = new HashSet<>();
    this.addRole(role);
  }

  public User() {}

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities.stream()
        .map(SimpleGrantedAuthority::new)
        .toList();
  }

  @Override
  @Nullable
  @Contract("-> null")
  public String getPassword() {
    return null; // We don't use passwords to authenticate users
  }

  @Override
  public String getUsername() {
    return email.split("@")[0];
  }

  public void addAuthority(@NotNull final ApplicationAuthority authority) {
    requireNonNull(authority, "Authority cannot be null");
    authorities.add(authority.getAuthority());
  }

  public void removeAuthority(@NotNull final ApplicationAuthority authority) {
    requireNonNull(authority, "Authority cannot be null");
    authorities.remove(authority.getAuthority());
  }

  public boolean hasAuthority(@NotNull final ApplicationAuthority authority) {
    requireNonNull(authority, "Authority cannot be null");
    return authorities.contains(authority.getAuthority());
  }

  public void addRole(@NotNull final ApplicationRole role) {
    requireNonNull(role, "Role cannot be null");
    authorities.add(role.name());

    for (ApplicationAuthority authority : role.getAuthorities()) {
      addAuthority(authority);
    }
  }

  public void removeRole(@NotNull final ApplicationRole role) {
    requireNonNull(role, "Role cannot be null");
    authorities.remove(role.name());

    for (ApplicationAuthority authority : role.getAuthorities()) {
      removeAuthority(authority);
    }
  }

  public boolean hasRole(@NotNull final ApplicationRole role) {
    requireNonNull(role, "Role cannot be null");
    return authorities.contains(role.name());
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public Long getUpdatedAt() {
    return updatedAt;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setAccountNonExpired(boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  public void setAccountNonLocked(boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  public void setCredentialsNonExpired(boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
