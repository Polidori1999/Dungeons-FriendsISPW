package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.entity.User;

public interface UserAwareInterface {
    void setCurrentUser(UserBean user);
}