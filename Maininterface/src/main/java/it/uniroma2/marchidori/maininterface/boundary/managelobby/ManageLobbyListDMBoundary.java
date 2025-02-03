package it.uniroma2.marchidori.maininterface.boundary.managelobby;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;


import java.io.IOException;

public class ManageLobbyListDMBoundary extends ManageLobbyListBoundary {

    @Override
    protected void initialize() {
        // Chiama la configurazione di base definita nella superclasse
        super.initialize();

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
    }


}
