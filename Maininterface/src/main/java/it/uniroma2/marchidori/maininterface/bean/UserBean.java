package it.uniroma2.marchidori.maininterface.bean;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;
import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.DM;

public class UserBean {

    private String id;

    private String email;
    private RoleEnum roleBehavior;
    private List<LobbyBean> favouriteLobbies;
    private String selectedLobbyName;
    private List<LobbyBean> joinedLobbies;
    private List<CharacterSheetBean> characterSheets; // Lista di personaggi associati all'utente
    // Aggiungi anche notifiche, se necessario:
    // private List<Notification> notificationPlayer;

    // Costruttore che include i personaggi
    public UserBean(String id, String email, List<LobbyBean> favouriteLobbies, List<LobbyBean> joinedLobbies, List<CharacterSheetBean> characterSheets) {
        this.id = id;
        this.email = email;
        this.favouriteLobbies = favouriteLobbies;
        this.joinedLobbies = joinedLobbies;
        this.roleBehavior = PLAYER; // Default come Player
        // Se characterSheets è null, inizializzala a una nuova ArrayList
        this.characterSheets = (characterSheets != null) ? characterSheets : new ArrayList<>();
    }


    // Costruttore con ruolo esterno
    public UserBean(String id, String email, RoleEnum initialRole, List<LobbyBean> joinedLobbies, List<LobbyBean> favouriteLobbies, List<CharacterSheetBean> characterSheets) {
        this.id = id;

        this.email = email;
        this.roleBehavior = initialRole;
        this.favouriteLobbies = favouriteLobbies;
        this.characterSheets = characterSheets;
    }


    public String getSelectedLobbyName() {
        return selectedLobbyName;
    }

    public void setSelectedLobbyName(String selectedLobbyName) {
        this.selectedLobbyName = selectedLobbyName;
    }
    // Getter e Setter

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<LobbyBean> getFavouriteLobbies() {
        return favouriteLobbies;
    }

    public void setFavouriteLobbies(List<LobbyBean> favouriteLobbies) {
        this.favouriteLobbies = favouriteLobbies;
    }

    public RoleEnum getRoleBehavior() {
        return roleBehavior;
    }

    public void setRoleBehavior(RoleEnum roleBehavior) {
        this.roleBehavior = roleBehavior;
    }

    public List<CharacterSheetBean> getCharacterSheets() {
        return characterSheets;
    }

    public void setCharacterSheets(List<CharacterSheetBean> characterSheets) {
        this.characterSheets = characterSheets;
    }

    public List<LobbyBean> getJoinedLobbies() {
        return joinedLobbies;
    }
    public void setJoinedLobbies(List<LobbyBean> joinedLobbies) {
        this.joinedLobbies = joinedLobbies;
    }


}
