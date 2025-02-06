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

    // Metodo per aggiungere un nuovo personaggio
    public void addCharacterSheet(CharacterSheetBean characterSheet) {
        if (this.characterSheets == null) {
            System.err.println(">>> ERRORE: Lista personaggi è NULL!");
            this.characterSheets = new ArrayList<>();
        }
        this.characterSheets.add(characterSheet);
        System.out.println(">>> Personaggio aggiunto! Lista aggiornata: " + this.characterSheets);
    }

    // Metodo per aggiungere un nuovo personaggio
    public void addLobby(LobbyBean lobby) {
        if (this.joinedLobbies == null) {
            System.err.println(">>> ERRORE: Lista lobby è NULL!");
            this.joinedLobbies = new ArrayList<>();
        }
        this.joinedLobbies.add(lobby);
        System.out.println(">>> lobbyo aggiunto! Lista aggiornata: " + this.joinedLobbies);
    }

    // Metodo per aggiungere un nuovo personaggio
    public void addLobbyToFavourite(LobbyBean lobby) {
        if (this.favouriteLobbies == null) {
            System.err.println(">>> ERRORE: Lista lobby è NULL!");
            this.favouriteLobbies = new ArrayList<>();
        }
        this.favouriteLobbies.add(lobby);
        System.out.println(">>> lobbyo aggiunto! Lista aggiornata: " + this.favouriteLobbies);
    }

    public boolean removeLobbyByName(String name) {
        if (this.favouriteLobbies == null || name == null) {
            return false;
        }
        // removeIf restituisce true se almeno un elemento è stato rimosso
        return favouriteLobbies.removeIf(lobby -> lobby.getName().equals(name));
    }





    // Metodo per rimuovere un personaggio (per nome, per esempio)
    public void removeCharacterSheet(String characterName) {
        characterSheets.removeIf(cs -> cs.getInfoBean().getName().equals(characterName));
    }
    // Metodo per rimuovere un personaggio (per nome, per esempio)


    // Verifica se l'utente è un DM
    public boolean accessDMTool(){
        return roleBehavior == DM;
    }

    // Metodo per cambiare il ruolo tra PLAYER e DM
    public void switchRole() {
        if(this.roleBehavior == PLAYER) {
            this.roleBehavior = DM;
        } else if (this.roleBehavior == DM) {
            this.roleBehavior = PLAYER;
        }
    }

    // Possibile metodo per visualizzare i personaggi associati
    public void displayCharacterSheets() {
        if (characterSheets.isEmpty()) {
            System.out.println("L'utente non ha personaggi.");
        } else {
            characterSheets.forEach(cs -> System.out.println("Personaggio: " + cs.getInfoBean().getName()));
        }
    }

    public void updateCharacterSheet(CharacterSheetBean updatedCharacter) {
        for (int i = 0; i < characterSheets.size(); i++) {
            if (characterSheets.get(i).getInfoBean().getName().equals(updatedCharacter.getInfoBean().getName())) {
                characterSheets.set(i, updatedCharacter); // **Sostituisce il vecchio personaggio**
                System.out.println(">>> DEBUG: Personaggio aggiornato nello UserBean: " + updatedCharacter.getInfoBean().getName());
                return;
            }
        }
        characterSheets.add(updatedCharacter); // Se non esiste, lo aggiunge
    }



}
