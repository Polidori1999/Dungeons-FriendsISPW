package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.logging.Logger;



public class ManageLobbyListDMBoundary extends ManageLobbyListPlayerBoundary {
    private static final Logger logger = Logger.getLogger(ManageLobbyListDMBoundary.class.getName());

    //funzione di inizializzazione della GUI
    @Override
    protected void initialize() {
        // Richiama l’initialize() della superclasse
        super.initialize();
        if (controller == null) {
            logger.severe("Errore: controller non inizializzato!");
            return;
        }
        // Carica la lista delle lobby gestite dal DM
        data.clear();
        data.addAll(currentUser.getJoinedLobbies());
        tableViewLobby.refresh();

        TableColumnUtils.setupConditionalButtonColumn(
                tableViewLobbyEdit,
                lobbyBean -> lobbyBean.getOwner().equals(currentUser.getEmail()),
                "Edit",
                this::editLobby
        );
        // Rendo cliccabile il bottone "New Lobby"
        newLobbyButton.setVisible(true);
        newLobbyButton.setDisable(false);
    }

    //funzione per il cambio di scena per il caso di editlobby
    private void editLobby(LobbyBean lobbyBean) {
        // Imposta il nome della lobby selezionata nel currentUser
        currentUser.setSelectedLobbyName(lobbyBean.getName());
        // Cambia scena verso ManageLobbyBoundary
        try {
            changeScene(SceneNames.MANAGE_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (modifica Lobby).", e);
        }
    }

    //funzione per il cambio di scena per il caso di newLobby
    private void newLobby() {
        // Imposta la lobby da editare nel currentUser
        currentUser.setSelectedLobbyName(null);
        try {
            changeScene(SceneNames.MANAGE_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (crea Lobby).", e);
        }
    }

    //funzione per gli altri cambi di scena
    @Override
    @FXML
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();

        if (SceneNames.MANAGE_LOBBY.equals(fxml)) {
            newLobby();
        } else {
            super.onNavigationButtonClick(event);
        }
    }
}