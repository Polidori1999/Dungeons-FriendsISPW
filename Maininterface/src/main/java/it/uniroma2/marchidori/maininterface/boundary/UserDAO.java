package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.entity.User;


public interface UserDAO {
    void saveUser(String email, String password);
    User getUserByEmail(String email);
}