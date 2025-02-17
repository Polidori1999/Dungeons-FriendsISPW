package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;

import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileNotFoundException;

import java.util.logging.Logger;

public class LoginController implements UserAwareInterface {

    private UserService userService;

    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    UserBean currentUser;

    public LoginController() {
        this.userService = UserService.getInstance(Session.getInstance().getDB());
    }

    // Metodo dell'interfaccia UserAwareInterface
    public void setCurrentUser(UserBean userBean) {
        this.currentUser = userBean;
    }

    public User login(String email, String password) throws FileNotFoundException {


        // Recupera l'utente tramite il servizio; il DAO carica tutte le info (comprese le lobby)
        User retrievedUser = userService.getUserByEmail(email);

        if (retrievedUser == null) {
            return null;
        }


        // Verifica la password usando BCrypt
        if (BCrypt.checkpw(password, retrievedUser.getPassword())) {
            if(Session.getInstance().getDemo()){
                logger.info("demo true!");
                retrievedUser = userService.loadUserDataDemo(email);
            }else{
                logger.info("demo false!");
                retrievedUser = userService.loadUserData(retrievedUser);
            }
            // Converte l'entity User in un UserBean per l'interfaccia utente

            retrievedUser.setRoleBehavior(RoleEnum.PLAYER);
            UserBean convertedUser = Converter.convert(retrievedUser);
            logger.info("🔄 Conversione User -> UserBean completata: " + convertedUser.getEmail());

            // Imposta l'utente corrente (sia a livello di controller che di sessione)
            setCurrentUser(convertedUser);
            Session.getInstance().setCurrentUser(retrievedUser);

            return retrievedUser;
        } else {
            return null;
        }
    }
}
