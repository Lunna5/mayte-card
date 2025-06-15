package dev.lunna.mayte.exception;

public class SimpleJsonException extends JsonBaseException {
  public SimpleJsonException(String message, int status, String error) {
    super(message, status, error);
  }
}
