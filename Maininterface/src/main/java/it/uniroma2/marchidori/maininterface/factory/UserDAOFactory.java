package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.dao.UserDAODatabase;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;
import it.uniroma2.marchidori.maininterface.dao.UserDaoMem;


// La classe UserDAOFactory segue il pattern "Factory" e "Singleton" per creare
//  e restituire istanze di UserDAO in base a specifiche condizioni
public class UserDAOFactory {

    // Singola istanza (Singleton) di UserDAOFactory
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
    //Ritorna un'istanza di UserDAO:
    public UserDAO getUserDAO(boolean useDatabase) {
        if (useDatabase) {
            if (databaseInstance == null) {
                databaseInstance = new UserDAODatabase();
            }
            return databaseInstance;
        } else {
            if (fileSysInstance == null) {
                fileSysInstance = new UserDAOFileSys();
            }
            return fileSysInstance;
        }
    }

    //@overload di getUserDAO che permette di specificare anche la modalità "demo"
    public UserDAO getUserDAO(boolean useDatabase, boolean demo) {
        UserDAO memoryInstance;
        if(demo){
            memoryInstance = new UserDaoMem();
            return memoryInstance;
        }
        return getUserDAO(useDatabase);
    }
}