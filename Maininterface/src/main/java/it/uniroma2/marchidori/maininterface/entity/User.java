package it.uniroma2.marchidori.maininterface.entity;

import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;


import java.util.List;

public class User {

    private final String email; //email
    private String password;
    private RoleEnum roleBehavior; //PLAYER || DM
    private List<CharacterSheet> characterSheets;  // lista delle schede personaggi
    private List<Lobby> favouriteLobbies;  // lista delle lobby preferite
    private List<Lobby> joinedLobbies; // lista delle lobby a cui partecipa

    // Costruttore con parametri "base"
    public User(String email,String password, List<CharacterSheet> characterSheets,
                List<Lobby> favouriteLobbies, List<Lobby> joinedLobbies) {
        this.email = email;
        this.password=password;
        this.characterSheets = characterSheets;
        this.favouriteLobbies = favouriteLobbies;
        this.joinedLobbies = joinedLobbies;
        this.roleBehavior = RoleEnum.PLAYER; // Default come Player
    }


    //costruttore per assegnare direttamente un ruolo che non sia il default: PLAYER
    public User(String email, RoleEnum initialRole, List<CharacterSheet> characterSheets, List<Lobby> favouriteLobbies, List<Lobby> joinedLobbies) {
        this.email = email;

        this.roleBehavior = initialRole;
        this.characterSheets = characterSheets;
        this.favouriteLobbies = favouriteLobbies;
        this.joinedLobbies = joinedLobbies;
    }

    // Getter & setter

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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
