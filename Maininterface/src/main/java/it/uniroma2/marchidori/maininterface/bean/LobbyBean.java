package it.uniroma2.marchidori.maininterface.bean;

public class LobbyBean {
    private String duration;       // "Singola"/"Campagna"
    private String name;           // nome lobby
    private String liveOnline;     // "Online"/"Presenza"
    private String infoLink;
    private int maxOfPlayers;      // Numero massimo di giocatori
    private String owner;
    private int joinedPlayersCount; // Nuovo contatore per il numero di giocatori joinati

    public LobbyBean() {
    }

    /**
     * Costruttore che utilizza un contatore per i giocatori joinati.
     *
     * @param name                  nome della lobby
     * @param duration              durata (es. "Singola" o "Campagna")
     * @param type                  modalità ("Online" o "Presenza")
     * @param numberOfPlayers       numero massimo di giocatori
     * @param owner                 proprietario della lobby
     * @param infoLink              link di informazioni
     * @param joinedPlayersCount    numero di giocatori che hanno joinato la lobby
     */
    public LobbyBean(String name, String duration, String type, int numberOfPlayers, String owner, String infoLink, int joinedPlayersCount) {
        this.name = name;
        this.duration = duration;
        this.liveOnline = type;
        this.maxOfPlayers = numberOfPlayers;
        this.owner = owner;
        this.infoLink = infoLink;
        this.joinedPlayersCount = joinedPlayersCount;
    }

    // Getters e Setters

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
    public void setLiveOnline(String liveOnline) {
        this.liveOnline = liveOnline;
    }

    public int getMaxOfPlayers() {
        return maxOfPlayers;
    }
    public void setMaxOfPlayers(int maxOfPlayers) {
        this.maxOfPlayers = maxOfPlayers;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getInfoLink() {
        return infoLink;
    }
    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public int getJoinedPlayersCount() {
        return joinedPlayersCount;
    }
    public void setJoinedPlayersCount(int joinedPlayersCount) {
        this.joinedPlayersCount = joinedPlayersCount;
    }

    /**
     * Restituisce una stringa formattata che mostra il numero di giocatori joinati
     * rispetto al numero massimo.
     */
    public String getPlayers() {
        return joinedPlayersCount + "/" + maxOfPlayers;
    }
}
