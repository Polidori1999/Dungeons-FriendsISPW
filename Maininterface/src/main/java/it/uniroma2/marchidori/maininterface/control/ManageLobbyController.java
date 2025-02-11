package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;
import it.uniroma2.marchidori.maininterface.entity.Lobby;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageLobbyController implements UserAwareInterface {

    private static final Logger LOGGER = Logger.getLogger(ManageLobbyController.class.getName());

    private UserBean currentUser;
    private User currentEntity = Session.getInstance().getCurrentUser();

    public ManageLobbyController(){
        // empty
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    public void createLobby(LobbyBean bean) {
        if (bean == null) {
            LOGGER.log(Level.SEVERE, "ERRORE: Il Bean passato a createLobby() è NULL!");
            return;
        }
        Lobby newlobby = beanToEntity(bean);
        if (currentUser != null && currentUser.getJoinedLobbies() != null) {
            LOGGER.log(Level.INFO, "Aggiungendo lobby a UserBean: {0}", newlobby.getLobbyName());
            currentUser.getJoinedLobbies().add(bean);
            currentEntity.getJoinedLobbies().add(newlobby);
            LobbyRepository.addLobby(newlobby);
            currentUser.setSelectedLobbyName(null);
            LOGGER.log(Level.INFO, "Lista attuale delle lobby: {0}", currentUser.getJoinedLobbies());
        } else {
            LOGGER.log(Level.SEVERE, "ERRORE: currentUser è NULL in createLobby()!");
        }
    }

    public void updateLobby(String oldName, LobbyBean bean) {
        if (currentUser != null && currentUser.getJoinedLobbies() != null) {
            // Cerca la lobby nella lista dello user
            for (int i = 0; i < currentUser.getJoinedLobbies().size(); i++) {
                LobbyBean lobbyBean = currentUser.getJoinedLobbies().get(i);
                // Confronta usando oldName (il nome originale)
                if (lobbyBean.getName().equals(oldName)) {
                    // Converte il bean aggiornato in un'entità Lobby
                    Lobby updatedLobby = beanToEntity(bean);
                    // Aggiorna la lobby nella lista dello user
                    currentUser.getJoinedLobbies().set(i, bean);
                    currentEntity.getJoinedLobbies().set(i, updatedLobby);
                    // Aggiorna anche la repository:
                    // Rimuove la vecchia lobby e aggiunge quella aggiornata.
                    LobbyRepository.removeLobby(oldName);
                    LobbyRepository.addLobby(updatedLobby);
                    currentUser.setSelectedLobbyName(null);
                    LOGGER.log(Level.INFO, "Lobby aggiornata correttamente in UserBean e Repository.");
                    return;
                }
            }
            LOGGER.log(Level.SEVERE, "ERRORE: Nessuna lobby trovata con il nome: {0}", oldName);
        } else {
            LOGGER.log(Level.SEVERE, "ERRORE: currentUser o la lista delle lobby è NULL in updateLobby().");
        }
    }

    /**
     * Converte da Bean "spezzato" a Entity pura.
     */
    private Lobby beanToEntity(LobbyBean bean) {
        return new Lobby(bean.getName(), bean.getDuration(), bean.getLiveOnline(), bean.isOwned(), bean.getNumberOfPlayers());
    }

    public LobbyBean findLobbyByName(String lobbyName, List<LobbyBean> beans) {
        if (beans == null) {
            return null;
        }
        LobbyBean foundLobby = beans.stream()
                .filter(l -> l.getName().equals(lobbyName))
                .findFirst()
                .orElse(null);
        if (foundLobby == null) {
            LOGGER.log(Level.WARNING, "Nessuna lobby trovata con il nome: {0}", lobbyName);
            return null;
        }
        return new LobbyBean(
                foundLobby.getDuration(),
                foundLobby.getName(),
                foundLobby.getLiveOnline(),
                foundLobby.getNumberOfPlayers(),
                foundLobby.isOwned()
        );
    }

    public String validate(LobbyBean lobby) {
        StringBuilder errors = new StringBuilder();

        // Validazione dei campi di testo (non vuoti)
        validateNotEmpty(lobby.getName(), "Name", errors);
        validateNotEmpty(lobby.getDuration(), "Duration", errors);
        validateNotEmpty(lobby.getLiveOnline(), "Live/Online", errors);
        if (lobby.getNumberOfPlayers() == 0) {
            errors.append("Max number of players cannot be 0.\n");
        }
        return errors.toString();
    }

    private static void validateNotEmpty(String value, String fieldName, StringBuilder errors) {
        if (value == null || value.trim().isEmpty()) {
            errors.append(fieldName).append(" cannot be empty.\n");
        }
    }
}
