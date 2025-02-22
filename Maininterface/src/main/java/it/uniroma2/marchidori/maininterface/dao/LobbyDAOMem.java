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

    //metodo vuoto copia aggiornata insieme all'entity
    @Override
    public void updateLobby(Lobby lobby) throws IOException {
        //void
    }

    //elimina una lobby dalla lista
    @Override
    public boolean deleteLobby(String lobbyName) {
        Lobby lobby = getLobbyByName(lobbyName);
        lobbies.remove(lobby);
        return true;
    }

    //aggiunge una lobby alla lista
    @Override
    public boolean addLobby(Lobby lobby) {
        lobbies.add(lobby);
        return true;
    }

    //metodo per cercare una lobby nella lista
    private Lobby getLobbyByName(String name){
        for(Lobby lobby : lobbies){
            if(lobby.getLobbyName().equals(name)){
                return lobby;
            }
        }
        return null;
    }
}