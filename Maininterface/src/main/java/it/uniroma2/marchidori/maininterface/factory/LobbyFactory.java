package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.entity.Lobby;




public class LobbyFactory {
    private LobbyFactory() {
        //no istanziazione diretta
    }

    public static LobbyBean createBean() {
        return new LobbyBean("", "", "", 0, "","", 0);
    }

    public static Lobby createLobby(String name, String duration, String type, int maxPlayers, String owner, String infoLink,int joinedPlayersCount) {
        return new Lobby(name, duration, type,maxPlayers , owner, infoLink, joinedPlayersCount);
    }
}

