package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
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
import java.util.logging.Logger;

public class CharacterListPlayerBoundary extends CharacterListBoundary {

    private static final Logger logger = Logger.getLogger(CharacterListPlayerBoundary.class.getName());

    @Override
    public void initialize(){
        super.initialize();

        newCharacterButton.setVisible(true);
        newCharacterButton.setDisable(false);
        if (controller == null) {
            logger.severe("Errore: controller non inizializzato!");
            return;
        }
        data.addAll(controller.getAllCharacters());
        // **DEBUG: Verifica che la tabella venga popolata**
        System.out.println(">>> DEBUG: Numero di personaggi nella tabella: " + data.size());
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
                        System.out.println(">>> DEBUG: Bottone Edit premuto per personaggio: " + selectedChar.getInfoBean().getName());
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

    private void openEditCharacterModal(CharacterSheetBean beanToEdit) {
        try {
            System.out.println(">>> DEBUG: Avvio modifica personaggio: " + beanToEdit.getInfoBean().getName());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/characterSheet.fxml"));

            // **Crea manualmente il Controller e assegna PRIMA di caricare**
            CharacterSheetBoundary sheetController = new CharacterSheetBoundary();
            loader.setController(sheetController); // <- ASSEGNA IL CONTROLLER PRIMA DI CARICARE L'FXML

            Parent root = loader.load(); // **Ora carica il file FXML**

            // **Verifica che il controller sia stato assegnato**
            if (sheetController == null) {
                System.err.println(">>> ERRORE: Il controller non è stato caricato correttamente!");
                return;
            }

            // **PASSA I DATI AL CONTROLLER**
            sheetController.setCharacterSheetBean(beanToEdit);
            sheetController.setCreationMode(false);
            sheetController.setController(controller);
            sheetController.setParentBoundary(this);

            Stage modalStage = new Stage();
            modalStage.setTitle("Modifica Personaggio");
            modalStage.initOwner(characterPane.getScene().getWindow());
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.setScene(new Scene(root));

            System.out.println(">>> DEBUG: Finestra di modifica aperta!");

            modalStage.showAndWait();

            // **Aggiorna la tabella dopo la modifica**
            tableViewChar.refresh();

        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (modifica personaggio).", e);
        }
    }



    public void addNewCharacterBean(CharacterSheetBean newBean) {
        if (newBean != null) {
            System.out.println(">>> Aggiungendo il nuovo personaggio alla tabella...");
            data.add(newBean);  // **AGGIUNGIAMO IL PERSONAGGIO ALLA LISTA**
            tableViewChar.refresh(); // **FORZIAMO IL REFRESH DELLA TABELLA**
        } else {
            System.err.println(">>> ERRORE: newBean è NULL in addNewCharacterBean()!");
        }
    }



    @FXML
    void onClickNewCharacter(ActionEvent event) {
        openCreateCharacterModal();
    }

    private void openCreateCharacterModal() {
        try {
            System.out.println(">>> DEBUG: Avvio caricamento finestra modale per nuovo personaggio...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/characterSheet.fxml"));

            // **IMPORTANTE: SETTA IL CONTROLLER PRIMA DI CARICARE**
            CharacterSheetBoundary sheetController = new CharacterSheetBoundary();
            loader.setController(sheetController);

            Parent root = loader.load();
            System.out.println(">>> DEBUG: FXML caricato correttamente!");

            // **Ora assegniamo il controller in modo manuale**
            sheetController.setCreationMode(true);
            sheetController.setCharacterSheetBean(new CharacterSheetBean(
                    new CharacterInfoBean("", "", 0, "", 1),
                    new CharacterStatsBean(10, 10, 10, 10, 10, 10)
            ));
            sheetController.setController(controller);
            sheetController.setParentBoundary(this);

            // **Creiamo la finestra modale**
            Stage modalStage = new Stage();
            modalStage.setTitle("Crea Nuovo Personaggio");
            modalStage.initOwner(characterPane.getScene().getWindow());
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.setScene(new Scene(root));

            System.out.println(">>> DEBUG: Mostro la finestra modale...");
            modalStage.showAndWait();

            System.out.println(">>> DEBUG: Finestra modale chiusa, aggiorno la tabella...");
            tableViewChar.refresh();

        } catch (IOException e) {
            System.err.println(">>> ERRORE: IOException durante il caricamento di characterSheet.fxml!");
            e.printStackTrace();
            throw new SceneChangeException("Errore nel cambio scena (nuovo personaggio).", e);
        }
    }

    // Aggiunge un nuovo personaggio alla tabella
    public void addCharacterToTable(CharacterSheetBean character) {
        data.add(character);
        tableViewChar.refresh();
        System.out.println(">>> DEBUG: Personaggio aggiunto alla tabella: " + character.getInfoBean().getName());
    }

    // Aggiorna un personaggio esistente nella tabella
    public void updateExistingCharacterInTable(CharacterSheetBean updatedCharacter) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getInfoBean().getName().equals(updatedCharacter.getInfoBean().getName())) { // Usa un identificatore univoco
                data.set(i, updatedCharacter);
                break;
            }
        }
        tableViewChar.refresh();
        System.out.println(">>> DEBUG: Personaggio aggiornato nella tabella: " + updatedCharacter.getInfoBean().getName());
    }

    // Ricarica completamente la tabella
    public void refreshTable() {
        tableViewChar.refresh();
    }




    @FXML
    @Override
    void onClickMyCharacter(ActionEvent event) {
        try {
            changeScene(SceneNames.CHARACTER_LIST);
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

    @Override
    public void setCurrentUser(UserBean currentUser){
        this.currentUser = currentUser;
    }
}
