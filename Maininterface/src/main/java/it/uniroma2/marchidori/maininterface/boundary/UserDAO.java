package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.UserBean;

public interface UserDAO {
    void saveUser(UserBean user, String password);
    UserBean authenticate(String email, String password);
}