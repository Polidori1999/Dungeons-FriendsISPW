package it.uniroma2.marchidori.maininterface.bean;

public class LobbyBean {
    private String duration;       // "Singola"/"Campagna"
    private String name;           // nome lobby
    private String type;           // "Online"/"Presenza"
    private int numberOfPlayers;// # di giocatori
    private boolean owned;


    public LobbyBean(){
    }

    public LobbyBean(String duration, String name, String type, int numberOfPlayers, boolean owned) {
        this.duration = duration;
        this.name = name;
        this.type = type;
        this.numberOfPlayers = numberOfPlayers;
        this.owned = owned;
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

    public boolean isOwned() {
        return owned;
    }
    public void setOwned(boolean owned) {
        this.owned = owned;
    }


    @Override
    public String toString() {
        return "[LobbyBean] " + name + " (" + type + ") - Duration: " + duration + " - players: " + numberOfPlayers;
    }

}
