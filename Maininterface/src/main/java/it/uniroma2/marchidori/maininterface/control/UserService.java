package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;

import java.io.*;

public class UserService {
    private static UserService instance;
    private final UserDAO userDAO;

    private UserService(boolean useDatabase) {
        if (Session.getInstance().getDemo()) {
            this.userDAO = UserDAOFactory.getInstance().getUserDAO(useDatabase, Session.getInstance().getDemo());
        } else {
            this.userDAO = UserDAOFactory.getInstance().getUserDAO(useDatabase);
        }
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
        Session.getInstance().setUserDAO(UserDAOFactory.getInstance().getUserDAO(false));
        return Session.getInstance().getUserDAOFileSys().loadUserData(user);
    }


    public User loadUserDataDemo(String email){
        Session.getInstance().setUserDAO(userDAO);
        return getUserByEmail(email);
    }
}
