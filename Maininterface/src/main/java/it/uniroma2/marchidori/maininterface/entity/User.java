package it.uniroma2.marchidori.maininterface.entity;

import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;

import java.util.List;

public class User {

    private final String id;
    private final String username;
    private final String email;
    private RoleEnum roleBehavior;
    //private final List<CharacterSheet> characterSheets;  // Riferimento ai fogli dei personaggi
    private final List<it.uniroma2.marchidori.maininterface.entity.Lobby> favouriteLobbies;  // Riferimento alle lobby preferite
    // private final List<Notification> notificationPlayer;  // Riferimento alle notifiche

    // Costruttore immutabile
    public User(String id, String username, String email,
                List<it.uniroma2.marchidori.maininterface.entity.Lobby> favouriteLobbies)/*,List<CharacterSheet> characterSheets, List<Notification> notificationPlayer)*/ {
        this.id = id;
        this.username = username;
        this.email = email;
        this.favouriteLobbies = favouriteLobbies;
        this.roleBehavior = RoleEnum.PLAYER; // Default come Player

        //DA IMPLEMENTARE
        //this.characterSheets = characterSheets;
        //this.notificationPlayer = notificationPlayer;
    }

    /**
     * Se vuoi poter assegnare direttamente un ruolo esterno:
     */
    public User(String id, String username, String email, RoleEnum initialRole, List<Lobby> favouriteLobbies) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roleBehavior = initialRole;
        this.favouriteLobbies = favouriteLobbies;
    }

    //DA IMPLEMENTARE
    /*public List<CharacterSheet> getCharacterSheets() {
        return characterSheets;
    }*/

    /**
     * Esempio di metodo per passare da Player a DM
     * o viceversa a runtime.
     */

    public String getEmail() {
        return email;
    }


    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    //DA IMPLEMENTARE
    /*public List<Notification> getNotificationPlayer() {
        return notificationPlayer;
    }*/
}
