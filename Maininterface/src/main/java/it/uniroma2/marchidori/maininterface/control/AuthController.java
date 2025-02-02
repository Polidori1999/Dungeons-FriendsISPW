package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import java.util.ArrayList;

public class AuthController {

    public UserBean authenticate(String email, String passString) {
        // Simula l'autenticazione
        if ("Mario".equals(email) && "123".equals(passString)) {
            // Restituisce un UserBean con i dati dell'utente.
            // Invece di passare null, passiamo due nuove liste vuote:
            return new UserBean("1", "Mario", email, RoleEnum.PLAYER, new ArrayList<Lobby>(), new ArrayList<CharacterSheet>());
        }
        return null; // Restituisce null se l'autenticazione fallisce
    }
}
