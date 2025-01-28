package it.uniroma2.marchidori.maininterface;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CharacterListController implements Initializable {

    @FXML
    private Button Consult_rules;

    @FXML
    private AnchorPane CharacterPane;

    @FXML
    private Button Joinlobby;

    @FXML
    private Button ManageLobby;

    @FXML
    private Button Mychar;

    @FXML
    private Button newCharacterButton;

    @FXML
    private TableView<CharacterSheet> tableViewChar;

    @FXML
    private TableColumn<CharacterSheet, String> tableViewCharAge;

    @FXML
    private TableColumn<CharacterSheet, String> tableViewCharClass;

    @FXML
    private TableColumn<CharacterSheet, String> tableViewCharName;

    @FXML
    private TableColumn<CharacterSheet, String> tableViewCharRace;

    // Colonna EDIT (definita in FXML, fx:id="tableViewCharButton")
    @FXML
    private TableColumn<CharacterSheet, Button> tableViewCharButton;

    // Colonna DELETE (definita in FXML, fx:id="tableViewCharDelete")
    @FXML
    private TableColumn<CharacterSheet, Button> tableViewCharDelete;

    @FXML
    private Button userButton;

    // Lista dei personaggi
    private final ObservableList<CharacterSheet> data = FXCollections.observableArrayList();

    @FXML
    void onClickGoToHome(ActionEvent event) throws IOException {
        goToHome();
    }

    private void goToHome() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) CharacterPane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void onClickNewCharacter(ActionEvent event) {
        // Esempio: creiamo 3 personaggi fittizi
        CharacterSheet character1 = new CharacterSheet("giov", "human","17","barbaro");
        CharacterSheet character2 = new CharacterSheet("giov", "human","17","barbaro");
        CharacterSheet character3 = new CharacterSheet("giov", "human","17","barbaro");

        data.add(character1);
        data.add(character2);
        data.add(character3);

        tableViewChar.setItems(data);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Impostiamo i ValueFactory per i campi testuali
        tableViewCharAge.setCellValueFactory(new PropertyValueFactory<>("Age"));
        tableViewCharClass.setCellValueFactory(new PropertyValueFactory<>("Class"));
        tableViewCharName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tableViewCharRace.setCellValueFactory(new PropertyValueFactory<>("Race"));

        // ===================== COLONNA EDIT =====================
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

                    // Azione del pulsante "Edit"
                    item.setOnAction(e -> {
                        // 1) Recuperiamo il personaggio corrispondente
                        CharacterSheet selectedChar = getTableView().getItems().get(getIndex());

                        // 2) Apriamo la finestra CharacterSheet.fxml e passiamo il personaggio
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("CharacterSheet.fxml"));
                            Parent root = loader.load();

                            // Recuperiamo il suo controller
                            CharacterSheetController sheetController = loader.getController();
                            // Passiamo il CharacterSheet selezionato
                            sheetController.setCharacterSheet(selectedChar);

                            // Cambiamo scena
                            Stage stage = (Stage) CharacterPane.getScene().getWindow();
                            Scene scene = new Scene(root);
                            stage.setScene(scene);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }
        });

        // ===================== COLONNA DELETE =====================
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

                    // Azione del pulsante "Delete"
                    item.setOnAction(e -> {
                        // Recupera il personaggio corrispondente
                        CharacterSheet selectedChar = getTableView().getItems().get(getIndex());
                        // Rimuove il personaggio dalla lista
                        getTableView().getItems().remove(selectedChar);
                        // Se non ci sono altri riferimenti all'oggetto,
                        // il GC potrà deallocarlo successivamente
                    });
                }
            }
        });

        // Aggiungiamo un personaggio di esempio all'avvio
        CharacterSheet prova = new CharacterSheet("probva","giov","17","barbaro");
        data.add(prova);

        // Carichiamo i dati iniziali nella TableView
        tableViewChar.setItems(data);
    }

    @FXML
    void onClickUser(ActionEvent event) throws IOException {
        goToHome();
    }
}
