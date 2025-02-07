package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.entity.Session;
import org.mindrot.jbcrypt.BCrypt;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {

    private UserService userService;

    // Creazione del logger come campo statico
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    // Costruttore senza parametri
    public LoginController() {
        // true per db false per filesys
        this.userService = new UserService(false);
    }

    public void setCurrentUser(UserBean user) {
        // Metodo vuoto, viene implementato solo per implementare l'interfaccia
    }

    // Metodo login che esegue il controllo della password
    public UserBean login(String email, String password) {
        // Recupera l'utente (con password hashata) tramite il DAO
        UserBean retrievedUser = userService.getUserByEmail(email);

        if (retrievedUser == null) {
            // Log solo se il livello di log permette
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe(String.format("❌ Utente non trovato per: %s", email));
            }
            return null;
        }

        // Verifica la password usando BCrypt
        if (BCrypt.checkpw(password, retrievedUser.getPassword())) {
            // Log solo se il livello di log permette
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format("✅ Login riuscito per: %s", email));
            }
            setCurrentUser(retrievedUser);
            Session.getInstance().setCurrentUser(Converter.userBeanToEntity(retrievedUser));            return retrievedUser;
        } else {
            // Log solo se il livello di log permette
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe(String.format("❌ Password errata per: %s", email));
            }
            return null;
        }
    }
}
