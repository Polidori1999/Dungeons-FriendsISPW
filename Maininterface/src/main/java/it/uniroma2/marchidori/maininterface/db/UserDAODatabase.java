package it.uniroma2.marchidori.maininterface.db;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;

class UserDAODatabase implements UserDAO {
    @Override
    public void saveUser(UserBean user, String password) {
        // Logica per salvare utente su database
    }

    @Override
    public UserBean authenticate(String email, String password) {
        // Logica per autenticare utente da database
        return null;
    }
}