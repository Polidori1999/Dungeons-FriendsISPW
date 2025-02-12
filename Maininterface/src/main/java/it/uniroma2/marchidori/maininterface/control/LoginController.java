package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.control.UserService;  // oppure il package corretto
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController implements UserAwareInterface {

    private UserService userService;
    private UserBean userBean;  // Bean usato dall'interfaccia
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    private UserBean currentUser;

    public LoginController() {
        this.userService = UserService.getInstance(false);
    }

    // Metodo dell'interfaccia UserAwareInterface
    public void setCurrentUser(UserBean userBean) {
        this.currentUser = userBean;
    }

    public User login(String email, String password) throws FileNotFoundException {
        logger.info("🔍 Tentativo di login per: " + email);

        // Recupera l'utente tramite il servizio; il DAO carica tutte le info (comprese le lobby)
        User retrievedUser = userService.getUserByEmail(email);

        if (retrievedUser == null) {
            logger.severe("❌ Utente non trovato per: " + email);
            return null;
        }

        logger.info("🔄 Utente trovato: " + retrievedUser.getEmail());

        // Verifica la password usando BCrypt
        if (BCrypt.checkpw(password, retrievedUser.getPassword())) {
            logger.info("✅ Password corretta per: " + email);

            //retrievedUser = userService.loadUserData(retrievedUser);

            // Se il DAO ha caricato le joined lobbies, possiamo usarle direttamente
            /*if (retrievedUser.getJoinedLobbies() != null) {
                logger.info("🔄 Numero di lobby joinate: " + retrievedUser.getJoinedLobbies().size());
            }*/

            // Converte l'entity User in un UserBean per l'interfaccia utente
            UserBean convertedUser = Converter.convert(retrievedUser);
            logger.info("🔄 Conversione User -> UserBean completata: " + convertedUser.getEmail());

            // Imposta l'utente corrente (sia a livello di controller che di sessione)
            setCurrentUser(convertedUser);
            Session.getInstance().setCurrentUser(retrievedUser);

            return retrievedUser;
        } else {
            logger.severe("❌ Password errata per: " + email);
            return null;
        }
    }
}
