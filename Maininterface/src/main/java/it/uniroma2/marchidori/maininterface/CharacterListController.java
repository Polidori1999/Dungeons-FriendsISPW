package it.uniroma2.marchidori.maininterface;

import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CharacterListController implements Initializable {

    @FXML
    private AnchorPane CharacterPane;

    @FXML
    private TableView<CharacterSheet> tableViewChar;

    @FXML
    private TableColumn<CharacterSheet, String> tableViewCharName;

    @FXML
    private TableColumn<CharacterSheet, String> tableViewCharRace;

    @FXML
    private TableColumn<CharacterSheet, String> tableViewCharAge;

    @FXML
    private TableColumn<CharacterSheet, String> tableViewCharClass;

    @FXML
    private TableColumn<CharacterSheet, Button> tableViewCharButton;  // colonna "Edit"
    @FXML
    private TableColumn<CharacterSheet, Button> tableViewCharDelete;  // colonna "Delete"

    // La tua lista di personaggi
    private final ObservableList<CharacterSheet> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Associazioni "classiche" colonne -> campi
        tableViewCharName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableViewCharRace.setCellValueFactory(new PropertyValueFactory<>("race"));
        tableViewCharAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        tableViewCharClass.setCellValueFactory(new PropertyValueFactory<>("classe"));

        // ----- Colonna EDIT -----
        tableViewCharButton.setCellValueFactory(cellData -> {
            Button editBtn = new Button("Edit");
            return new ReadOnlyObjectWrapper<>(editBtn);
        });

        tableViewCharButton.setCellFactory(col -> new TableCell<CharacterSheet, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    // Azione "Edit"
                    item.setOnAction(e -> {
                        CharacterSheet selectedChar = getTableView().getItems().get(getIndex());
                        openEditCharacterModal(selectedChar);
                    });
                }
            }
        });

        // ----- Colonna DELETE -----
        tableViewCharDelete.setCellValueFactory(cellData -> {
            Button deleteBtn = new Button("Delete");
            return new ReadOnlyObjectWrapper<>(deleteBtn);
        });

        tableViewCharDelete.setCellFactory(col -> new TableCell<CharacterSheet, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    // Azione "Delete"
                    item.setOnAction(e -> {
                        CharacterSheet selectedChar = getTableView().getItems().get(getIndex());
                        getTableView().getItems().remove(selectedChar);
                    });
                }
            }
        });

        // Imposta la lista
        tableViewChar.setItems(data);

        // Esempio: se vuoi aggiungere un personaggio di default
        // data.add(new CharacterSheet("Eroe", "Umano", "20", "Mago", ...));
    }

    // ---------------------------------------------------------
    //                   BOTTONE: NEW CHARACTER
    // ---------------------------------------------------------
    @FXML
    void onClickNewCharacter(ActionEvent event) {
        openCreateCharacterModal();
    }

    // ---------------------------------------------------------
    //    Apri una FINESTRA secondaria (Stage modale) per CREARE
    // ---------------------------------------------------------
    private void openCreateCharacterModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CharacterSheet.fxml"));
            Parent root = loader.load();

            // Recupera il controller
            CharacterSheetController sheetController = loader.getController();
            // Indica che siamo in "creation mode"
            sheetController.setCreationMode(true);
            // Passiamo a "sheetController" un riferimento a questo controller
            sheetController.setParentController(this);

            // Crea una finestra secondaria "modale"
            Stage modalStage = new Stage();
            modalStage.setTitle("Crea Nuovo Personaggio");
            modalStage.initOwner(CharacterPane.getScene().getWindow());
            modalStage.initModality(Modality.WINDOW_MODAL);

            // Imposta la scena
            modalStage.setScene(new Scene(root));
            // Mostra la finestra e ATTENDE la chiusura
            modalStage.showAndWait();

            // Quando la finestra si chiude, se l'utente ha cliccato "Save",
            // il personaggio risulta aggiunto. Aggiorniamo (se necessario):
            tableViewChar.refresh();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    //    Apri una FINESTRA secondaria (Stage modale) per EDIT
    // ---------------------------------------------------------
    private void openEditCharacterModal(CharacterSheet characterToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CharacterSheet.fxml"));
            Parent root = loader.load();

            CharacterSheetController sheetController = loader.getController();
            // Impostiamo i campi con il personaggio esistente
            sheetController.setCharacterSheet(characterToEdit);

            // Crea uno Stage modale
            Stage modalStage = new Stage();
            modalStage.setTitle("Modifica Personaggio");
            modalStage.initOwner(CharacterPane.getScene().getWindow());
            modalStage.initModality(Modality.WINDOW_MODAL);

            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();

            // Alla chiusura, il personaggio è già stato modificato nella ObservableList
            tableViewChar.refresh();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    //    Metodo invocato dal CharacterSheetController
    //    quando l'utente salva un NUOVO personaggio
    // ---------------------------------------------------------
    public void addNewCharacter(CharacterSheet newChar) {
        data.add(newChar);
        // La table è già collegata a data, si aggiorna da sola,
        // ma se vuoi forzare un ridisegno immediato:
        tableViewChar.refresh();
    }

    public void onClickGoToHome(ActionEvent actionEvent) {
    }

    public void onClickUser(ActionEvent actionEvent) {
    }
}
