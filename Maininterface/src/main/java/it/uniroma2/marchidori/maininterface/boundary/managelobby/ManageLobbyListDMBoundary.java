package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageLobbyListDMBoundary extends ManageLobbyListBoundary {
    private static final Logger logger = Logger.getLogger(ManageLobbyListDMBoundary.class.getName());

    protected ConfirmationPopupController confirmationPopupController;
    private LobbyBean pendingDeleteBean;

    @Override
    protected void initialize() {
        super.initialize();
        if (controller == null) {
            logger.severe("Errore: controller non inizializzato!");
            return;
        }
        data.clear();
        data.addAll(currentUser.getJoinedLobbies());
        tableViewLobby.refresh();
        logger.log(Level.INFO, "Numero di personaggi nella tabella: {0}", data.size());
        confirmationPopupController = ConfirmationPopupController.loadPopup(manageLobbyListPane);

// Configura la colonna "Edit"
        TableColumnUtils.setupButtonColumn(tableViewLobbyEdit, "Edit", this::editLobby);

// Configura la colonna "Delete"
        TableColumnUtils.setupButtonColumn(tableViewLobbyDelete, "Delete", lobby -> {
            pendingDeleteBean = lobby;
            showDeleteConfirmation();
        });

        // Rendo cliccabile il bottone "New Lobby"
        newLobbyButton.setVisible(true);
        newLobbyButton.setDisable(false);
    }

    private void showDeleteConfirmation() {
        if (confirmationPopupController != null && pendingDeleteBean != null) {
            String message = "Vuoi eliminare il personaggio '" + pendingDeleteBean.getName() + "'?";
            confirmationPopupController.show(message, 10,
                    this::onConfirmDelete,
                    this::onCancelDelete);
        } else {
            logger.severe("Errore: ConfirmationPopupController non inizializzato o pendingDeleteBean è null");
        }
    }

    private void onConfirmDelete() {
        if (pendingDeleteBean != null) {
            String characterName = pendingDeleteBean.getName();
            tableViewLobby.getItems().remove(pendingDeleteBean);
            controller.deleteLobby(characterName);
            LobbyRepository.removeLobby(pendingDeleteBean.getName());
            pendingDeleteBean = null;
        }
    }

    private void onCancelDelete() {
        pendingDeleteBean = null;
    }

    private void editLobby(LobbyBean beanToEdit) {
        // Imposta in userBean il nome della lobby da editare
        currentUser.setSelectedLobbyName(beanToEdit.getName());
        // Passo alla scena "manageLobby.fxml"
        try {
            SceneSwitcher.changeScene(
                    (Stage) manageLobbyListPane.getScene().getWindow(),
                    SceneNames.MANAGE_LOBBY,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (modifica Lobby).", e);
        }
    }

    @FXML
    void onClickNewCharacter(ActionEvent event) {
        // Invece di aprire un modal, usiamo la scena "manageLobby.fxml"
        // e settiamo selectedLobbyName = null => creazione
        currentUser.setSelectedLobbyName(null);
        logger.info(currentUser.getEmail());
        try {
            SceneSwitcher.changeScene(
                    (Stage) manageLobbyListPane.getScene().getWindow(),
                    SceneNames.MANAGE_LOBBY,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (nuova lobby).", e);
        }
    }

    @Override
    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }
}
