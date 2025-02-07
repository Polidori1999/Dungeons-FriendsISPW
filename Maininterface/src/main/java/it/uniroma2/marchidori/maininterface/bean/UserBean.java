package it.uniroma2.marchidori.maininterface.bean;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import java.util.ArrayList;
import java.util.List;
import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;

public class UserBean {

    private String email;
    // Aggiungi il campo password (hashata)
    private transient String password; // transient se non vuoi esporlo in serializzazione
    private RoleEnum roleBehavior;
    private List<LobbyBean> favouriteLobbies;
    private String selectedLobbyName;
    private List<LobbyBean> joinedLobbies;
    private List<CharacterSheetBean> characterSheets;

    // Costruttore "base" (senza password) per altri usi
    public UserBean(String email, List<LobbyBean> favouriteLobbies, List<LobbyBean> joinedLobbies, List<CharacterSheetBean> characterSheets) {
        this.email = email;
        this.favouriteLobbies = favouriteLobbies;
        this.joinedLobbies = joinedLobbies;
        this.roleBehavior = PLAYER;
        this.characterSheets = (characterSheets != null) ? characterSheets : new ArrayList<>();
    }

    // Costruttore per l'autenticazione, include la password hashata
    public UserBean(String email, String password, RoleEnum initialRole, List<LobbyBean> joinedLobbies, List<LobbyBean> favouriteLobbies, List<CharacterSheetBean> characterSheets) {

        this.email = email;
        this.password = password;
        this.roleBehavior = initialRole;
        this.joinedLobbies = joinedLobbies;
        this.favouriteLobbies = favouriteLobbies;
        this.characterSheets = characterSheets;
    }

    // Getter e Setter
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public RoleEnum getRoleBehavior() {
        return roleBehavior;
    }
    public void setRoleBehavior(RoleEnum roleBehavior) {
        this.roleBehavior = roleBehavior;
    }
    public List<LobbyBean> getFavouriteLobbies() {
        return favouriteLobbies;
    }
    public void setFavouriteLobbies(List<LobbyBean> favouriteLobbies) {
        this.favouriteLobbies = favouriteLobbies;
    }
    public String getSelectedLobbyName() {
        return selectedLobbyName;
    }
    public void setSelectedLobbyName(String selectedLobbyName) {
        this.selectedLobbyName = selectedLobbyName;
    }
    public List<LobbyBean> getJoinedLobbies() {
        return joinedLobbies;
    }
    public void setJoinedLobbies(List<LobbyBean> joinedLobbies) {
        this.joinedLobbies = joinedLobbies;
    }
    public List<CharacterSheetBean> getCharacterSheets() {
        return characterSheets;
    }
    public void setCharacterSheets(List<CharacterSheetBean> characterSheets) {
        this.characterSheets = characterSheets;
    }
}
