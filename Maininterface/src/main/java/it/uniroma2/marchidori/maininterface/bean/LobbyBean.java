package it.uniroma2.marchidori.maininterface.bean;

public class LobbyBean {
    private String duration;       // "Singola"/"Campagna"
    private String name;           // nome lobby
    private String liveOnline;// "Online"/"Presenza"
    private int currentNumberOfPlayers;
    private int maxOfPlayers;// # di giocatori
    private boolean owned;


    public LobbyBean(){
    }

    public LobbyBean(String duration, String name, String type, int numberOfPlayers, boolean owned) {
        this.duration = duration;
        this.name = name;
        this.liveOnline = type;
        this.maxOfPlayers = numberOfPlayers;
        this.currentNumberOfPlayers = 0;
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

    public String getLiveOnline() {
        return liveOnline;
    }
    public void setLiveOnline(String type) {
        this.liveOnline = type;
    }

    public int getNumberOfPlayers() {
        return maxOfPlayers;
    }
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.maxOfPlayers = numberOfPlayers;
    }

    public boolean isOwned() {
        return owned;
    }
    public void setOwned(boolean owned) {
        this.owned = owned;
    }
    public int getCurrentNumberOfPlayers() {
        return currentNumberOfPlayers;
    }
    public void setCurrentNumberOfPlayers(int currentNumberOfPlayers) {
        this.currentNumberOfPlayers = currentNumberOfPlayers;
    }
    public String getPlayers() {
        return currentNumberOfPlayers + "/" + maxOfPlayers;
    }
}
