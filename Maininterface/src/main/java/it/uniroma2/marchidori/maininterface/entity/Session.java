package it.uniroma2.marchidori.maininterface.entity;

import it.uniroma2.marchidori.maininterface.boundary.LobbyDAO;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;

public class Session {

    // L'istanza singleton
    private static Session instance = null;

    // Campo che contiene l'utente corrente (non statico)
    private User currentUser;
    private UserDAO userDAO;
    private LobbyDAO lobbyDAO;
    private boolean isCLI = false;
    private boolean isDB = false;
    private boolean isDemo = false;

    // Costruttore privato per evitare istanziazioni esterne
    private Session() { }

    // Metodo per ottenere l'unica istanza della sessione
    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // Imposta l'utente corrente (metodo d'istanza)
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Recupera l'utente corrente (metodo d'istanza)
    public User getCurrentUser() {
        return this.currentUser;
    }

    // Pulisce la sessione (ad es. logout)
    public void clear() {
        this.currentUser = null;
    }
    public boolean getCLI() {
        return isCLI;
    }
    public void setCLI(boolean isCLI) {
        this.isCLI = isCLI;
    }
    public boolean getDB() {
        return isDB;
    }
    public void setDB(boolean isDB) {
        this.isDB = isDB;
    }
    public boolean getDemo() {
        return isDemo;
    }
    public void setDemo(boolean isDemo) {
        this.isDemo = isDemo;
    }
    public UserDAO getUserDAO() {
        return userDAO;
    }
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public void setLobbyDAO(LobbyDAO lobbyDAO) {this.lobbyDAO = lobbyDAO;}
    public LobbyDAO getLobbyDAO() {return lobbyDAO;}

}
