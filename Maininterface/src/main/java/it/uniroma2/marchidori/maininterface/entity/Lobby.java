package it.uniroma2.marchidori.maininterface.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity "Lobby": dati e logica di dominio.
 * Fatta in questo modo con finale e solo getter la lobby è immutabile
 *
 */
public class Lobby {
    private final String lobbyName;
    private final String duration;  // "Singola"/"Campagna"
    private final String type;      // "Online"/"Presenza"
    private final boolean owned;
    private List<String> players;
    private int numberOfPlayers;


    private Lobby() {
        this.lobbyName = "lobbyName";
        this.duration = "duration";
        this.type = "type";
        this.owned = false;
        this.players = new ArrayList<>();
    }


    // costruttore
    public Lobby(String lobbyName, String duration, String type, boolean owned, int numberOfPlayers) {
        this.lobbyName = lobbyName;
        this.duration = duration;
        this.type = type;
        this.owned = owned;
        this.numberOfPlayers = numberOfPlayers;
        this.players = new ArrayList<>();
    }

    public String getLobbyName() {
        return lobbyName;
    }
    public String getDuration() {
        return duration;
    }
    public String getType() {
        return type;
    }

    public List<String> getPlayers(){
        return players;
    }

    public boolean isFull(){
        return players.size() >= 8;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
    public boolean isOwned() {
        return owned;
    }


}
