package it.uniroma2.marchidori.maininterface.entity;

import java.util.List;

public class Player {

    private final String id;
    private final String username;
    //private final List<CharacterSheet> characterSheets;  // Riferimento ai fogli dei personaggi
    private final List<it.uniroma2.marchidori.maininterface.entity.Lobby> favouriteLobbies;  // Riferimento alle lobby preferite
   // private final List<Notification> notificationPlayer;  // Riferimento alle notifiche

    // Costruttore immutabile
    public Player(String id, String username, //List<CharacterSheet> characterSheets,
                  List<it.uniroma2.marchidori.maininterface.entity.Lobby> favouriteLobbies)/*, List<Notification> notificationPlayer)*/ {
        this.id = id;
        this.username = username;
       // this.characterSheets = characterSheets;
        this.favouriteLobbies = favouriteLobbies;
        //this.notificationPlayer = notificationPlayer;
    }

    // Getter
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    /*public List<CharacterSheet> getCharacterSheets() {
        return characterSheets;
    }*/

    public List<it.uniroma2.marchidori.maininterface.entity.Lobby> getFavouriteLobbies() {
        return favouriteLobbies;
    }

    /*public List<Notification> getNotificationPlayer() {
        return notificationPlayer;
    }*/
}
