package it.uniroma2.marchidori.maininterface.entity;


public class Session {

    // Campo statico che contiene l'utente corrente
    private static User currentUser;

    // Imposta l'utente corrente
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Recupera l'utente corrente
    public static User getCurrentUser() {
        return currentUser;
    }

    // Metodo per pulire la sessione (ad es. logout)
    public static void clear() {
        currentUser = null;
    }
}
