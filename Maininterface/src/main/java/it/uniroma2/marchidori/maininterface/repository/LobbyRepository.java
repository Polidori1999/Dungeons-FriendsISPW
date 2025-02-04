package it.uniroma2.marchidori.maininterface.repository;

import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.factory.LobbyFactory;

import java.util.ArrayList;
import java.util.List;

public class LobbyRepository {
    private final List<Lobby> lobbyList;

    public LobbyRepository() {
        this.lobbyList = new ArrayList<>();
        // Inizializziamo con lobby statiche per ora
        lobbyList.add(LobbyFactory.createLobby("edogay", "Singola", "Presenza", 3));
        lobbyList.add(LobbyFactory.createLobby("edonegro", "Campagna", "Online", 5));
        lobbyList.add(LobbyFactory.createLobby("edomerda", "Campagna", "Online", 8));
        lobbyList.add(LobbyFactory.createLobby("Gay Legends", "Singola", "Online", 6));
    }

    /**
     * Restituisce tutte le lobby disponibili.
     */
    public List<Lobby> getAllLobbies() {
        return new ArrayList<>(lobbyList); // Ritorniamo una copia per evitare modifiche dirette
    }

    /**
     * Trova una lobby per nome.
     */
    public Lobby findLobbyByName(String name) {
        for (Lobby lobby : lobbyList) {
            if (lobby.getLobbyName().equals(name)) {
                return lobby;
            }
        }
        return null; // Se non trovata
    }

    /**
     * Aggiunge una nuova lobby alla lista.
     */
    public void addLobby(Lobby lobby) {
        lobbyList.add(lobby);
    }

    /**
     * Rimuove una lobby per nome.
     */
    public void removeLobby(String name) {
        lobbyList.removeIf(lobby -> lobby.getLobbyName().equals(name));
    }
}
