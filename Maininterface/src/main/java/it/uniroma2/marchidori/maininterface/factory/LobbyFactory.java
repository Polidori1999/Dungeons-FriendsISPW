package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.entity.Lobby;


public class LobbyFactory {
    private LobbyFactory() {
        //no istanziazione diretta
    }
    public static Lobby createLobby(String name, String duration, String type, int initialPlayers, boolean owned) {
        return new Lobby(name, duration, type, owned, initialPlayers);
    }
}
