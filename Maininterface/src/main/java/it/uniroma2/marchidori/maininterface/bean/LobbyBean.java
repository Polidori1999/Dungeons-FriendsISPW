package it.uniroma2.marchidori.maininterface.bean;

public class LobbyBean {
    private String duration;       // "Singola"/"Campagna"
    private String name;           // nome lobby
    private String type;           // "Online"/"Presenza"
    private int numberOfPlayers;   // # di giocatori

    public LobbyBean(){
    }

    public LobbyBean(String duration, String name, String type, int numberOfPlayers) {
        this.duration = duration;
        this.name = name;
        this.type = type;
        this.numberOfPlayers = numberOfPlayers;
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

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public String toString() {
        return "[LobbyBean] " + name + " (" + type + ") - Duration: " + duration + " - players: " + numberOfPlayers;
    }
}
