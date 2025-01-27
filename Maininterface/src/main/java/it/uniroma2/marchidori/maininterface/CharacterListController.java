package it.uniroma2.marchidori.maininterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Button userButton;

    private final ObservableList<CharacterSheet> data = FXCollections.observableArrayList();

    @FXML
    void onClickGoToHome(ActionEvent event) throws IOException {
        goToHome();
    }
    private void goToHome() throws IOException {
        // Carica il file FXML della seconda scena
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent root = loader.load();

        // Ottieni lo stage attuale
        Stage stage = (Stage) CharacterPane.getScene().getWindow(); // Alternativa: (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Crea una nuova scena e impostala nello stage
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

    public void initialize(URL url, ResourceBundle rb) {
        tableViewCharAge.setCellValueFactory(new PropertyValueFactory<>("Age"));
        tableViewCharClass.setCellValueFactory(new PropertyValueFactory<>("Class"));
        tableViewCharName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tableViewCharRace.setCellValueFactory(new PropertyValueFactory<>("Race"));

        CharacterSheet prova = new CharacterSheet("probva","giov","17","barbaro");
        data.add(prova);
        tableViewChar.setItems(data);
    }

    @FXML
    void onClickUser(ActionEvent event) throws IOException {
        goToHome();
    }

}
