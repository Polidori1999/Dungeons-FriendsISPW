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

    private LobbyBean entityToBean(Lobby cs) {
        // Infine crea il bean complessivo
        return new LobbyBean(cs.getDuration(),
                cs.getLobbyName(),
                cs.getType(),
                cs.getNumberOfPlayers(),
                cs.isOwned());
    }


    public boolean deleteLobby(String lobbyName) {
        if (lobbyName == null || currentUser.getJoinedLobbies() == null) {
            logger.warning("Impossibile rimuovere la lobby: il nome o la lista delle lobby è null");
            return false;
        }

        // Rimuovi la lobby dalla lista dei LobbyBean (usata dalla UI)
        boolean removedFromBean = currentUser.getJoinedLobbies().removeIf(lobby -> lobby.getName().equals(lobbyName));

        // Rimuovi la lobby dalla lista dell'entity User (per la persistenza)
        boolean removedFromEntity = currentEntity.getJoinedLobbies().removeIf(lobby -> lobby.getLobbyName().equals(lobbyName));

        if (removedFromEntity) {
            // Aggiorna la persistenza chiamando il DAO
            UserDAO dao = UserDAOFactory.getUserDAO(false);
            dao.saveUsersEntityData(currentEntity);

            // Notifica i listener (se presenti) che la lista delle lobby è cambiata
            LobbyRepository.notifyLobbyChangeListeners();
            logger.info("Lobby joinata rimossa con successo: " + lobbyName);
        } else {
            logger.warning("Nessuna lobby joinata trovata con il nome: " + lobbyName);
        }

        return removedFromEntity;
    }


    /*public void deleteLobby(String lobbyName) {
        if (currentUser == null || currentUser.getRoleBehavior() == RoleEnum.GUEST) {
            LOGGER.log(Level.WARNING, "Errore: L'utente è un guest e non può gestire le lobby.");
            return;
        }

        // Trova la lobby nella lista delle joinedLobbies dell'utente
        LobbyBean lobbyToRemove = null;
        for (LobbyBean lobby : currentUser.getJoinedLobbies()) {
            if (lobby.getName().equals(lobbyName)) {
                lobbyToRemove = lobby;
                break;
            }
        }

        if (lobbyToRemove == null) {
            LOGGER.log(Level.SEVERE, "❌ ERRORE: Nessuna lobby trovata con il nome: {0}", lobbyName);
            return;
        }


        if (lobbyToRemove.isOwned()) {
            LOGGER.log(Level.INFO, "🛑 Il proprietario sta eliminando la lobby: {0}", lobbyName);
            currentUser.getJoinedLobbies().remove(lobbyToRemove);
            currentEntity.getJoinedLobbies().removeIf(l -> l.getLobbyName().equals(lobbyName));

            // SOLO SE IL PROPRIETARIO, RIMUOVIAMO DALLA REPOSITORY
            LobbyRepository.removeLobby(lobbyName);
        } else {
            LOGGER.log(Level.INFO, "🚪 Il player sta uscendo dalla lobby: {0}", lobbyName);
            currentUser.getJoinedLobbies().remove(lobbyToRemove);
            currentEntity.getJoinedLobbies().removeIf(l -> l.getLobbyName().equals(lobbyName));

            // Rimuovi solo per questo utente (ma la lobby rimane nella repository)
            UserService userService = UserService.getInstance(false);
            userService.removeUserLobby(currentUser.getEmail(), lobbyName);
        }


        Lobby lobbyEntity = LobbyRepository.findLobbyByName(lobbyName);
        if (lobbyEntity == null) {
            LOGGER.log(Level.INFO, "🔄 Reinserimento della lobby nella lista disponibile: {0}", lobbyName);
        }
        LOGGER.log(Level.INFO, "✅ Azione completata per la lobby: {0}", lobbyName);
    }*/





    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    public List<LobbyBean> getJoinedLobbies() {
        List<LobbyBean> beans = new ArrayList<>();

        // Verifichiamo che currentEntity non sia null e che abbia la lista di joinedLobbies valorizzata
        UserDAO dao = UserDAOFactory.getUserDAO(false);

        if (currentEntity != null && currentEntity.getJoinedLobbies() != null) {

            for (Lobby lob : currentEntity.getJoinedLobbies()) {
                // Converte la entity Lobby in un LobbyBean
                System.out.println(lob);
                LobbyBean bean = entityToBean(lob);
                beans.add(bean);
            }
        } else {
            LOGGER.log(Level.SEVERE, "ERRORE: currentEntity o la sua lista di joinedLobbies è null.");
        }

        return beans;
    }


}
