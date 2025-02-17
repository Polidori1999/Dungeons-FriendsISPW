package it.uniroma2.marchidori.maininterface.exception;

public class UserDataAccessException extends RuntimeException {
    public UserDataAccessException() {
        super();
    }

    public UserDataAccessException(String message) {
        super(message);
    }

    public UserDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDataAccessException(Throwable cause) {
        super(cause);
    }
}
