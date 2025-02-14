package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;

import java.io.*;

public class UserService {
    private static UserService instance;
    private final UserDAO userDAO;
    private final UserDAOFileSys userDAOFileSys = new UserDAOFileSys();

    private UserService(boolean useDatabase) {
        this.userDAO = UserDAOFactory.getInstance().getUserDAO(useDatabase);
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


    public User loadUserData(User user) throws FileNotFoundException {
        Session.getInstance().setUserDAOFileSys(userDAOFileSys);
        return Session.getInstance().getUserDAOFileSys().loadUserData(user);
    }



}
