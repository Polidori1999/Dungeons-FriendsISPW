package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;

public class UserService {
    private final UserDAO userDAO;

    public UserService(boolean useDatabase) {
        this.userDAO = UserDAOFactory.getUserDAO(useDatabase);
    }

    public void registerUser(String email, String password) {
        userDAO.saveUser(email, password);
    }

    // Metodo per recuperare l'utente basandosi sull'email
    public UserBean getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }
}
