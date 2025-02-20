package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.LobbyDAO;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;

import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ManageLobbyListController implements UserAwareInterface {

    private static final Logger LOGGER = Logger.getLogger(ManageLobbyListController.class.getName());

    private UserBean currentUser;
    private User currentEntity = Session.getInstance().getCurrentUser();

    public ManageLobbyListController() {
        // empty
    }

    public void leaveLobby(LobbyBean lobbyBean) throws IOException {

        // 1. Recupera la lobby dal repository (versione persistente) cercandola per nome.
        LobbyDAO lobbyDao=Session.getInstance().getLobbyDAO();
        List<Lobby> repoLobbies = lobbyDao.getLobby();
        Lobby repoLobby = null;
        for (Lobby l : repoLobbies) {
            if (l.getLobbyName().equals(lobbyBean.getName())) {
                repoLobby = l;
                break;
            }
        }
        if (repoLobby == null) {
            return;
        }


        // 2. Decrementa il contatore della lobby (solo sulla versione repository)
        int currentCount = repoLobby.getJoinedPlayersCount();
        if(repoLobby.getOwner().equals(currentUser.getEmail())){
            if(!lobbyDao.deleteLobby(repoLobby.getLobbyName())){
                LOGGER.severe("delete lobby failed");
                return;
            }
        }else if (currentCount > 0) {
            // 3. Aggiorna il file della lobby: elimina la riga esistente e aggiungi quella aggiornata.
                repoLobby.setJoinedPlayersCount(currentCount - 1);
                if(!lobbyDao.deleteLobby(repoLobby.getLobbyName())){
                    LOGGER.severe("delete lobby failed");
                    return;
                }
                if(!lobbyDao.addLobby(repoLobby)){
                    LOGGER.severe("add lobby failed");
                    return;
                }
        } else {
            return;
        }
        if (currentUser.getJoinedLobbies() != null) {
            currentUser.getJoinedLobbies().removeIf(lobby -> lobby.getName().equals(lobbyBean.getName()));
        }

// Rimuovi la lobby dalla lista delle lobby joinate dell'entity (User)
        if (currentEntity.getJoinedLobbies() != null) {
            currentEntity.getJoinedLobbies().removeIf(lobby -> lobby.getLobbyName().equals(lobbyBean.getName()));
        }
        // 5. Aggiorna la persistenza dell'utente.
        UserDAO dao = Session.getInstance().getUserDAO();
        dao.updateUsersEntityData(currentEntity);

    }


    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    public List<LobbyBean> getJoinedLobbies() {
        List<LobbyBean> beans = new ArrayList<>();

        if (currentEntity != null && currentEntity.getJoinedLobbies() != null) {
            for (Lobby lob : currentEntity.getJoinedLobbies()) {
                LobbyBean bean = Converter.lobbyEntityToBean(lob);
                beans.add(bean);
            }
        } else {
            LOGGER.log(Level.SEVERE, "ERRORE: currentEntity o la sua lista di joinedLobbies è null.");
        }

        return beans;
    }
}
