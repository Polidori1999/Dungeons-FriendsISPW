package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.control.ConfirmationPopupController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;

import java.io.IOException;
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

        System.out.println(">>> DEBUG: Numero di personaggi nella tabella: " + data.size());

        // Carica il popup di conferma dal file FXML e aggiungilo al contenitore principale
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/confirmationPopup.fxml"));
            Parent popupRoot = loader.load();
            // Si assume che "characterPane" (definito in CharacterListBoundary) sia il contenitore principale
            manageLobbyListPane.getChildren().add(popupRoot);
            confirmationPopupController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Configura colonna "Edit"
        tableViewLobbyEdit.setCellValueFactory(cellData -> {
            Button editBtn = new Button("Edit");
            return new ReadOnlyObjectWrapper<>(editBtn);
        });
        tableViewLobbyEdit.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    item.setOnAction(e -> {
                        LobbyBean selectedLobby = getTableView().getItems().get(getIndex());
                        editLobby(selectedLobby);
                    });
                }
            }
        });
        tableViewLobbyDelete.setCellValueFactory(cellData -> {
            Button deleteBtn = new Button("Delete");
            return new ReadOnlyObjectWrapper<>(deleteBtn);
        });
        tableViewLobbyDelete.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    item.setOnAction(e -> {
                        // Salva il bean selezionato per la cancellazione
                        pendingDeleteBean = getTableView().getItems().get(getIndex());
                        // Mostra il popup di conferma con timer
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        showDeleteConfirmation();
                    });
                }
            }
        });

        // Rendo cliccabile il bottone "New Lobby"
        newLobbyButton.setVisible(true);
        newLobbyButton.setDisable(false);
    }

    private void showDeleteConfirmation() {
        if (confirmationPopupController != null && pendingDeleteBean != null) {
            String message = "Vuoi eliminare il personaggio '" + pendingDeleteBean.getName() + "'?";
            confirmationPopupController.show(message, 10,
                    () -> onConfirmDelete(),
                    () -> onCancelDelete());
        } else {
            System.err.println("Errore: ConfirmationPopupController non inizializzato o pendingDeleteBean è null");
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

    private void deleteLobby(LobbyBean bean) {
        // Invochi la delete sul controller e rimuovi dalla tabella
        if (bean == null) return;
        controller.deleteLobby(bean.getName());
        data.remove(bean);
        tableViewLobby.refresh();
    }

    @FXML
    void onClickNewCharacter(ActionEvent event) {
        // Invece di aprire un modal, usiamo la scena "manageLobby.fxml"
        // e settiamo selectedLobbyName = null => creazione
        currentUser.setSelectedLobbyName(null);
        System.out.println(currentUser.getEmail());
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

    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }
}
