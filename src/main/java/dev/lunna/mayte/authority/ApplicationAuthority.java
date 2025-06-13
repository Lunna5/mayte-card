package dev.lunna.mayte.authority;

import org.jetbrains.annotations.NotNull;

public enum ApplicationAuthority {
  PERMISSION_SUBMIT_CARD("SUBMIT_CARD"),
  PERMISSION_MANAGE_USERS("MANAGE_USERS"),
  PERMISSION_VIEW_CARDS("VIEW_CARDS");

  private final String authority;

  ApplicationAuthority(@NotNull final String authority) {
    this.authority = authority;
  }

  @NotNull
  public String getAuthority() {
    return authority;
  }


  @Override
  public String toString() {
    return "ApplicationAuthority{" +
           "authority='" + authority + '\'' +
           '}';
  }
}
