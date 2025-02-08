package it.uniroma2.marchidori.maininterface.exception;

/**
 * Eccezione dedicata per gestire gli errori di connessione al database.
 * Estende RuntimeException così che sia unchecked.
 */
public class DatabaseConnectionException extends RuntimeException {

  // Costruttore di base
  public DatabaseConnectionException() {
    super();
  }

  // Costruttore con messaggio di errore personalizzato
  public DatabaseConnectionException(String message) {
    super(message);
  }

  // Costruttore con messaggio e causa sottostante (chaining dell'eccezione)
  public DatabaseConnectionException(String message, Throwable cause) {
    super(message, cause);
  }

  // Costruttore con sola causa sottostante
  public DatabaseConnectionException(Throwable cause) {
    super(cause);
  }
}
