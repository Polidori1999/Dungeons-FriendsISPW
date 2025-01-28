package it.uniroma2.marchidori.maininterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CharacterSheetController {

    @FXML
    private AnchorPane characterSheetPane;

    // Campi di testo
    @FXML
    private TextField charName;

    @FXML
    private TextField charRace;

    @FXML
    private TextField charAge;

    @FXML
    private TextField charClass;

    @FXML
    private TextField charStrenght;

    @FXML
    private TextField charDexerity;

    @FXML
    private TextField charConstitution;

    @FXML
    private TextField charIntelligence;

    @FXML
    private TextField charWisdom;

    @FXML
    private TextField charCharisma;

    @FXML
    private TextField charLevel;

    // Ecc. per tutti i campi che hai...

    // Pulsanti o RadioButton o Label vari...
    @FXML
    private Button goBackToList;

    @FXML
    private Button goToHome;

    @FXML
    private Button saveButton;

    // Salviamo qui il character ricevuto
    private CharacterSheet currentCharacter;

    /**
     * Questo metodo viene chiamato dal Controller "CharacterListController"
     * quando si clicca "Edit". Ci permette di ricevere il CharacterSheet da visualizzare.
     */
    public void setCharacterSheet(CharacterSheet character) {
        // Salviamo il riferimento
        this.currentCharacter = character;

        // Popoliamo i campi
        charName.setText(character.getName());
        charRace.setText(character.getRace());
        charAge.setText(character.getAge());
        charClass.setText(character.getClasse());  // Attento al nome se è "getClass()" vs "getClass_()"
        // e così via per Forza, Destrezza, etc. se li hai nel model
        // Esempio:
        // charStrenght.setText(character.getStrength());
        // charDexerity.setText(character.getDexterity());
        // ... ecc.
    }

    @FXML
    void onClickGoBackToList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CharacterList.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) characterSheetPane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void onClickGoToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) characterSheetPane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) {
        // ...
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) throws IOException {
        // ...
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        // ...
    }

    @FXML
    void onClickUser(ActionEvent event) throws IOException {
        // ...
    }

    @FXML
    void onclickGoToMyCharList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CharacterList.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) characterSheetPane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    void onClickSaveCharacter(ActionEvent event) {
        // Esempio: salva i campi nell’oggetto
        // Attento a convertire eventuali stringhe in interi se necessario.
        currentCharacter.setName(charName.getText());
        currentCharacter.setRace(charRace.getText());
        currentCharacter.setAge(charAge.getText());
        currentCharacter.setClasse(charClass.getText()); // o setClass() se la proprietà si chiama diversamente

        // Se devi aggiornare la TableView, potresti ricaricarla
        // ma serve un riferimento al controller principale o un modello condiviso
        // A seconda di come gestisci i tuoi dati, potresti semplicemente
        // salvare l'oggetto e poi tornare alla lista con "onClickGoBackToList(...)"

        System.out.println("Dati aggiornati: " + currentCharacter);
    }
}
