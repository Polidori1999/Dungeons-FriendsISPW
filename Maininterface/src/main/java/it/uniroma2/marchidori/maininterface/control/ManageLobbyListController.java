package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageLobbyListController implements UserAwareInterface {

    private static final Logger LOGGER = Logger.getLogger(ManageLobbyListController.class.getName());

    private UserBean currentUser;
    private User currentEntity = Session.getCurrentUser();

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

    public void deleteLobby(String lobbyName) {
        // Se l'utente è un guest, non fare nulla con la lista delle lobby
        if (currentUser == null || currentUser.getRoleBehavior() == RoleEnum.GUEST) {
            LOGGER.log(Level.WARNING, "Errore: L'utente è un guest e non può gestire le lobby.");
            return;  // Non fare nulla se l'utente è un guest
        }

        // Se l'utente non è un guest, prosegui con la logica normale
        if (currentUser.getJoinedLobbies() != null) {
            for (int i = 0; i < currentUser.getJoinedLobbies().size(); i++) {
                if (currentUser.getJoinedLobbies().get(i).getName().equals(lobbyName)) {
                    currentUser.getJoinedLobbies().remove(i);
                    currentEntity.getJoinedLobbies().remove(i);
                    LOGGER.log(Level.INFO, "Lobby eliminata dallo UserBean: {0}", lobbyName);
                    return;
                }
            }
            LOGGER.log(Level.SEVERE, "ERRORE: Nessuna lobby trovata con il nome: {0}", lobbyName);
        } else {
            LOGGER.log(Level.SEVERE, "ERRORE: La lista delle lobby è null per l utente {0}", currentUser.getRoleBehavior());
        }
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    public List<LobbyBean> getJoinedLobbies() {
        List<LobbyBean> beans = new ArrayList<>();

        // Verifichiamo che currentEntity non sia null e che abbia la lista di joinedLobbies valorizzata
        if (currentEntity != null && currentEntity.getJoinedLobbies() != null) {
            for (Lobby lob : currentEntity.getJoinedLobbies()) {
                // Converte la entity Lobby in un LobbyBean
                LobbyBean bean = entityToBean(lob);
                beans.add(bean);
            }
        } else {
            LOGGER.log(Level.SEVERE, "ERRORE: currentEntity o la sua lista di joinedLobbies è null.");
        }

        return beans;
    }
}
