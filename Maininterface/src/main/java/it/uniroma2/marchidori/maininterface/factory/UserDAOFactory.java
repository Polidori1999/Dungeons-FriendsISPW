package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.dao.UserDAODatabase;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;
import it.uniroma2.marchidori.maininterface.dao.UserDaoMem;

import java.util.logging.Logger;

public class UserDAOFactory {
    private static final Logger logger = Logger.getLogger(UserDAOFactory.class.getName());

    private static UserDAOFactory instance = null;
    private UserDAO fileSysInstance;
    private UserDAO databaseInstance;

    private UserDAOFactory() { }

    // Metodo per ottenere l'unica istanza della sessione
    public static synchronized UserDAOFactory getInstance() {
        if (instance == null) {
            instance = new UserDAOFactory();
        }
        return instance;
    }

    public UserDAO getUserDAO(boolean useDatabase) {
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


    public UserDAO getUserDAO(boolean useDatabase, boolean demo) {
        UserDAO memoryInstance;
        if(demo){
            memoryInstance = new UserDaoMem();
            return memoryInstance;
        }
        return getUserDAO(useDatabase);
    }
}