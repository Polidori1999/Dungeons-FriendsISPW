package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.dao.UserDAODatabase;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;

public class UserDAOFactory {
    public static UserDAO getUserDAO(boolean useDatabase) {
        if (useDatabase) {
            System.out.println("🛠 Usando UserDAODatabase (MySQL)");
            return new UserDAODatabase();
        } else {
            System.out.println("🛠 Usando UserDAOFileSys (File System)");
            return new UserDAOFileSys();
        }
    }
}


