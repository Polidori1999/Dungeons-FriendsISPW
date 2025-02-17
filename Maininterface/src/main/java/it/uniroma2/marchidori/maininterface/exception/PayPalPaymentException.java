package it.uniroma2.marchidori.maininterface.exception;

public class PayPalPaymentException extends Exception {
    public PayPalPaymentException(String message) {
        super(message);
    }

    public PayPalPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}

