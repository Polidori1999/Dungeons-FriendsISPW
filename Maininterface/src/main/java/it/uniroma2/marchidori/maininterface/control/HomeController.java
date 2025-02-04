package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;

public class HomeController implements UserAwareInterface {
    private UserBean currentUser;

    public HomeController() {}

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
