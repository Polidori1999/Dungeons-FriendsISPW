package it.uniroma2.marchidori.maininterface.exception;

public class UserDataAccessException extends RuntimeException {
    public UserDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}