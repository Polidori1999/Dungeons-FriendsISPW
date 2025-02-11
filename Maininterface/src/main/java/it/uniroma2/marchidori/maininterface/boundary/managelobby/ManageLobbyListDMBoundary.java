package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageLobbyListDMBoundary extends ManageLobbyListBoundary {
    private static final Logger logger = Logger.getLogger(ManageLobbyListDMBoundary.class.getName());


    @Override
    protected void initialize() {
        // Richiama l’initialize() della superclasse per caricare tabella, ecc.
        super.initialize();

        if (controller == null) {
            logger.severe("Errore: controller non inizializzato!");
            return;
        }
        // Carica la lista delle lobby gestite dal DM
        data.clear();
        data.addAll(currentUser.getJoinedLobbies());
        tableViewLobby.refresh();

        logger.log(Level.INFO, "Numero di lobby nella tabella: {0}", data.size());

        // Configura la colonna "Edit"
        TableColumnUtils.setupConditionalButtonColumn(
                tableViewLobbyEdit,
                LobbyBean::isOwned,  // il bottone sarà visibile solo se isOwned restituisce true
                "Edit",
                this::editLobby          // azione da eseguire al click
        );


        // Rendo cliccabile il bottone "New Lobby"
        // (nel FXML, newLobbyButton chiama onNavigationButtonClick con userData="manageLobby.fxml")
        newLobbyButton.setVisible(true);
        newLobbyButton.setDisable(false);
    }


    private void editLobby(LobbyBean beanToEdit) {
        // Imposta la lobby da editare nel currentUser
        currentUser.setSelectedLobbyName(beanToEdit.getName());
        // Cambia scena (usa il metodo protetto ereditato dalla superclasse)
        try {
            changeScene(SceneNames.MANAGE_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (modifica Lobby).", e);
        }
    }

    // Se vuoi, puoi sovrascrivere setCurrentUser, ma non è più strettamente necessario
    @Override
    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }
}
