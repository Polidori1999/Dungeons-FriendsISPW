package it.uniroma2.marchidori.maininterface.exception;

public class PopupLoadingException extends RuntimeException {

    public PopupLoadingException() {
        super();
    }
    public PopupLoadingException(String message) {

        super(message);
    }
    public PopupLoadingException(Throwable cause) {
        super(cause);
    }
}
