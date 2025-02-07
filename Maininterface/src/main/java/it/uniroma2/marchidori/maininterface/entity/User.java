package it.uniroma2.marchidori.maininterface.entity;

import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;

import java.util.ArrayList;
import java.util.List;

public class User {


    private final String id;
    private final String email;
    private transient String password;
    private RoleEnum roleBehavior;
    private List<CharacterSheet> characterSheets;  // Riferimento ai fogli dei personaggi
    private List<Lobby> favouriteLobbies;  // Riferimento alle lobby preferite
    private List<Lobby> joinedLobbies;

    // Riferimento alle lobby preferite
    // private final List<Notification> notificationPlayer;  // Riferimento alle notifiche

    // Costruttore immutabile
    public User(String id, String email, List<CharacterSheet> characterSheets,
                List<it.uniroma2.marchidori.maininterface.entity.Lobby> favouriteLobbies, List<Lobby> joinedLobbies)/*,List<CharacterSheet> characterSheets, List<Notification> notificationPlayer)*/ {
        this.id = id;
        this.email = email;
        this.characterSheets = characterSheets;
        this.favouriteLobbies = favouriteLobbies;
        this.joinedLobbies = joinedLobbies;
        this.roleBehavior = RoleEnum.PLAYER; // Default come Player

        //DA IMPLEMENTARE
        //this.characterSheets = characterSheets;
        //this.notificationPlayer = notificationPlayer;
    }

    /**
     * Se vuoi poter assegnare direttamente un ruolo esterno:
     */
    public User(String id, String email,String password, RoleEnum initialRole, List<CharacterSheet> characterSheets, List<Lobby> favouriteLobbies, List<Lobby> joinedLobbies) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roleBehavior = initialRole;
        this.characterSheets = characterSheets;
        this.favouriteLobbies = favouriteLobbies;
        this.joinedLobbies = joinedLobbies;
    }

    /**
     * Esempio di metodo per passare da Player a DM
     * o viceversa a runtime.
     */

    public String getEmail() {
        return email;
    }



    public String getId() {
        return id;
    }

    public RoleEnum getRoleBehavior() {
        return roleBehavior;
    }

    public void setRoleBehavior(RoleEnum roleBehavior) {
        this.roleBehavior = roleBehavior;
    }

    public List<Lobby> getFavouriteLobbies() {
        return favouriteLobbies;
    }

    public List<CharacterSheet> getCharacterSheets() {
        return characterSheets;
    }

    public List<Lobby> getJoinedLobbies() {
        return joinedLobbies;
    }

    public void setFavouriteLobbies(List<Lobby> favouriteLobbies) {
        this.favouriteLobbies = favouriteLobbies;
    }

    public void setJoinedLobbies(ArrayList<Lobby> objects) {
        this.joinedLobbies = objects;
    }
}
