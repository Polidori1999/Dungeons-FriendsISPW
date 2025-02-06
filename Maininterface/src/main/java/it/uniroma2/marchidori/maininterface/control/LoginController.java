package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;

public class LoginController {

    private UserService userService;
    private UserBean currentUser;
    private UserDAOFileSys userDAOFileSys;

    // ✅ Aggiungi un costruttore senza parametri per la Factory
    public LoginController() {
        this.userService = new UserService(false); // Usa `false` per default (file system)
    }

    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    public UserBean login(String email, String password) {
        UserBean authenticatedUser = userService.loginUser(email, password);
        if (authenticatedUser != null) {
            setCurrentUser(authenticatedUser);
        }
        return authenticatedUser;
    }


}


    //bean to entity


/*public class LoginController implements UserAwareInterface {
    private UserBean currentUser;

    public LoginController() {}

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}*/
