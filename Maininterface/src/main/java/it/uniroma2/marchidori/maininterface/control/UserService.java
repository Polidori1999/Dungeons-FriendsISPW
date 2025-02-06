package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;

class UserService {
    private final UserDAO userDAO;
    private AuthController authController;


    public UserService(boolean useDatabase) {
        this.userDAO = UserDAOFactory.getUserDAO(useDatabase);
    }

    public void registerUser(String email, String password) {
        userDAO.saveUser(email, password);
    }

    public UserBean loginUser(String email, String password) {
        return authController.authenticate(email, password);
    }
}