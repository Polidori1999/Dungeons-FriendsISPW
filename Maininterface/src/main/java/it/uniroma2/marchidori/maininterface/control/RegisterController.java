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

    //non serve creare new userbean meglio metterlo in login
    public void register(String email, String password) throws AccountAlreadyExistsException {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Chiamato register() con: %s", email));
        }
        userService.registerUser(email, password);
    }

    @Override
    public void setCurrentUser(UserBean user) {
        // Metodo vuoto perchè implementa
    }
}
