package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.boundary.LobbyDAO;
import it.uniroma2.marchidori.maininterface.entity.Lobby;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LobbyDAOMem implements LobbyDAO {

    private static LobbyDAOMem instance=null;

    private LobbyDAOMem() {}

    public static synchronized LobbyDAO getInstance(){
        if(instance==null){
            instance=new LobbyDAOMem();
        }
        return instance;
    }

    private List<Lobby> lobbies = new ArrayList<>();

    @Override
    public List<Lobby> getLobby() {
        return lobbies;
    }

    @Override
    public void updateLobby(Lobby lobby) throws IOException {
        Lobby old = getLobbyByName(lobby.getLobbyName());
        old = lobby;
    }

    @Override
    public void deleteLobby(String lobbyName) throws IOException {
        Lobby lobby = getLobbyByName(lobbyName);
        lobbies.remove(lobby);
    }

    @Override
    public void addLobby(Lobby lobby) throws IOException {
        lobbies.add(lobby);
    }

    private Lobby getLobbyByName(String name){
        for(Lobby lobby : lobbies){
            if(lobby.getLobbyName().equals(name)){
                return lobby;
            }
        }
        return null;
    }
}
