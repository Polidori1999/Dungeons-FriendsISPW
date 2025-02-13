package it.uniroma2.marchidori.maininterface.repository;

import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.factory.LobbyFactory;
import java.util.ArrayList;
import java.util.List;

////////////////////////////////////singleton/////////////////////////////////////////
public class LobbyRepository {

    private LobbyRepository(){}
    // Rendi la lista statica
    private static List<Lobby> lobbyList = new ArrayList<>();
    // Inizializza la lista in un blocco statico
    static {
        // Inizializziamo con lobby statiche per ora
        lobbyList.add(LobbyFactory.createLobby("edogay", "Singola", "Presenza", 3, "","maps.example.com/?q=edogay", new ArrayList<>(2)));
        lobbyList.add(LobbyFactory.createLobby("edonegro", "Campagna", "Presenza", 5, "","maps.example.com/?q=edonegro", new ArrayList<>(4)));
        lobbyList.add(LobbyFactory.createLobby("edomerda", "Campagna", "Online", 8, "","discord.gg/edomerda",new ArrayList<>(6)));
        lobbyList.add(LobbyFactory.createLobby("Gay Legends", "Singola", "Online", 6, "","discord.gg/GayLegends", new ArrayList<>(4)));
    }

    /**
     * Restituisce tutte le lobby disponibili.
     */
    public static List<Lobby> getAllLobbies() {
        return new ArrayList<>(lobbyList); // Ritorniamo una copia per evitare modifiche dirette
    }

    /**
     * Trova una lobby per nome.
     */
    public static Lobby findLobbyByName(String name) {
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
    public static void addLobby(Lobby lobby) {
        lobbyList.add(lobby);
    }

    /**
     * Rimuove una lobby per nome.
     */
    public static void removeLobby(String name) {
        lobbyList.removeIf(lobby -> lobby.getLobbyName().equals(name));
    }
}