package it.uniroma2.marchidori.maininterface.exception;

public class LobbyFilterException extends RuntimeException {
  public LobbyFilterException(String message) {
    super(message);
  }

  public LobbyFilterException(String message, Throwable cause) {
    super(message, cause);
  }
}
