package it.uniroma2.marchidori.maininterface.exception;

public class JoinLobbyException extends RuntimeException {
    public JoinLobbyException(String message) {
        super(message);
    }

    public JoinLobbyException(String message, Throwable cause) {
        super(message, cause);
    }
}
