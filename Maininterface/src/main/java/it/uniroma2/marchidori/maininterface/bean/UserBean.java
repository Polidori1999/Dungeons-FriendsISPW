package it.uniroma2.marchidori.maininterface.bean;

import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;
import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.DM;

public class UserBean {

    private String id;
    private String username;
    private String email;
    private RoleEnum roleBehavior;
    private List<Lobby> favouriteLobbies;
    private String selectedLobbyName;
    private List<Lobby> joinedLobbies;
    private List<CharacterSheet> characterSheets; // Lista di personaggi associati all'utente
    // Aggiungi anche notifiche, se necessario:
    // private List<Notification> notificationPlayer;

    // Costruttore che include i personaggi
    public UserBean(String id, String username, String email, List<Lobby> favouriteLobbies, List<Lobby> joinedLobbies, List<CharacterSheet> characterSheets) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.favouriteLobbies = favouriteLobbies;
        this.joinedLobbies = joinedLobbies;
        this.roleBehavior = PLAYER; // Default come Player
        // Se characterSheets è null, inizializzala a una nuova ArrayList
        this.characterSheets = (characterSheets != null) ? characterSheets : new ArrayList<>();
    }


    // Costruttore con ruolo esterno
    public UserBean(String id, String username, String email, RoleEnum initialRole, List<Lobby> favouriteLobbies, List<CharacterSheet> characterSheets) {
        this.id = id;
        this.username = username;
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

    public List<CharacterSheet> getCharacterSheets() {
        return characterSheets;
    }

    public void setCharacterSheets(List<CharacterSheet> characterSheets) {
        this.characterSheets = characterSheets;
    }

    public List<Lobby> getJoinedLobbies() {
        return joinedLobbies;
    }
    public void setJoinedLobbies(List<Lobby> joinedLobbies) {
        this.joinedLobbies = joinedLobbies;
    }

    // Metodo per aggiungere un nuovo personaggio
    public void addCharacterSheet(CharacterSheet characterSheet) {
        if (this.characterSheets == null) {
            System.err.println(">>> ERRORE: Lista personaggi è NULL!");
            this.characterSheets = new ArrayList<>();
        }
        this.characterSheets.add(characterSheet);
        System.out.println(">>> Personaggio aggiunto! Lista aggiornata: " + this.characterSheets);
    }

    // Metodo per aggiungere un nuovo personaggio
    public void addLobby(Lobby lobby) {
        if (this.joinedLobbies == null) {
            System.err.println(">>> ERRORE: Lista lobby è NULL!");
            this.joinedLobbies = new ArrayList<>();
        }
        this.joinedLobbies.add(lobby);
        System.out.println(">>> lobbyo aggiunto! Lista aggiornata: " + this.joinedLobbies);
    }

    // Metodo per aggiungere un nuovo personaggio
    public void addLobbyToFavourite(Lobby lobby) {
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
        return favouriteLobbies.removeIf(lobby -> lobby.getLobbyName().equals(name));
    }





    // Metodo per rimuovere un personaggio (per nome, per esempio)
    public void removeCharacterSheet(String characterName) {
        characterSheets.removeIf(cs -> cs.getName().equals(characterName));
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
            characterSheets.forEach(cs -> System.out.println("Personaggio: " + cs.getName()));
        }
    }

    public void updateCharacterSheet(CharacterSheet updatedCharacter) {
        for (int i = 0; i < characterSheets.size(); i++) {
            if (characterSheets.get(i).getName().equals(updatedCharacter.getName())) {
                characterSheets.set(i, updatedCharacter); // **Sostituisce il vecchio personaggio**
                System.out.println(">>> DEBUG: Personaggio aggiornato nello UserBean: " + updatedCharacter.getName());
                return;
            }
        }
        characterSheets.add(updatedCharacter); // Se non esiste, lo aggiunge
    }



}
