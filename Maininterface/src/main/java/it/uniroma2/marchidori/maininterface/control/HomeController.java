package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;

public class HomeController implements UserAwareInterface {
    public UserBean currentUser;
    public User currentEntity = Session.getCurrentUser();

    public HomeController() {}

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }


}
