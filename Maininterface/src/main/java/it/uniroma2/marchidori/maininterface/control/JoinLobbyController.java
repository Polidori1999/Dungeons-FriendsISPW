package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.LobbyDAO;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;

import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;

import it.uniroma2.marchidori.maininterface.utils.Alert;
import javafx.collections.FXCollections;

import java.io.IOException;
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


    public String validate(LobbyBean lobby) {
        // Recupera il contatore corrente
        int currentCount = lobby.getJoinedPlayersCount();
        StringBuilder errors = new StringBuilder();

        // Se c'è spazio, incrementa il contatore; altrimenti, segnala che la lobby è piena.
        if (currentCount+1 > lobby.getMaxOfPlayers()) {
            errors.append("Lobby piena operazione di join non possibile!\n");
        }
        return errors.toString();
    }

    /**
     * Aggiunge l'utente corrente alla lobby se c'è spazio disponibile.
     * Utilizza un contatore (joinedPlayersCount) per tenere traccia del numero di giocatori.
     */
    public void addLobby(LobbyBean lobbyBean) throws IOException {
        // Recupera il contatore corrente
        int currentCount = lobbyBean.getJoinedPlayersCount();

        // Se c'è spazio, incrementa il contatore; altrimenti, segnala che la lobby è piena.
        if (currentCount < lobbyBean.getMaxOfPlayers()) {
            lobbyBean.setJoinedPlayersCount(currentCount + 1);
        } else {
            Alert.showError("Lobby piena","La lobby è piena!");
            return;
        }



        // Aggiorna le strutture dell'utente e della sessione
        if (currentUser.getJoinedLobbies() == null) {
            currentUser.setJoinedLobbies(FXCollections.observableArrayList());
        }
        currentUser.getJoinedLobbies().add(lobbyBean);
        currentEntity.getJoinedLobbies().add(Converter.lobbyBeanToEntity(lobbyBean));

        // Aggiorna la lobby nella repository

        LobbyDAO lobbyDAO=Session.getInstance().getLobbyDAO();
        UserDAO dao = Session.getInstance().getUserDAO();

        //fare update per migliorare invece di delete e add
        lobbyDAO.deleteLobby(lobbyBean.getName());
        lobbyDAO.addLobby(Converter.lobbyBeanToEntity(lobbyBean));

        // Aggiorna la persistenza dell'utente
        dao.updateUsersEntityData(currentEntity);

        logger.log(Level.INFO, "Lobby aggiornata! Numero giocatori: {0}", lobbyBean.getJoinedPlayersCount());
    }

    /**
     * Aggiunge la lobby ai preferiti dell'utente.
     */
    public void addLobbyToFavourite(LobbyBean lobbyBean) {
        if (currentUser.getFavouriteLobbies() == null) {
            currentUser.setFavouriteLobbies(new ArrayList<>());
        }
        currentUser.getFavouriteLobbies().add(lobbyBean);
        // Aggiorna anche la lista nell'entity
        currentEntity.getFavouriteLobbies().add(Converter.lobbyBeanToEntity(lobbyBean));
        UserDAO dao = Session.getInstance().getUserDAO();
        dao.updateUsersEntityData(currentEntity);
    }

    /**
     * Filtra le lobby in base ai parametri.
     */
    public List<LobbyBean> filterLobbies(String type, String duration, String searchQuery)  {
        List<LobbyBean> result = new ArrayList<>();
        LobbyDAO lobbyDAO = Session.getInstance().getLobbyDAO();
        for (Lobby lob : lobbyDAO.getLobby()) {
            boolean matchesType = (type == null || type.isEmpty() || lob.getLiveOnline().equals(type));
            boolean matchesDuration = (duration == null || duration.isEmpty() || lob.getDuration().equals(duration));
            boolean matchesSearch = (searchQuery == null || searchQuery.isEmpty() ||
                    lob.getLobbyName().toLowerCase().contains(searchQuery.toLowerCase()));

            if (matchesType && matchesDuration && matchesSearch) {
                result.add(Converter.lobbyEntityToBean(lob));
            }
        }
        return result;
    }

    /**
     * Verifica se una lobby è nei preferiti dell'utente.
     */
    public boolean isLobbyFavorite(String lobbyName, List<LobbyBean> favouriteLobbies) {
        if (favouriteLobbies == null) {
            return false;
        }
        return favouriteLobbies.stream()
                .anyMatch(lobby -> lobby.getName().equals(lobbyName));
    }

    /**
     * Rimuove una lobby dai preferiti dell'utente dato il nome.
     */
    public boolean removeLobbyByName(String name) {
        if (currentUser.getFavouriteLobbies() == null || name == null) {
            return false;
        }
        boolean removed = currentUser.getFavouriteLobbies().removeIf(lobby -> lobby.getName().equals(name));
        currentEntity.getFavouriteLobbies().removeIf(lobby -> lobby.getLobbyName().equals(name));
        return removed;
    }

    /**
     * Controlla se l'utente ha già joinato una lobby.
     */
    public boolean isLobbyJoined(LobbyBean lobby) {
        return currentUser != null &&
                currentUser.getJoinedLobbies() != null &&
                currentUser.getJoinedLobbies().stream()
                        .anyMatch(lb -> lb.getName().equals(lobby.getName()));
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    public List<Lobby> getLobbies() {
        LobbyDAO lobbyDAO = Session.getInstance().getLobbyDAO();
        return lobbyDAO.getLobby();
    }
}
