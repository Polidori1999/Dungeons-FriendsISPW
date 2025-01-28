package it.uniroma2.marchidori.maininterface.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity "Lobby": dati e logica di dominio.
 */
public class Lobby {
    private final String lobbyName;
    private final String duration;  // "Singola"/"Campagna"
    private final String type;      // "Online"/"Presenza"
    private List<String> players;

    // costruttore
    public Lobby(String lobbyName, String duration, String type, int initialPlayersCount) {
        this.lobbyName = lobbyName;
        this.duration = duration;
        this.type = type;
        this.players = new ArrayList<>();
        // Per test, potresti aggiungere 'initialPlayersCount' giocatori fittizi
        for (int i = 0; i < initialPlayersCount; i++) {
            this.players.add("Player"+(i+1));
        }
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

    public void addPlayer(String player){
        players.add(player);
    }

    public boolean isFull(){
        return players.size() >= 8;
    }
}
