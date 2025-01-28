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

    @FXML
    private TableColumn<CharacterSheet, Button> tableViewCharButton;

    @FXML
    private Button userButton;

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
        // Impostiamo i ValueFactory per i campi "testuali"
        tableViewCharAge.setCellValueFactory(new PropertyValueFactory<>("Age"));
        tableViewCharClass.setCellValueFactory(new PropertyValueFactory<>("Class"));
        tableViewCharName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tableViewCharRace.setCellValueFactory(new PropertyValueFactory<>("Race"));

        // Impostiamo il ValueFactory della colonna "Edit" per creare un nuovo Button per ogni riga
        tableViewCharButton.setCellValueFactory(cellData -> {
            Button editBtn = new Button("Edit");
            return new ReadOnlyObjectWrapper<>(editBtn);
        });

        // Definiamo la CellFactory per mostrare e gestire il pulsante su ogni riga
        tableViewCharButton.setCellFactory(col -> new TableCell<CharacterSheet, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    // Azione associata al pulsante
                    item.setOnAction(e -> {
                        CharacterSheet selectedChar = getTableView().getItems().get(getIndex());
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        Stage stage = (Stage) CharacterPane.getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);

                        // Esempio: potresti aprire un nuovo FXML per l'edit
                        // apriFinestraEdit(selectedChar);
                    });
                }
            }
        });

        // Aggiungiamo un esempio di personaggio all'avvio
        CharacterSheet prova = new CharacterSheet("probva","giov","17","barbaro");
        data.add(prova);

        // Infine, carichiamo i dati nella TableView
        tableViewChar.setItems(data);
    }

    @FXML
    void onClickUser(ActionEvent event) throws IOException {
        goToHome();
    }
}
