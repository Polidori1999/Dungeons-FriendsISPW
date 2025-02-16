package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.exception.AccountAlreadyExistsException;

import java.io.FileNotFoundException;


public interface UserDAO {
    void saveUser(String email, String password)throws AccountAlreadyExistsException;
    User getUserByEmail(String email);

    void updateUsersEntityData(User currentEntity);

     User loadUserData(User user) throws FileNotFoundException;
}