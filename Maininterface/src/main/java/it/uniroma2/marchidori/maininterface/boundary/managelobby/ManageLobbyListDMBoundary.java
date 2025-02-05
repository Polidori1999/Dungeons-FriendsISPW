package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class ManageLobbyListDMBoundary extends ManageLobbyListBoundary {
    private static final Logger logger = Logger.getLogger(ManageLobbyListDMBoundary.class.getName());

    @Override
    protected void initialize() {
        super.initialize();
        if (controller == null) {
            logger.severe("Errore: controller non inizializzato!");
            return;
        }
        data.clear();
        data.addAll(controller.getAllLobbies());
        tableViewLobby.refresh();

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

        // Configura colonna "Delete" (ereditata dal genitore, suppongo)
        tableViewLobbyDelete.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    item.setOnAction(e -> {
                        // logica di delete
                        LobbyBean pendingDeleteBean = getTableView().getItems().get(getIndex());
                        deleteLobby(pendingDeleteBean);
                    });
                }
            }
        });

        // Rendo cliccabile il bottone "New Lobby"
        newLobbyButton.setVisible(true);
        newLobbyButton.setDisable(false);
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
        System.out.println(currentUser.getUsername());
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
