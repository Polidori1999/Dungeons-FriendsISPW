package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;

public class LoginController {

    private UserService userService;
    private UserBean currentUser;

    // ✅ Aggiungi un costruttore senza parametri per la Factory
    public LoginController() {
        this.userService = new UserService(false); // Usa `false` per default (file system)
    }

    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    public UserBean login(String email, String password) {
        return userService.loginUser(email, password);
    }
}
/*public class LoginController implements UserAwareInterface {
    private UserBean currentUser;

    public LoginController() {}

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}*/
