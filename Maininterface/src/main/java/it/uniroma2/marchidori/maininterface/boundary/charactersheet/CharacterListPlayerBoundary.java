package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheet.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CharacterListPlayerBoundary extends CharacterListBoundary {

    private CharacterSheetController controller;

    @Override
    public void initialize(){
        super.initialize();
        newCharacterButton.setVisible(true);
        newCharacterButton.setDisable(false);
        if (controller == null) {
            System.err.println("Errore: controller non inizializzato!");
            return;
        }
        data.addAll(controller.getAllCharacters());
        // Colonna EDIT
        tableViewCharButton.setCellValueFactory(cellData -> {
            Button editBtn = new Button("Edit");
            return new ReadOnlyObjectWrapper<>(editBtn);
        });
        tableViewCharButton.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    item.setOnAction(e -> {
                        CharacterSheetBean selectedChar = getTableView().getItems().get(getIndex());
                        openEditCharacterModal(selectedChar);
                    });
                }
            }
        });
        // Colonna DELETE
        tableViewCharDelete.setCellValueFactory(cellData -> {
            Button deleteBtn = new Button("Delete");
            return new ReadOnlyObjectWrapper<>(deleteBtn);
        });
        tableViewCharDelete.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    item.setOnAction(e -> {
                        // Rimuoviamo il Bean dalla tabella
                        CharacterSheetBean selectedBean = getTableView().getItems().get(getIndex());
                        getTableView().getItems().remove(selectedBean);
                        // Se necessario, chiamare anche un metodo su controller
                        // (ad esempio "controller.deleteCharacter(selectedBean);")
                    });
                }
            }
        });
    }

    // Apertura finestra secondaria per edit
    private void openEditCharacterModal(CharacterSheetBean beanToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/characterSheet.fxml"));
            Parent root = loader.load();

            CharacterSheetBoundary sheetController = loader.getController();
            // No creationMode
            sheetController.setCreationMode(false);
            // Passiamo il Bean esistente
            sheetController.setCharacterSheetBean(beanToEdit);
            // Passiamo il controller BCE
            sheetController.setController(controller);
            // Passiamo la boundary
            sheetController.setParentBoundary(this);

            Stage modalStage = new Stage();
            modalStage.setTitle("Modifica Personaggio");
            modalStage.initOwner(characterPane.getScene().getWindow());
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();

            // Refresh
            tableViewChar.refresh();

        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (modifica personaggio).", e);
        }
    }

    public void addNewCharacterBean(CharacterSheetBean newBean) {
        data.add(newBean);
        tableViewChar.refresh();
    }

    @FXML
    void onClickNewCharacter(ActionEvent event) {
        openCreateCharacterModal();
    }

    // Apertura finestra secondaria per creare
    private void openCreateCharacterModal() {
        try {
            changeScene("characterSheet.fxml");
            //sheetController.setCreationMode(true);


            // Refresh della tabella dopo la chiusura della finestra
            tableViewChar.refresh();

        } catch (IOException e) {
            // Usare la "dedicated exception" SceneChangeException
            throw new SceneChangeException("Errore nel cambio scena (nuovo personaggio).", e);
        }
    }
    @FXML
    void onClickMyCharacter(ActionEvent event) {
        try {
            changeScene("characterList.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a characterList.fxml", e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        // Usa SceneSwitcher per cambiare scena
        Stage currentStage = (Stage) characterPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);  // Cambia scena con SceneSwitcher
    }


    public void setCurrentUser(UserBean currentUser){
        this.currentUser = currentUser;
    }
}
