package it.uniroma2.marchidori.maininterface;

import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
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

    // Pulsanti / bottoni vari
    @FXML
    private Button goBackToList;

    @FXML
    private Button goToHome;

    @FXML
    private Button saveButton;

    // --- Variabili di supporto per gestire Creazione o Modifica ---
    /** Se creazione = true, stiamo creando un nuovo personaggio. Se false, stiamo modificando. */
    private boolean creationMode = false;

    /** Riferimento al personaggio in caso di modifica */
    private CharacterSheet currentCharacter;

    /**
     * Riferimento al controller padre (CharacterListController),
     * per poter aggiungere un nuovo personaggio all'ObservableList, se siamo in creationMode.
     */
    private CharacterListBoundary parentController;

    // -------------------------------------------------------------
    //              METODI DI CONFIGURAZIONE DEL CONTROLLER
    // -------------------------------------------------------------

    /**
     * Viene chiamato dal CharacterListController se siamo in modalità creazione.
     */
    public void setCreationMode(boolean creationMode) {
        this.creationMode = creationMode;
        if (creationMode) {
            // Se vogliamo partire con i campi vuoti
            charName.setText("");
            charRace.setText("");
            charAge.setText("");
            charClass.setText("");
            charStrenght.setText("");
            charDexerity.setText("");
            charConstitution.setText("");
            charIntelligence.setText("");
            charWisdom.setText("");
            charCharisma.setText("");
            charLevel.setText("");
        }
    }

    /**
     * Viene chiamato dal CharacterListController per passare
     * un riferimento a se stesso (così possiamo aggiungere un personaggio).
     */
    public void setParentController(CharacterListBoundary parentController) {
        this.parentController = parentController;
    }

    /**
     * Viene chiamato dal CharacterListController quando si clicca "Edit".
     * Imposta la modalità "modifica" e popola i campi con i dati del personaggio.
     */
    public void setCharacterSheet(CharacterSheet character) {
        this.currentCharacter = character;
        this.creationMode = false; // Stiamo modificando un personaggio esistente

        // Popoliamo i campi
        charName.setText(character.getName());
        charRace.setText(character.getRace());
        charAge.setText(character.getAge());
        charClass.setText(character.getClasse());
        charStrenght.setText(character.getStrength());
        charDexerity.setText(character.getDexterity());
        charConstitution.setText(character.getConstitution());
        charIntelligence.setText(character.getIntelligence());
        charWisdom.setText(character.getWisdom());
        charCharisma.setText(character.getCharisma());
        charLevel.setText(character.getLevel());
    }

    // -------------------------------------------------------------
    //                 METODI DI NAVIGAZIONE INALTERATI
    //     (manteniamo i tuoi metodi come da codice originale)
    // -------------------------------------------------------------

    @FXML
    void onClickGoBackToList(ActionEvent event) throws IOException {
        // Invece di ricaricare CharacterList.fxml, ci limitiamo a chiudere questa finestra
        Stage stage = (Stage) characterSheetPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickGoToHome(ActionEvent event) throws IOException {
        // Rimane invariato (se vuoi davvero cambiare finestra)
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
        // Esempio: potresti voler chiudere la finestra corrente, o ricaricare la lista
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CharacterList.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) characterSheetPane.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    // -------------------------------------------------------------
    //           SALVATAGGIO DEL PERSONAGGIO (BOTTONE SAVE)
    // -------------------------------------------------------------

    @FXML
    void onClickSaveCharacter(ActionEvent event) throws IOException {
        if (creationMode) {
            // Creiamo un nuovo CharacterSheet
            CharacterSheet newCharacter = new CharacterSheet(
                    charName.getText(),
                    charRace.getText(),
                    charAge.getText(),
                    charClass.getText(),
                    charLevel.getText(),
                    charStrenght.getText(),
                    charDexerity.getText(),
                    charIntelligence.getText(),
                    charWisdom.getText(),
                    charCharisma.getText(),
                    charConstitution.getText()
            );

            // Aggiungiamolo alla lista nel controller padre
            if (parentController != null) {
                parentController.addNewCharacter(newCharacter);
            }
            System.out.println("Creato nuovo personaggio: " + newCharacter);

        } else {
            // Stiamo modificando un personaggio esistente
            if (currentCharacter != null) {
                currentCharacter.setName(charName.getText());
                currentCharacter.setRace(charRace.getText());
                currentCharacter.setAge(charAge.getText());
                currentCharacter.setClasse(charClass.getText());
                currentCharacter.setLevel(charLevel.getText());
                currentCharacter.setStrength(charStrenght.getText());
                currentCharacter.setDexterity(charDexerity.getText());
                currentCharacter.setIntelligence(charIntelligence.getText());
                currentCharacter.setWisdom(charWisdom.getText());
                currentCharacter.setCharisma(charCharisma.getText());
                currentCharacter.setConstitution(charConstitution.getText());

                System.out.println("Aggiornato personaggio esistente: " + currentCharacter);
            }
        }

        // Chiudiamo la finestra (invece di ricaricare CharacterList.fxml)
        Stage stage = (Stage) characterSheetPane.getScene().getWindow();
        stage.close();
    }
}