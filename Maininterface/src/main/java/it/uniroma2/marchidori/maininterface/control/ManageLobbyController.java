package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.LobbyDAO;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;

import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;

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

    //creo una nuova lobby
    public void createLobby(LobbyBean bean) {
        if (bean == null) {
            LOGGER.log(Level.SEVERE, "ERRORE: Il Bean passato a createLobby() è NULL!");
            return;
        }
        Lobby newlobby = Converter.lobbyBeanToEntity(bean);

        if (currentUser != null && currentUser.getJoinedLobbies() != null) {
            // Aggiunge la lobby alle liste (Bean e Entity)
            currentUser.getJoinedLobbies().add(bean);
            currentEntity.getJoinedLobbies().add(newlobby);


            // Salva la lobby appena creata tramite LobbyDAO
            LobbyDAO lobbyDAO=Session.getInstance().getLobbyDAO();
            lobbyDAO.addLobby(newlobby);
            // Aggiorna i dati dell'utente
            UserDAO userDAO = Session.getInstance().getUserDAO();
            userDAO.updateUsersEntityData(currentEntity);

            currentUser.setSelectedLobbyName(null);
        } else {
            LOGGER.log(Level.SEVERE, "ERRORE: currentUser è NULL in createLobby()!");
        }
    }

    //aggiorna lobby
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

                    // Salva le modifiche tramite LobbyDAO e UserDAO
                    LobbyDAO lobbyDAO = Session.getInstance().getLobbyDAO();
                    lobbyDAO.updateLobby(updatedLobby);

                    UserDAO userDAO = Session.getInstance().getUserDAO();
                    userDAO.updateUsersEntityData(currentEntity);


                    currentUser.setSelectedLobbyName(null);
                    return;
                }
            }
            LOGGER.log(Level.SEVERE, "ERRORE: Nessuna lobby trovata con il nome: {0}", oldName);
        } else {
            LOGGER.log(Level.SEVERE, "ERRORE: currentUser o la lista delle lobby è NULL in updateLobby().");
        }
    }

    //Cerca una lobby per nome nella lista di LobbyBean passata.
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

    //validate
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

    //verifica se non è vuota
    private static void validateNotEmpty(List<String> value, List<String> fieldName, StringBuilder errors) {
        for(int i = 0; i < value.size(); i++) {
            if (value.get(i) == null || value.get(i).trim().isEmpty()) {
                errors.append(fieldName.get(i)).append(" cannot be empty.\n");
            }
        }

    }
}
