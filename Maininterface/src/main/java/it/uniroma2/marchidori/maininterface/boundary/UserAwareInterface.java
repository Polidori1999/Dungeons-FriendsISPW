package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.UserBean;

public interface UserAwareInterface {
    void setCurrentUser(UserBean user);
}