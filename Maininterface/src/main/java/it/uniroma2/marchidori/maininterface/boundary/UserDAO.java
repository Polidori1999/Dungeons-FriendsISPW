package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.UserBean;

public interface UserDAO {
    void saveUser(String email, String password);

}