package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.entity.Lobby;

import java.util.List;

public class LobbyFactory {
    public static List<Lobby> getLobbies() {
        return List.of(
                new Lobby("edogay", "Singola", "Presenza", 3),
                new Lobby("edonegro", "Campagna", "Online", 5),
                new Lobby("edomerda", "Campagna", "Online", 8),
                new Lobby("Gay Legends", "Singola", "Online", 6)
        );
    }
}
