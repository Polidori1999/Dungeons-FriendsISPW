package it.uniroma2.marchidori.maininterface.bean;

import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;


import java.util.List;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;
import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.DM;


public class UserBean {

    private String id;
    private String username;
    private String email;
    private RoleEnum roleBehavior;
    private List<it.uniroma2.marchidori.maininterface.entity.Lobby> favouriteLobbies;

    public UserBean(String id, String username, String email,
                List<Lobby> favouriteLobbies)/*,List<CharacterSheet> characterSheets, List<Notification> notificationPlayer)*/ {
        this.id = id;
        this.username = username;
        this.email = email;
        this.favouriteLobbies = favouriteLobbies;
        this.roleBehavior = PLAYER; // Default come Player

        //DA IMPLEMENTARE
        //this.characterSheets = characterSheets;
        //this.notificationPlayer = notificationPlayer;
    }

    /**
     * Se vuoi poter assegnare direttamente un ruolo esterno:
     */
    public UserBean(String id, String username, String email, RoleEnum initialRole, List<Lobby> favouriteLobbies) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roleBehavior = initialRole;
        this.favouriteLobbies = favouriteLobbies;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Lobby> getFavouriteLobbies() {
        return favouriteLobbies;
    }

    public void setFavouriteLobbies(List<Lobby> favouriteLobbies) {
        this.favouriteLobbies = favouriteLobbies;
    }

    public RoleEnum getRoleBehavior() {
        return roleBehavior;
    }

    public void setRoleBehavior(RoleEnum roleBehavior) {
        this.roleBehavior = roleBehavior;
    }

    public boolean accessDMTool(){
        return roleBehavior == DM;
    }
    public void switchRole() {
        if(this.roleBehavior == PLAYER) {
            this.roleBehavior = DM;
        } else if (this.roleBehavior == DM) {
            this.roleBehavior = PLAYER;
        }
    }
}
