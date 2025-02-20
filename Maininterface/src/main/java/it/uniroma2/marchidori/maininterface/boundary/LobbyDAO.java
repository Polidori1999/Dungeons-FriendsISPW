package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.entity.Lobby;

import java.io.IOException;
import java.util.List;

public interface LobbyDAO {
    public List<Lobby> getLobby();

    public void updateLobby(Lobby lobby) throws IOException;
    public boolean deleteLobby(String lobbyName);
    public boolean addLobby(Lobby lobby);
}
