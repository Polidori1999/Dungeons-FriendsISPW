package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;

public class AuthController {

    public UserBean authenticate(String email, String password) {
        // Simula l'autenticazione
        if ("Mario".equals(email) && "123".equals(password)) {
            // Restituisce un UserBean con i dati dell'utente
            return new UserBean("1", "Mario", email, RoleEnum.PLAYER ,null);
        }
        return null; // Restituisce null se l'autenticazione fallisce
    }
}