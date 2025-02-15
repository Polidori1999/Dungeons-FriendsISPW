package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.LobbyDAO;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;

import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher.logger;


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
            System.out.println("Lobby " + lobbyBean.getName() + " non trovata nel repository.");
            return;
        }


        System.out.println("[DEBUG] Lobby nel repository trovata: " + repoLobby.getLobbyName() +
                " con count = " + repoLobby.getJoinedPlayersCount());

        // 2. Decrementa il contatore della lobby (solo sulla versione repository)
        int currentCount = repoLobby.getJoinedPlayersCount();
        if(repoLobby.getOwner().equals(currentUser.getEmail())){
            lobbyDao.deleteLobby(repoLobby.getLobbyName());
        }else if (currentCount > 0) {
            // 3. Aggiorna il file della lobby: elimina la riga esistente e aggiungi quella aggiornata.
            try {
                repoLobby.setJoinedPlayersCount(currentCount - 1);
                lobbyDao.deleteLobby(repoLobby.getLobbyName());
                lobbyDao.addLobby(repoLobby);
                System.out.println("[DEBUG] File repository aggiornato per la lobby " + repoLobby.getLobbyName());
            } catch (IOException e) {
                System.err.println("Errore durante l'aggiornamento della lobby: " + e.getMessage());
            }
        } else {
            System.out.println("La lobby " + repoLobby.getLobbyName() + " non ha giocatori da rimuovere.");
            return;
        }
        System.out.println("[DEBUG] Nuovo count nel repository per la lobby '"
                + repoLobby.getLobbyName() + "': " + repoLobby.getJoinedPlayersCount());

        if (currentUser.getJoinedLobbies() != null) {
            boolean removedFromBean = currentUser.getJoinedLobbies().removeIf(lobby -> lobby.getName().equals(lobbyBean.getName()));
            System.out.println("[DEBUG] Rimosso dalla lista UI: " + removedFromBean);
        } else {
            System.out.println("[DEBUG] La lista dei LobbyBean è null, nessuna rimozione effettuata.");
        }

// Rimuovi la lobby dalla lista delle lobby joinate dell'entity (User)
        if (currentEntity.getJoinedLobbies() != null) {
            boolean removedFromEntity = currentEntity.getJoinedLobbies().removeIf(lobby -> lobby.getLobbyName().equals(lobbyBean.getName()));
            System.out.println("[DEBUG] Rimosso dalla lista entity: " + removedFromEntity);
        } else {
            System.out.println("[DEBUG] La lista delle entity Lobby è null, nessuna rimozione effettuata.");
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
                System.out.println(lob);
                LobbyBean bean = Converter.lobbyEntityToBean(lob);
                beans.add(bean);
            }
        } else {
            LOGGER.log(Level.SEVERE, "ERRORE: currentEntity o la sua lista di joinedLobbies è null.");
        }

        return beans;
    }
}
