package dev.lunna.mayte.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonBaseException extends RuntimeException {

  private final LocalDateTime timestamp;
  private final int status;
  private final String error;

  public JsonBaseException(String message, int status, String error) {
    super(message);
    this.status = status;
    this.error = error;
    this.timestamp = LocalDateTime.now();
  }

  public Map<String, Object> toJson() {
    Map<String, Object> json = new HashMap<>();
    json.put("timestamp", timestamp.toString());
    json.put("status", status);
    json.put("error", error);
    json.put("message", getMessage());
    return json;
  }

  public int getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }
}
