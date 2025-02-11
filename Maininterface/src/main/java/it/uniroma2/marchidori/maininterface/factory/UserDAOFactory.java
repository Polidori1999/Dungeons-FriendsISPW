package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.dao.UserDAODatabase;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;
import java.util.logging.Logger;

public class UserDAOFactory {
    private static final Logger logger = Logger.getLogger(UserDAOFactory.class.getName());

    private static UserDAO fileSysInstance;
    private static UserDAO databaseInstance;

    private UserDAOFactory() {}

    public static UserDAO getUserDAO(boolean useDatabase) {
        if (useDatabase) {
            if (databaseInstance == null) {
                logger.info("🛢️ Creazione di UserDAODatabase (MySQL)");
                databaseInstance = new UserDAODatabase();
            }
            return databaseInstance;
        } else {
            if (fileSysInstance == null) {
                logger.info("📁 Creazione di UserDAOFileSys (File System)");
                fileSysInstance = new UserDAOFileSys();
            }
            return fileSysInstance;
        }
    }
}