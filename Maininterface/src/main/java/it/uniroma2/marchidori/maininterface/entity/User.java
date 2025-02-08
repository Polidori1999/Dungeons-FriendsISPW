package it.uniroma2.marchidori.maininterface.entity;

import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;


import java.util.List;

public class User {

    private final String email;
    private RoleEnum roleBehavior;
    private List<CharacterSheet> characterSheets;  // Riferimento ai fogli dei personaggi
    private List<Lobby> favouriteLobbies;  // Riferimento alle lobby preferite
    private List<Lobby> joinedLobbies;

    // Costruttore immutabile
    public User(String email, List<CharacterSheet> characterSheets,
                List<Lobby> favouriteLobbies, List<Lobby> joinedLobbies) {
        this.email = email;
        this.characterSheets = characterSheets;
        this.favouriteLobbies = favouriteLobbies;
        this.joinedLobbies = joinedLobbies;
        this.roleBehavior = RoleEnum.PLAYER; // Default come Player
    }

    /**
     * Se vuoi poter assegnare direttamente un ruolo esterno:
     */
    public User(String email, RoleEnum initialRole, List<CharacterSheet> characterSheets, List<Lobby> favouriteLobbies, List<Lobby> joinedLobbies) {
        this.email = email;
        this.roleBehavior = initialRole;
        this.characterSheets = characterSheets;
        this.favouriteLobbies = favouriteLobbies;
        this.joinedLobbies = joinedLobbies;
    }

    // Getter e setter per i campi

    public String getEmail() {
        return email;
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

    public void setJoinedLobbies(List<Lobby> objects) {
        this.joinedLobbies = objects;
    }
}
