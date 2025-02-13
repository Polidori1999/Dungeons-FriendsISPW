package it.uniroma2.marchidori.maininterface.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity "Lobby": dati e logica di dominio.
 * Fatta in questo modo con finale e solo getter la lobby è immutabile
 *
 */
public class Lobby {
    private final String name;
    private final String duration;  // "Singola"/"Campagna"
    private final String liveOnline;      // "Online"/"Presenza"
    private final String infoLink;
    private int maxOfPlayers;
    private String owner;
    private List<String> joinedPlayers;



    public Lobby(String name,String duration, String type, int numberOfPlayers,String owner, String infoLink,List<String> joinedPlayers) {
        this.name = name;
        this.duration = duration;
        this.liveOnline = type;
        this.maxOfPlayers = numberOfPlayers;

        this.owner = owner;
        this.infoLink = infoLink;
        this.joinedPlayers = new ArrayList<>();
    }

    public String getLobbyName() {
        return name;
    }
    public String getDuration() {
        return duration;
    }
    public String getLiveOnline() {
        return liveOnline;
    }

    public List<String> getJoinedPlayers(){
        return joinedPlayers;
    }

    public boolean isFull(){
        return joinedPlayers.size() >= maxOfPlayers;
    }

    public int getMaxOfPlayers() {
        return maxOfPlayers;
    }
    public String getOwner() {
        return owner;
    }
    public String getInfoLink() {
        return infoLink;
    }


}
