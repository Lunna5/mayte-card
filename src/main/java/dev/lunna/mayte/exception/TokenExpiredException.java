package dev.lunna.mayte.exception;

public class TokenExpiredException extends JsonBaseException {

  public TokenExpiredException() {
    super("Token has expired", 401, "Unauthorized");
  }
}
