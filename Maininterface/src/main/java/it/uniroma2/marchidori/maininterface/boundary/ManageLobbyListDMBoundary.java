package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageLobbyListDMBoundary extends ManageLobbyListBoundary {

    @Override
    protected void configureUI() {
        // Chiama la configurazione di base definita nella superclasse
        super.configureUI();

        // Configurazione della colonna "Edit"
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
                        // Qui inserisci la logica per la modifica della lobby
                        System.out.println("Edit della lobby: " + selectedLobby.getName());
                    });
                }
            }
        });

        // Imposta il bottone newLobbyButton (definito nella superclasse) come visibile e cliccabile
        newLobbyButton.setVisible(true);
        newLobbyButton.setDisable(false);
        newLobbyButton.setOnAction(e -> {
            // Definisci qui cosa deve fare il bottone "New Lobby"
            try {
                changeScene("ManageLobby");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            // Ad esempio, potresti cambiare scena oppure aprire un form per creare una nuova lobby
        });
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) manageLobbyListPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }

}
