package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.User;

import java.util.ArrayList;
import java.util.UUID;

/*public class RegisterController implements UserAwareInterface {
    private UserBean currentUser;

    public RegisterController() {}

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}*/

public class RegisterController implements UserAwareInterface {
    private UserService userService;
    private UserBean currentUser;

    // ✅ Costruttore senza parametri per la Factory
    public RegisterController() {
        this.userService = new UserService(false); // Usa false per default (file system)
    }

    public void register(String email, String password) {
        UserBean newUser = new UserBean(UUID.randomUUID().toString(), email, email, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        userService.registerUser(newUser, password);
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser=user;
    }
}
