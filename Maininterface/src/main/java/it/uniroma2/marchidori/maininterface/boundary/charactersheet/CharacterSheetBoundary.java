package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.Alert;
import it.uniroma2.marchidori.maininterface.utils.CharacterSheetValidator;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Boundary per mostrare/creare/modificare i dati di un CharacterSheet
 * in forma "spezzata" (info + statsScores).
 */
public class CharacterSheetBoundary implements UserAwareInterface, ControllerAwareInterface {

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
        // Inizializza le ComboBox con le opzioni disponibili
        charRace.getItems().addAll("Elfo", "Nano", "Halfling", "Orco", "Tiefling");
        charClass.getItems().addAll(
                "Barbaro", "Guerriero", "Ladro", "Monaco", "Paladino",
                "Ranger", "Bardo", "Chierico", "Stregone", "Mago",
                "Druido", "Warlock"
        );

        // Stampa di debug per verificare l'iniezione dell'utente

        // Determina la modalità in base al campo selectedLobbyName del currentUser
        // (Nel tuo caso probabilmente usi selectedLobbyName per determinare se si tratta di un update)
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
                System.err.println(">>> Non ho trovato il personaggio con nome: " + selected);
                creationMode = true;
                currentBean = new CharacterSheetBean();
            }
        }
        if (creationMode) {
            clearFields();
            System.out.println(">>> Modalità creazione attiva.");
        } else {
            populateFields(currentBean);
            System.out.println(">>> Modalità modifica attiva per personaggio: " + currentBean.getInfoBean().getName());
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
    private void onClickSaveCharacter(ActionEvent event) {

        if (currentBean == null) {
            System.err.println(">>> ERRORE: currentBean è NULL! Non posso aggiornare.");
            return;
        }

        // Se le parti del bean non sono inizializzate, creale.
        if (currentBean.getInfoBean() == null) {
            currentBean.setInfoBean(new it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean());
        }
        if (currentBean.getStatsBean() == null) {
            currentBean.setStatsBean(new it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean());
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
        String validationErrors = CharacterSheetValidator.validate(currentBean);
        if (!validationErrors.isEmpty()) {
            System.out.println(">>> ERRORE DI VALIDAZIONE:\n" + validationErrors);
            Alert.showError("Errore di Validazione", validationErrors);
            return;
        }

        System.out.println(">>> Personaggio aggiornato con successo: " + currentBean.getInfoBean().getName());

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
        try {
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            SceneSwitcher.changeScene(currentStage, SceneNames.CHARACTER_LIST, currentUser);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a characterlist.fxml", e);
        }
    }

    /**
     * Helper: prova a convertire una stringa in int, restituisce 0 se il parsing fallisce.
     */
    private int parseIntOrZero(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            System.err.println(">>> ERRORE: Il valore inserito non è un numero valido: " + input);
            return 0;
        }
    }

    // -------------------------------------------------------------
    //                     NAVIGAZIONE (RESTO)
    // -------------------------------------------------------------
    @FXML
    void onClickGoBackToList(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Errore durante il cambio verso characterList.fxml", e);
        }
    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.CONSULT_RULES);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a consultRules.fxml", e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.HOME);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a home.fxml", e);
        }
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.JOIN_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a joinlobby.fxml", e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.MANAGE_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a managelobby.fxml", e);
        }
    }

    @FXML
    void onClickUser(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.USER);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a user.fxml", e);
        }
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) throws IOException {
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a mycharacter.fxml", e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) characterSheetPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);
    }

    @Override
    public void setCurrentUser(UserBean user) {
        System.out.println("SetCurrentUser chiamato con: " + user);
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (CharacterSheetController) logicController;
    }
}
