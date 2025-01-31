package it.uniroma2.marchidori.maininterface.entity;

import java.util.List;

public class User {

    private final String id;
    private final String username;
    private final String email;
    private RoleBehavior roleBehavior;
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
        this.roleBehavior = new PlayerRole(); // Default come Player

        //DA IMPLEMENTARE
        //this.characterSheets = characterSheets;
        //this.notificationPlayer = notificationPlayer;
    }

    /**
     * Se vuoi poter assegnare direttamente un ruolo esterno:
     */
    public User(String id, String username, String email, RoleBehavior initialRole, List<Lobby> favouriteLobbies) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roleBehavior = initialRole;
        this.favouriteLobbies = favouriteLobbies;
    }

    // Getter
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    //DA IMPLEMENTARE
    /*public List<CharacterSheet> getCharacterSheets() {
        return characterSheets;
    }*/

    public List<it.uniroma2.marchidori.maininterface.entity.Lobby> getFavouriteLobbies() {
        return favouriteLobbies;
    }

    public RoleBehavior getRoleBehavior() {
        return roleBehavior;
    }

    public void setRoleBehavior(RoleBehavior roleBehavior) {
        this.roleBehavior = roleBehavior;
    }

    public boolean canAccessDMTools() {
        return roleBehavior.canAccessDMTools();
    }

    /**
     * Esempio di metodo per passare da Player a DM
     * o viceversa a runtime.
     */
    public void switchRole() {
        if (roleBehavior instanceof PlayerRole) {
            // Da Player a DM
            this.roleBehavior = new DMRole();
        } else if (roleBehavior instanceof DMRole) {
            // Da DM a Player
            this.roleBehavior = new PlayerRole();
        }
    }

    public String getEmail() {
        return email;
    }
    //DA IMPLEMENTARE
    /*public List<Notification> getNotificationPlayer() {
        return notificationPlayer;
    }*/
}
