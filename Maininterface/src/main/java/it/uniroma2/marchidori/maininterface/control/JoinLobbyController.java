package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JoinLobbyController implements UserAwareInterface {
    private UserBean currentUser;
    private final User currentEntity = Session.getInstance().getCurrentUser();

    private static final Logger logger = Logger.getLogger(JoinLobbyController.class.getName());

    public JoinLobbyController() {
        // empty
    }

    public List<LobbyBean> getList(List<Lobby> lobbyList) {
        List<LobbyBean> beans = new ArrayList<>();
        for (Lobby lobby : lobbyList) {
            beans.add(entityToBean(lobby));
        }
        return beans;
    }

    /**
     * Filtro su type, duration, e numero di players.
     * Se un parametro è null/empty => ignoriamo quel filtro.
     */
    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    public Lobby beanToEntity(LobbyBean bean) {
        return new Lobby(bean.getName(), bean.getDuration(), bean.getLiveOnline(), bean.isOwned(), bean.getNumberOfPlayers());
    }

    public List<LobbyBean> filterLobbies(String type, String duration, String numPlayersStr, String searchQuery) {
        List<LobbyBean> result = new ArrayList<>();

        for (Lobby lob : LobbyRepository.getAllLobbies()) {
            boolean matchesType = (type == null || type.isEmpty() || lob.getType().equals(type));
            boolean matchesDuration = (duration == null || duration.isEmpty() || lob.getDuration().equals(duration));
            boolean matchesPlayers = true;
            boolean matchesSearch = (searchQuery == null || searchQuery.isEmpty() ||
                    lob.getLobbyName().toLowerCase().contains(searchQuery));

            if (numPlayersStr != null && !numPlayersStr.isEmpty()) {
                int n = Integer.parseInt(numPlayersStr);
                matchesPlayers = (lob.getPlayers().size() == n);
            }

            if (matchesType && matchesDuration && matchesPlayers && matchesSearch) {
                result.add(entityToBean(lob));
            }
        }
        return result;
    }

    // Conversione da Entity -> Bean
    private LobbyBean entityToBean(Lobby lob) {
        LobbyBean bean = new LobbyBean();
        bean.setName(lob.getLobbyName());
        bean.setLiveOnline(lob.getType());
        bean.setDuration(lob.getDuration());
        bean.setNumberOfPlayers(lob.getNumberOfPlayers());
        return bean;
    }

    // Metodo per aggiungere un nuovo personaggio
    public void addLobbyToFavourite(LobbyBean lobby) {
        if (currentUser.getFavouriteLobbies() == null) {
            currentUser.setFavouriteLobbies(new ArrayList<>());
        }
        if (currentEntity.getFavouriteLobbies() == null) {
            currentEntity.setFavouriteLobbies(new ArrayList<>());
        }
        currentUser.getFavouriteLobbies().add(lobby);
        currentEntity.getFavouriteLobbies().add(beanToEntity(lobby));
    }

    public boolean removeLobbyByName(String name) {
        if (currentUser.getFavouriteLobbies() == null || name == null
                || currentEntity.getFavouriteLobbies() == null) {
            return false;
        }
        // removeIf restituisce true se almeno un elemento è stato rimosso
        currentUser.getFavouriteLobbies().removeIf(lobby -> lobby.getName().equals(name));
        currentEntity.getFavouriteLobbies().removeIf(lobby -> lobby.getLobbyName().equals(name));
        return true;
    }

    public void addLobby(LobbyBean lobby) {
        // Incrementa il numero corrente di giocatori
        lobby.setCurrentNumberOfPlayers(lobby.getCurrentNumberOfPlayers() + 1);

        if (currentUser.getJoinedLobbies() == null) {
            logger.log(Level.SEVERE, ">>> ERRORE: Lista lobby è NULL (currentUser.getJoinedLobbies()).");
            currentUser.setJoinedLobbies(new ArrayList<>());
        }
        if (currentEntity.getJoinedLobbies() == null) {
            logger.log(Level.SEVERE, ">>> ERRORE: Lista lobby è NULL (currentEntity.getJoinedLobbies()).");
            currentEntity.setJoinedLobbies(new ArrayList<>());
        }

        currentUser.getJoinedLobbies().add(lobby);
        currentEntity.getJoinedLobbies().add(beanToEntity(lobby));

        logger.log(Level.INFO, ">>> Lobby aggiunta! Lista aggiornata: {0}", currentUser.getJoinedLobbies());
    }

    public boolean isLobbyFavorite(String nameLobby, List<LobbyBean> favouriteLobbies) {
        if (favouriteLobbies == null || nameLobby == null) return false;
        return favouriteLobbies.stream().anyMatch(lb -> lb.getName().equals(nameLobby));
    }

    public boolean isLobbyJoined(LobbyBean lobby) {
        return currentUser != null &&
                currentUser.getJoinedLobbies() != null &&
                currentUser.getJoinedLobbies().stream()
                        .anyMatch(lb -> lb.getName().equals(lobby.getName()));
    }

}
