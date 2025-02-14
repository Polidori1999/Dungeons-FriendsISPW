package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.LobbyChangeListener;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.dao.LobbyDaoFileSys;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;
import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;
import javafx.collections.FXCollections;

import java.io.FileNotFoundException;
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




    public List<LobbyBean> getList(List<Lobby> lobbyList) {
        List<LobbyBean> beans = new ArrayList<>();
        for (Lobby lobby : lobbyList) {
            beans.add(Converter.lobbyEntityToBean(lobby));
        }
        return beans;
    }


    // Aggiungi una lobby all'utente e salva nel file
    public void addLobby(LobbyBean lobbyBean) throws IOException {
        lobbyBean.getJoinedPlayers().add(currentUser.getEmail());
        printStringList(lobbyBean.getJoinedPlayers());
        if (currentUser.getJoinedLobbies() == null) {
            currentUser.setJoinedLobbies(FXCollections.observableArrayList());
        }
        currentUser.getJoinedLobbies().add(lobbyBean);
        currentEntity.getJoinedLobbies().add(Converter.lobbyBeanToEntity(lobbyBean));

        // Aggiorna la lista tramite DAO...
        LobbyDaoFileSys lobbyDaoFileSys = new LobbyDaoFileSys();
        UserDAO dao = UserDAOFactory.getInstance().getUserDAO(Session.getInstance().getDB());

        lobbyDaoFileSys.deleteLobby(lobbyBean.getName());
        lobbyDaoFileSys.addLobby(Converter.lobbyBeanToEntity(lobbyBean));

        dao.updateUsersEntityData(currentEntity);

        logger.log(Level.INFO, "Lobby aggiunta! Lista aggiornata: {0}", currentUser.getJoinedLobbies());

    }

    public static void printStringList(List<String> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("La lista è vuota.");
        } else {
            for (String s : list) {
                System.out.println(s);
            }
        }
    }




    // Metodo per aggiungere un nuovo personaggio
    public void addLobbyToFavourite(LobbyBean lobbyBean) {
        if (currentUser.getFavouriteLobbies() == null) {
            currentUser.setFavouriteLobbies(new ArrayList<>());
        }
        currentUser.getFavouriteLobbies().add(lobbyBean);
        currentEntity.getFavouriteLobbies().add(Converter.lobbyBeanToEntity(lobbyBean));
        UserDAO dao = UserDAOFactory.getInstance().getUserDAO(Session.getInstance().getDB());
        dao.updateUsersEntityData(currentEntity);
    }

    // Metodo per filtrare le lobby in base ai parametri
    public List<LobbyBean> filterLobbies(String type, String duration, String numPlayersStr, String searchQuery) throws IOException {
        List<LobbyBean> result = new ArrayList<>();
        LobbyDaoFileSys lobbyDaoFileSys = new LobbyDaoFileSys();
        for (Lobby lob : lobbyDaoFileSys.getLobbiesFromSys()) {
            boolean matchesType = (type == null || type.isEmpty() || lob.getLiveOnline().equals(type));
            boolean matchesDuration = (duration == null || duration.isEmpty() || lob.getDuration().equals(duration));
            boolean matchesPlayers = true;
            boolean matchesSearch = (searchQuery == null || searchQuery.isEmpty() ||
                    lob.getLobbyName().toLowerCase().contains(searchQuery));

            if (numPlayersStr != null && !numPlayersStr.isEmpty()) {
                int n = Integer.parseInt(numPlayersStr);
                matchesPlayers = (lob.getJoinedPlayers().size() == n);
            }

            if (matchesType && matchesDuration && matchesPlayers && matchesSearch) {
                result.add(Converter.lobbyEntityToBean(lob));
            }
        }
        return result;
    }

    // Check if a lobby is in the user's favorite list
    public boolean isLobbyFavorite(String lobbyName, List<LobbyBean> favouriteLobbies) {
        if (favouriteLobbies == null) {
            return false;
        }
        return favouriteLobbies.stream()
                .anyMatch(lobby -> lobby.getName().equals(lobbyName));
    }

    // Remove a lobby from the user's favorite list by its name
    public boolean removeLobbyByName(String name) {
        if (currentUser.getFavouriteLobbies() == null || name == null) {
            return false;
        }
        // Remove the lobby if it's found in the favorites
        boolean removed = currentUser.getFavouriteLobbies().removeIf(lobby -> lobby.getName().equals(name));
        // Also remove the lobby from the current entity's favorite lobbies
        currentEntity.getFavouriteLobbies().removeIf(lobby -> lobby.getLobbyName().equals(name));
        return removed;
    }

    // Check if the user has joined a particular lobby
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

    public List<Lobby> getLobbies() throws IOException {
        LobbyDaoFileSys lobbyDaoFileSys = new LobbyDaoFileSys();
        return lobbyDaoFileSys.getLobbiesFromSys();
    }
}
