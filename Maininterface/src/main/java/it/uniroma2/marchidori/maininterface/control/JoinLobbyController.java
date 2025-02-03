package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.factory.LobbyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class JoinLobbyController {

    private List<Lobby> allLobbies;

    private static final Logger logger = Logger.getLogger(JoinLobbyController.class.getName());

    public JoinLobbyController() {
        this.allLobbies = LobbyFactory.getLobbies();//usa la factory per i dati
    }


    /**
     * Filtro su type, duration, e numero di players.
     * Se un parametro è null/empty => ignoriamo quel filtro.
     */
    public List<LobbyBean> filterLobbies(String type, String duration, String numPlayersStr) {
        List<LobbyBean> result = new ArrayList<>();

        for (Lobby lob : allLobbies) {
            boolean matchesType = (type == null || type.isEmpty() || lob.getType().equals(type));
            boolean matchesDuration = (duration == null || duration.isEmpty() || lob.getDuration().equals(duration));
            boolean matchesPlayers = true;

            // Correzione: != null
            if (numPlayersStr != null && !numPlayersStr.isEmpty()) {
                int n = Integer.parseInt(numPlayersStr);
                matchesPlayers = (lob.getPlayers().size() == n);
            }

            if (matchesType && matchesDuration && matchesPlayers) {
                result.add(entityToBean(lob));
            }
        }
        return result;
    }

    // Esempio di logica "join"
    public boolean joinLobby(LobbyBean bean, String playerName) {
        Lobby found = findLobbyByName(bean.getName());
        if (found == null) return false;
        if (found.isFull()) return false;

        found.addPlayer(playerName);
        return true;
    }

    public void saveChanges(LobbyBean bean) {
        logger.info("Salvataggio della lobby: " + bean.getName());
    }


    // Conversione da Entity -> Bean
    private LobbyBean entityToBean(Lobby lob) {
        LobbyBean bean = new LobbyBean();
        bean.setName(lob.getLobbyName());
        bean.setType(lob.getType());
        bean.setDuration(lob.getDuration());
        bean.setNumberOfPlayers(lob.getPlayers().size());
        return bean;
    }

    private Lobby findLobbyByName(String lobbyName) {
        for (Lobby lob : allLobbies) {
            if (lob.getLobbyName().equals(lobbyName)) {
                return lob;
            }
        }
        return null;
    }
    //come diceva dissan
    /*public static class Builder{
        private Builder(){
          // cannot be instantiated
        }
        public static JoinLobbyController build() {
            return new JoinLobbyController();
        }
        public static JoinLobbyController build(JoinLobbyConfiguration conf) {
            return new JoinLobbyController();
        }
    }*/

    public static class JoinLobbyConfiguration {

    }
}