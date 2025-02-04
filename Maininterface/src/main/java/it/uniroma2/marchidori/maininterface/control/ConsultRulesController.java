package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;

public class ConsultRulesController implements UserAwareInterface {
    private UserBean currentUser;

    public ConsultRulesController() {}

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
