package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;

public class UserDAODatabase implements UserDAO {
    @Override
    public void saveUser(String email, String password) {
        // Logica per salvare utente su database
    }

    public UserBean getUserByEmail(String email) {
        //logica
        return null;
    }
}
