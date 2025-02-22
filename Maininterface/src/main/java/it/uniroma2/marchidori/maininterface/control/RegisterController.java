package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.exception.AccountAlreadyExistsException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController implements UserAwareInterface {
    private UserService userService;

    // Creazione del logger
    private static final Logger logger = Logger.getLogger(RegisterController.class.getName());


    public RegisterController() {
        this.userService=UserService.getInstance(Session.getInstance().getDB());
        if (logger.isLoggable(Level.INFO)) {
            logger.info("🔍 [DEBUG] RegisterController inizializzato.");
        }
    }


    public boolean register(String email, String password) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Chiamato register() con: %s", email));
        }

        try {
            // Chiamata al servizio che si occupa di creare un nuovo account.
            userService.registerUser(email, password);
            // Se non è stata lanciata alcuna eccezione, la registrazione è andata a buon fine.
            return true;
        } catch (AccountAlreadyExistsException e) {
            // GESTIONE dell’eccezione: log e segnalazione di fallimento
            logger.warning("Tentativo di registrazione fallito: " + e.getMessage());
            return false;
        } catch (Exception e) {
            //metti caso va male
            logger.severe("Errore imprevisto durante la registrazione: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void setCurrentUser(UserBean user) {
        // Metodo vuoto perchè implementa
    }
}
