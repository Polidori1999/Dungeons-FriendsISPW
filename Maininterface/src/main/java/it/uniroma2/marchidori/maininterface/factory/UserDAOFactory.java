package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.dao.UserDAODatabase;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;
import java.util.logging.Logger;

public class UserDAOFactory {

    private static final Logger logger = Logger.getLogger(UserDAOFactory.class.getName());

    private UserDAOFactory() {
        // empty
    }

    public static UserDAO getUserDAO(boolean useDatabase) {
        if (useDatabase) {
            logger.info("🛠 Usando UserDAODatabase (MySQL)");
            return new UserDAODatabase();
        } else {
            logger.info("🛠 Usando UserDAOFileSys (File System)");
            return new UserDAOFileSys();
        }
    }
}