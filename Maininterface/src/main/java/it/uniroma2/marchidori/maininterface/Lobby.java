package it.uniroma2.marchidori.entity;

import java.util.List;

public class Lobby {

    private final String lobbyName;
    private final List<String> playerIds; // Solo ID dei giocatori, non Player direttamente
    private final String type;

    // Costruttore immutabile
    public Lobby(String lobbyName, List<String> playerIds, String type) {
        this.lobbyName = lobbyName;
        this.playerIds = playerIds;
        this.type = type;
    }

    // Metodi di accesso solo per leggere i dati (no setter)
    public String getLobbyName() {
        return lobbyName;
    }

    public List<String> getPlayerIds() {
        return playerIds;
    }

    public String getType() {
        return type;
    }
}
