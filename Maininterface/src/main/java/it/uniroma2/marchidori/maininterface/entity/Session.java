package it.uniroma2.marchidori.maininterface.entity;

public class Session {

    // L'istanza singleton
    private static Session instance = null;

    // Campo che contiene l'utente corrente (non statico)
    private User currentUser;
    private boolean isCLI;

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
}
