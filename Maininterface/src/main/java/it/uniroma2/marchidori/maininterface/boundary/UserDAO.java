package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.UserBean;

import java.util.List;

public interface UserDAO {
    //metodi che utilizza il dao
    void saveUser(String email, String password);
    UserBean getUserByEmail(String email);
    List<String> getUserLobbies(String email);
}