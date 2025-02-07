package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.UserBean;

public interface UserDAO {
    //metodi che utilizza il dao
    void saveUser(String email, String password);
    UserBean getUserByEmail(String email);

}