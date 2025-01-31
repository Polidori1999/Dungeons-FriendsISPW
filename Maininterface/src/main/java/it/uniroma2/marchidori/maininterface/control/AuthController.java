package it.uniroma2.marchidori.maininterface.control;

public class AuthController {
    public boolean authenticate(String email, String password) {
        // Qui potresti mettere la logica di autenticazione vera e propria (per esempio, verificare contro un database)
        return email.equals("Mario") && password.equals("123");
    }
}
