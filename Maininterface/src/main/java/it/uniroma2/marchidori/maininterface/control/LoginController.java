package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;
import it.uniroma2.marchidori.maininterface.entity.Session;
import org.mindrot.jbcrypt.BCrypt;

public class LoginController {

    private UserService userService;
    private UserBean currentUser;

    // Costruttore senza parametri
    public LoginController() {
        // true per db false per filesys
        this.userService = new UserService(false);
    }

    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    // Metodo login che esegue il controllo della password
    public UserBean login(String email, String password) {
        // Recupera l'utente (con password hash ata) tramite il DAO
        UserBean retrievedUser = userService.getUserByEmail(email);
        if (retrievedUser == null) {
            System.out.println("❌ Utente non trovato per: " + email);
            return null;
        }

        // Verifica la password usando BCrypt
        if (BCrypt.checkpw(password, retrievedUser.getPassword())) {
            System.out.println("✅ Login riuscito per: " + email);
            setCurrentUser(retrievedUser);
            Session.getInstance().setCurrentUser(Converter.userBeanToEntity(retrievedUser));
            return retrievedUser;
        } else {
            System.out.println("❌ Password errata per: " + email);
            return null;
        }
    }
}
