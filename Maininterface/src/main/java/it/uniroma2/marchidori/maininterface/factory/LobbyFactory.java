package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.entity.Lobby;

import java.util.ArrayList;
import java.util.List;


public class LobbyFactory {
    private LobbyFactory() {
        //no istanziazione diretta
    }

    public static LobbyBean createBean() {
        return new LobbyBean("", "", "", 0, "","", new ArrayList<>());
    }

    public static Lobby createLobby(String name, String duration, String type, int initialPlayers, String owner, String infoLink, List<String> joinedPlayers) {
        return new Lobby(name, duration, type,initialPlayers , owner, infoLink, joinedPlayers);
    }
}

