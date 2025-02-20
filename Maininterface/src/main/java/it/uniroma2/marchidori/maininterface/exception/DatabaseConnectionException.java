package it.uniroma2.marchidori.maininterface.exception;

public class DatabaseConnectionException extends RuntimeException {

  // Costruttore di base
  public DatabaseConnectionException() {
    super();
  }

  // Costruttore con messaggio e causa sottostante (chaining dell'eccezione)
  public DatabaseConnectionException(String message, Throwable cause) {
    super(message, cause);
  }
}