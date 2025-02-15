package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.entity.User;

import java.io.FileNotFoundException;


public interface UserDAO {
    void saveUser(String email, String password);
    User getUserByEmail(String email);

    void updateUsersEntityData(User currentEntity);

    public User loadUserData(User user) throws FileNotFoundException;
}