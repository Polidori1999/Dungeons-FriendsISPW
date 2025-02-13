package it.uniroma2.marchidori.maininterface.bean;

import it.uniroma2.marchidori.maininterface.entity.Lobby;

import java.util.ArrayList;
import java.util.List;

public class LobbyBean {
    private String duration;       // "Singola"/"Campagna"
    private String name;           // nome lobby
    private String liveOnline;// "Online"/"Presenza"
    private String infoLink;
    private int maxOfPlayers;// # di giocatori
    private String owner;
    private List<String> joinedPlayers;


    public LobbyBean(){
    }



    public LobbyBean(String name,String duration, String type, int numberOfPlayers,String owner, String infoLink,List<String> joinedPlayers) {
        this.name = name;
        this.duration = duration;
        this.liveOnline = type;
        this.maxOfPlayers = numberOfPlayers;
        this.owner = owner;
        this.infoLink = infoLink;
        this.joinedPlayers = new ArrayList<>();
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLiveOnline() {
        return liveOnline;
    }
    public void setLiveOnline(String type) {
        this.liveOnline = type;
    }

    public int getMaxOfPlayers() {
        return maxOfPlayers;
    }
    public void setMAXOfPlayers(int MAXOfPlayers) {
        this.maxOfPlayers = MAXOfPlayers;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwned(String owner) {
        this.owner = owner;
    }

    public String getPlayers() {
        return joinedPlayers.size() + "/" + maxOfPlayers;
    }
    public String getInfoLink() {
        return infoLink;
    }
    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }
    public List<String> getJoinedPlayers() {
        return joinedPlayers;
    }
    public void setJoinedPlayers(List<String> joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
    }
}
