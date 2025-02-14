package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.dao.LobbyDaoFileSys;
import it.uniroma2.marchidori.maininterface.dao.UserDAOFileSys;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;
import it.uniroma2.marchidori.maininterface.entity.Lobby;

import java.io.IOException;
import java.util.ArrayList;
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

    public void createLobby(LobbyBean bean) throws IOException {
        if (bean == null) {
            LOGGER.log(Level.SEVERE, "ERRORE: Il Bean passato a createLobby() è NULL!");
            return;
        }
        Lobby newlobby = Converter.lobbyBeanToEntity(bean);
        if (currentUser != null && currentUser.getJoinedLobbies() != null) {
            LOGGER.log(Level.INFO, "Aggiungendo lobby a UserBean: {0}", newlobby.getLobbyName());
            currentUser.getJoinedLobbies().add(bean);
            currentEntity.getJoinedLobbies().add(newlobby);
            LobbyRepository.addLobby(newlobby);
            //add to file sys missing dao repository
            LobbyDaoFileSys lobbyDaoFileSys = new LobbyDaoFileSys();
            lobbyDaoFileSys.addLobby(newlobby);
            UserDAOFileSys userDAOFileSys = new UserDAOFileSys();
            userDAOFileSys.saveUsersEntityData(currentEntity);
            currentUser.setSelectedLobbyName(null);
            LOGGER.log(Level.INFO, "Lista attuale delle lobby: {0}", currentUser.getJoinedLobbies());
        } else {
            LOGGER.log(Level.SEVERE, "ERRORE: currentUser è NULL in createLobby()!");
        }
    }

    public void updateLobby(String oldName, LobbyBean bean) throws IOException {
        if (currentUser != null && currentUser.getJoinedLobbies() != null) {
            // Cerca la lobby nella lista dello user
            for (int i = 0; i < currentUser.getJoinedLobbies().size(); i++) {
                LobbyBean lobbyBean = currentUser.getJoinedLobbies().get(i);
                // Confronta usando oldName (il nome originale)
                if (lobbyBean.getName().equals(oldName)) {
                    // Converte il bean aggiornato in un'entità Lobby
                    Lobby updatedLobby = Converter.lobbyBeanToEntity(bean);
                    // Aggiorna la lobby nella lista dello user
                    currentUser.getJoinedLobbies().set(i, bean);
                    currentEntity.getJoinedLobbies().set(i, updatedLobby);
                    // Aggiorna anche la repository:
                    // Rimuove la vecchia lobby e aggiunge quella aggiornata.
                    LobbyRepository.removeLobby(oldName);
                    LobbyRepository.addLobby(updatedLobby);
                    LobbyDaoFileSys lobbyDaoFileSys = new LobbyDaoFileSys();
                    lobbyDaoFileSys.updateLobby(updatedLobby);
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
        return foundLobby;
    }

    public String validate(LobbyBean lobby) {
        StringBuilder errors = new StringBuilder();

        // Validazione dei campi di testo (non vuoti)
        List<String> validationErrors = new ArrayList<>();
        validationErrors.add(lobby.getName());
        validationErrors.add(lobby.getDuration());
        validationErrors.add(lobby.getLiveOnline());
        validationErrors.add(lobby.getInfoLink());
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("name");
        fieldNames.add("duration");
        fieldNames.add("liveOnline");
        fieldNames.add("infoLink");
        validateNotEmpty(validationErrors,fieldNames,errors);
        if (lobby.getMaxOfPlayers() == 0) {
            errors.append("Max number of players cannot be 0.\n");
        }
        return errors.toString();
    }

    private static void validateNotEmpty(List<String> value, List<String> fieldName, StringBuilder errors) {
        for(int i = 0; i < value.size(); i++) {
            if (value.get(i) == null || value.get(i).trim().isEmpty()) {
                errors.append(fieldName.get(i)).append(" cannot be empty.\n");
            }
        }

    }
}
