package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;
import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;

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


    public boolean deleteLobby(String lobbyName) {
        if (lobbyName == null || currentUser.getJoinedLobbies() == null) {
            logger.warning("Impossibile rimuovere la lobby: il nome o la lista delle lobby è null");
            return false;
        }

        // Rimuovi la lobby dalla lista dei LobbyBean (UI)
        boolean removedFromBean = currentUser.getJoinedLobbies().removeIf(lobby -> lobby.getName().equals(lobbyName));

        // Rimuovi la lobby dalla lista dell'entity User (persistenza)
        boolean removedFromEntity = currentEntity.getJoinedLobbies().removeIf(lobby -> lobby.getLobbyName().equals(lobbyName));

        // Aggiorna la persistenza riscrivendo completamente il file
        UserDAO dao = UserDAOFactory.getInstance().getUserDAO(Session.getInstance().getDB());
        dao.updateUsersEntityData(currentEntity);
        // Notifica eventuali listener



        return removedFromEntity;
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    public List<LobbyBean> getJoinedLobbies() {
        List<LobbyBean> beans = new ArrayList<>();

        // Verifichiamo che currentEntity non sia null e che abbia la lista di joinedLobbies valorizzata
        //UserDAO dao = UserDAOFactory.getUserDAO(false);

        if (currentEntity != null && currentEntity.getJoinedLobbies() != null) {

            for (Lobby lob : currentEntity.getJoinedLobbies()) {
                // Converte la entity Lobby in un LobbyBean
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
