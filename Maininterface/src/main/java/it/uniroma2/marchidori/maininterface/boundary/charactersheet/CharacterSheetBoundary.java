package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.Alert;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class CharacterSheetBoundary implements UserAwareInterface, ControllerAwareInterface {

    private static final Logger logger = Logger.getLogger(CharacterSheetBoundary.class.getName());

    @FXML
    private AnchorPane characterSheetPane;

    // ---------------------- FIELDS: sezione "info" ----------------------
    @FXML
    private TextField charName;

    @FXML
    private ComboBox<String> charRace;

    @FXML
    private ComboBox<String> charClass;

    @FXML
    private TextField charAge;
    @FXML
    private TextField charLevel;

    // ---------------------- FIELDS: sezione "statsScores" ----------------------
    @FXML
    private TextField charStrenght;     // STR
    @FXML
    private TextField charDexerity;     // DEX
    @FXML
    private TextField charConstitution; // CON
    @FXML
    private TextField charIntelligence; // INT
    @FXML
    private TextField charWisdom;       // WIS
    @FXML
    private TextField charCharisma;     // CHA

    // Flag: true se stiamo creando, false se stiamo modificando
    protected boolean creationMode;

    // Variabile per gestire l'utente corrente
    protected UserBean currentUser;

    // Variabile per mantenere lo stato precedente (es. il nome vecchio per aggiornamenti)
    protected String oldName;

    // Il bean che rappresenta il CharacterSheet (diviso in info e stats)
    protected CharacterSheetBean currentBean;

    // Controller associato, iniettato tramite setLogicController
    protected CharacterSheetController controller;

    @FXML
    public void initialize() {

        // Verifica l'iniezione dell'utente
        String selected = currentUser != null ? currentUser.getSelectedLobbyName() : null;
        if (selected == null || selected.isEmpty()) {
            creationMode = true;
            currentBean = new CharacterSheetBean();
            oldName = null;
        } else {
            creationMode = false;
            currentBean = findCharByName(selected);
            oldName = selected;
            if (currentBean == null) {
                creationMode = true;
                currentBean = new CharacterSheetBean();
            }
        }
        if (creationMode) {
            clearFields();
            logger.info(">>> Modalità creazione attiva.");
        } else {
            populateFields(currentBean);
            logger.info(">>> Modalità modifica attiva per personaggio: " + currentBean.getInfoBean().getName());
        }
    }

    /**
     * Popola i campi della UI con i dati presenti nel CharacterSheetBean.
     */
    private void populateFields(CharacterSheetBean currentBean) {
        if (currentBean.getInfoBean() != null) {
            charName.setText(currentBean.getInfoBean().getName());
            charAge.setText(String.valueOf(currentBean.getInfoBean().getAge()));
            charLevel.setText(String.valueOf(currentBean.getInfoBean().getLevel()));
            charRace.setValue(currentBean.getInfoBean().getRace());
            charClass.setValue(currentBean.getInfoBean().getClasse());
        }
        if (currentBean.getStatsBean() != null) {
            charStrenght.setText(String.valueOf(currentBean.getStatsBean().getStrength()));
            charDexerity.setText(String.valueOf(currentBean.getStatsBean().getDexterity()));
            charConstitution.setText(String.valueOf(currentBean.getStatsBean().getConstitution()));
            charIntelligence.setText(String.valueOf(currentBean.getStatsBean().getIntelligence()));
            charWisdom.setText(String.valueOf(currentBean.getStatsBean().getWisdom()));
            charCharisma.setText(String.valueOf(currentBean.getStatsBean().getCharisma()));
        }
    }

    /**
     * Pulisce tutti i campi della UI e imposta dei valori di default per i punteggi.
     */
    private void clearFields() {
        charName.clear();
        charAge.clear();
        charLevel.clear();
        charStrenght.setText("10");
        charDexerity.setText("10");
        charConstitution.setText("10");
        charIntelligence.setText("10");
        charWisdom.setText("10");
        charCharisma.setText("10");
        charRace.setValue(null);
        charClass.setValue(null);
    }

    /**
     * Cerca il CharacterSheetBean nella lista dell'utente in base al nome.
     */
    private CharacterSheetBean findCharByName(String charName) {
        if (currentUser.getCharacterSheets() == null) {
            return null;
        }
        return currentUser.getCharacterSheets().stream()
                .filter(cs -> cs.getInfoBean().getName().equals(charName))
                .findFirst()
                .orElse(null);
    }

    // -------------------------------------------------------------
    //                 SALVATAGGIO (BOTTONE SAVE)
    // -------------------------------------------------------------
    @FXML
    private void onClickSaveCharacter(ActionEvent event) throws IOException {
        if (currentBean == null) {
            logger.severe(">>> ERRORE: currentBean è NULL! Non posso aggiornare.");
            return;
        }

        // Se le parti del bean non sono inizializzate, creale.
        if (currentBean.getInfoBean() == null) {
            currentBean.setInfoBean(new CharacterInfoBean());
        }
        if (currentBean.getStatsBean() == null) {
            currentBean.setStatsBean(new CharacterStatsBean());
        }

        // Aggiorna i dati del bean con i valori presenti nei campi della UI
        currentBean.getInfoBean().setName(charName.getText());
        currentBean.getInfoBean().setAge(parseIntOrZero(charAge.getText()));
        currentBean.getInfoBean().setClasse(charClass.getValue());
        currentBean.getInfoBean().setLevel(parseIntOrZero(charLevel.getText()));
        currentBean.getInfoBean().setRace(charRace.getValue());

        currentBean.getStatsBean().setStrength(parseIntOrZero(charStrenght.getText()));
        currentBean.getStatsBean().setDexterity(parseIntOrZero(charDexerity.getText()));
        currentBean.getStatsBean().setIntelligence(parseIntOrZero(charIntelligence.getText()));
        currentBean.getStatsBean().setWisdom(parseIntOrZero(charWisdom.getText()));
        currentBean.getStatsBean().setCharisma(parseIntOrZero(charCharisma.getText()));
        currentBean.getStatsBean().setConstitution(parseIntOrZero(charConstitution.getText()));

        // Validazione del bean
        String validationErrors = controller.validate(currentBean);
        if (!validationErrors.isEmpty()) {
            Alert.showError("Errore di Validazione", validationErrors);
            return;
        }

        logger.info(">>> Personaggio aggiornato con successo: " + currentBean.getInfoBean().getName());

        // Chiamata alle funzioni di update o create in base alla modalità
        if (!creationMode) {
            controller.updateChar(oldName, currentBean);
            // Dopo un update, puoi ripopolare i campi per riflettere i dati aggiornati
            populateFields(currentBean);
        } else {

            controller.createChar(currentBean);
            // Dopo una create, pulisci i campi per permettere l'inserimento di un nuovo personaggio
            clearFields();
        }

        // Dopo il salvataggio, resetta la selezione e torna alla lista dei personaggi
        currentUser.setSelectedLobbyName(null);

        changeScene(SceneNames.CHARACTER_LIST);
    }

    /**
     * Helper: prova a convertire una stringa in int, restituisce 0 se il parsing fallisce.
     */
    private int parseIntOrZero(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            logger.severe(">>> ERRORE: Il valore inserito non è un numero valido: " + input);
            return 0;
        }
    }

    // -------------------------------------------------------------
    //                     NAVIGAZIONE (RESTO)
    // -------------------------------------------------------------
    @FXML
    void onClickGoBackToList(ActionEvent event) throws IOException {
        changeScene(SceneNames.CHARACTER_LIST);
    }

    @FXML
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();

        // Esegui il cambio scena
        Stage currentStage = (Stage) characterSheetPane.getScene().getWindow();
        try {
            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) characterSheetPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (CharacterSheetController) logicController;
    }
}