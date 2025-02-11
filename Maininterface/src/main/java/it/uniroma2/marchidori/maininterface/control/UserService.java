package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;

import java.io.*;
import java.util.List;

public class UserService {
    private static UserService instance;
    private final UserDAO userDAO;

    private UserService(boolean useDatabase) {
        this.userDAO = UserDAOFactory.getUserDAO(useDatabase);
    }

    public static UserService getInstance(boolean useDatabase) {
        if (instance == null) {
            instance = new UserService(useDatabase);
        }
        return instance;
    }

    public void registerUser(String email, String password) {
        userDAO.saveUser(email, password);
    }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public String serializeCharacterSheet(CharacterSheet cs){
        return userDAO.serializeCharacterSheet(cs);
    }

    public String serializeLobby(Lobby lobby){
        return userDAO.serializeLobby(lobby);
    }

    public User loadUserData(User user) throws FileNotFoundException {
        return userDAO.loadUserData(user);
    }



}
