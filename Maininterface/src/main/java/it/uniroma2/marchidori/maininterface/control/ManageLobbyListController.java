package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.dao.LobbyDaoFileSys;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;
import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;

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

    public void leaveLobby(LobbyBean lobbyBean) {

        // 1. Recupera la lobby dal repository (versione persistente) cercandola per nome.
        LobbyDaoFileSys lobbyDao = new LobbyDaoFileSys();
        List<Lobby> repoLobbies = LobbyDaoFileSys.getLobbiesFromSys();
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
        if (currentCount > 0) {
            repoLobby.setJoinedPlayersCount(currentCount - 1);
        } else {
            System.out.println("La lobby " + repoLobby.getLobbyName() + " non ha giocatori da rimuovere.");
            return;
        }
        System.out.println("[DEBUG] Nuovo count nel repository per la lobby '"
                + repoLobby.getLobbyName() + "': " + repoLobby.getJoinedPlayersCount());

        // 3. Aggiorna il file della lobby: elimina la riga esistente e aggiungi quella aggiornata.
        try {
            lobbyDao.deleteLobby(repoLobby.getLobbyName());
            lobbyDao.addLobby(repoLobby);
            System.out.println("[DEBUG] File repository aggiornato per la lobby " + repoLobby.getLobbyName());
        } catch (IOException e) {
            System.err.println("Errore durante l'aggiornamento della lobby: " + e.getMessage());
        }

        // 4. Rimuovi la lobby dalla lista delle lobby joinate dell'utente.
        boolean removedFromBean = currentUser.getJoinedLobbies().removeIf(lobby -> lobby.getName().equals(lobbyBean.getName()));
        boolean removedFromEntity = currentEntity.getJoinedLobbies().removeIf(lobby -> lobby.getLobbyName().equals(lobbyBean.getName()));
        System.out.println("[DEBUG] Rimosso dalla lista UI: " + removedFromBean + ", dalla lista entity: " + removedFromEntity);

        // 5. Aggiorna la persistenza dell'utente.
        UserDAOFileSys dao = Session.getInstance().getUserDAOFileSys();
        dao.updateUsersEntityData(currentEntity);

    }



    /*public void deleteLobby(LobbyBean lobbyBean) throws IOException {
        if (lobbyBean == null || currentUser.getJoinedLobbies() == null) {
            logger.warning("Impossibile rimuovere la lobby: il nome o la lista delle lobby è null");
            return;
        }

        // Rimuovi la lobby dalla lista dei LobbyBean (UI)
        boolean removedFromBean = currentUser.getJoinedLobbies().removeIf(lobby -> lobby.getName().equals(lobbyBean.getName()));

        // Rimuovi la lobby dalla lista dell'entity User (persistenza)
        boolean removedFromEntity = currentEntity.getJoinedLobbies().removeIf(lobby -> lobby.getLobbyName().equals(lobbyBean.getName()));
        if(lobbyBean.getOwner().equals(currentUser.getEmail())){
            LobbyDaoFileSys lobbyDaoFileSys = new LobbyDaoFileSys();
            lobbyDaoFileSys.deleteLobby(lobbyBean.getName());
        }

        // Aggiorna la persistenza riscrivendo completamente il file
        UserDAOFileSys dao = Session.getInstance().getUserDAOFileSys();
        dao.updateUsersEntityData(currentEntity);
    }*/

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
