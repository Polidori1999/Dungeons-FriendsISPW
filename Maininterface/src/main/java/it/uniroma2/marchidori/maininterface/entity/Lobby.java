package it.uniroma2.marchidori.maininterface.entity;

public class Lobby {
    private final String name;
    private final String duration;      // "Singola"/"Campagna"
    private final String liveOnline;    // "Online"/"Presenza"
    private final String infoLink;      // link di informazioni
    private int maxOfPlayers;           //numero massimo dei giocatori
    private String owner;               //proprietario
    private int joinedPlayersCount;     // Contatore dei giocatori joinati


    //costruttore con parametri "base"
    public Lobby(String name, String duration, String type, int numberOfPlayers, String owner, String infoLink, int joinedPlayersCount) {
        this.name = name;
        this.duration = duration;
        this.liveOnline = type;
        this.maxOfPlayers = numberOfPlayers;
        this.owner = owner;
        this.infoLink = infoLink;
        this.joinedPlayersCount = joinedPlayersCount;
    }

    //Getter & Setter
    public String getLobbyName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getLiveOnline() {
        return liveOnline;
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

    public int getJoinedPlayersCount() {
        return joinedPlayersCount;
    }

    public boolean isFull() {
        return joinedPlayersCount >= maxOfPlayers;
    }

    public void setJoinedPlayersCount(int i) {
        this.joinedPlayersCount = i;
    }
}
