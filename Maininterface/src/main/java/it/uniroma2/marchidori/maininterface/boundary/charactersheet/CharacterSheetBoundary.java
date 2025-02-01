package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;

import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Boundary per mostrare/creare/modificare i dati di un CharacterSheet
 * in forma "spezzata" (info + statsScores).
 */
public class CharacterSheetBoundary implements UserAwareInterface {

    @FXML
    private AnchorPane characterSheetPane;

    // ---------------------- FIELDS: sezione "info" ----------------------
    @FXML
    private TextField charName;
    @FXML
    private TextField charRace;
    @FXML
    private TextField charAge;
    @FXML
    private TextField charClass;
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
    private boolean creationMode;

    protected UserBean currentUser;


    // Bean esistente (in caso di modifica)
    private CharacterSheetBean currentBean;

    // Riferimento al controller BCE
    private CharacterSheetController controller = new CharacterSheetController();

    // Riferimento alla finestra di lista (se vogliamo avvisarla di aggiornamenti)
    private CharacterListPlayerBoundary parentBoundary;



    // -------------------------------------------------------------
    //            METODI DI CONFIGURAZIONE DELLA BOUNDARY
    // -------------------------------------------------------------
    public void setController(CharacterSheetController controller) {
        this.controller = controller;
    }

    public void setParentBoundary(CharacterListPlayerBoundary parentBoundary) {
        this.parentBoundary = parentBoundary;
    }

    /**
     * Imposta la modalità "creazione". Se true, svuota i campi e
     * setta currentBean a null. Se false, vuol dire che setCharacterSheetBean()
     * verrà chiamato per popolare i campi.
     */
    public void setCreationMode(boolean creationMode) {
        this.creationMode = creationMode;
        if (creationMode) {
            clearFields();
            currentBean = null;
        }
    }

    /**
     * Popola i campi dalla Bean, in modalità modifica.
     */
    public void setCharacterSheetBean(CharacterSheetBean bean) {
        this.currentBean = bean;
        this.creationMode = false;
        populateFieldsFromBean(bean);
    }

    // -------------------------------------------------------------
    //                   METODI DI UTILITÀ
    // -------------------------------------------------------------
    private void clearFields() {
        // Sezione "info"
        charName.setText("");
        charRace.setText("");
        charAge.setText("");
        charClass.setText("");
        charLevel.setText("");

        // Sezione "stats"
        charStrenght.setText("");
        charDexerity.setText("");
        charConstitution.setText("");
        charIntelligence.setText("");
        charWisdom.setText("");
        charCharisma.setText("");
    }

    private void populateFieldsFromBean(CharacterSheetBean bean) {
        if (bean.getInfoBean() != null) {
            charName.setText(bean.getInfoBean().getName());
            charRace.setText(bean.getInfoBean().getRace());
            charAge.setText(String.valueOf(bean.getInfoBean().getAge()));
            charClass.setText(bean.getInfoBean().getClasse());
            charLevel.setText(String.valueOf(bean.getInfoBean().getLevel()));
        }
        if (bean.getStatsBean() != null) {
            charStrenght.setText(String.valueOf(bean.getStatsBean().getStrength()));
            charDexerity.setText(String.valueOf(bean.getStatsBean().getDexterity()));
            charConstitution.setText(String.valueOf(bean.getStatsBean().getConstitution()));
            charIntelligence.setText(String.valueOf(bean.getStatsBean().getIntelligence()));
            charWisdom.setText(String.valueOf(bean.getStatsBean().getWisdom()));
            charCharisma.setText(String.valueOf(bean.getStatsBean().getCharisma()));
        }
    }

    // -------------------------------------------------------------
    //                 SALVATAGGIO (BOTTONE SAVE)
    // -------------------------------------------------------------
    @FXML
    void onClickSaveCharacter(ActionEvent event) {
        setController(controller);
        if (controller == null) {
            // Se serve, gestisci l'errore qui (es. un alert)
            return;
        }
        // Semplifichiamo la logica: se creationMode è true, creiamo; altrimenti, aggiorniamo
        if (creationMode) {
            createNewCharacter();
        } else {
            updateExistingCharacter();
        }
    }

    /**
     * Crea un nuovo personaggio.
     */
    private void createNewCharacter() {
        CharacterInfoBean infoBean = buildInfoBeanFromFields();
        CharacterStatsBean statsBean = buildStatsBeanFromFields();

        CharacterSheetBean newBean = new CharacterSheetBean(infoBean, statsBean);
        controller.createCharacter(newBean);

        if (parentBoundary != null) parentBoundary.addNewCharacterBean(newBean);

    }

    /**
     * Aggiorna un personaggio esistente (currentBean), se presente.
     */
    private void updateExistingCharacter() {
        if (currentBean == null) {
            return; // non c'è nulla da aggiornare
        }

        // Se mancano i sub-bean, li creiamo
        if (currentBean.getInfoBean() == null) {
            currentBean.setInfoBean(new CharacterInfoBean());
        }
        if (currentBean.getStatsBean() == null) {
            currentBean.setStatsBean(new CharacterStatsBean());
        }

        // Aggiorniamo la parte "info"
        CharacterInfoBean infoB = currentBean.getInfoBean();
        infoB.setName(charName.getText());
        infoB.setRace(charRace.getText());
        infoB.setAge(parseIntOrZero(charAge.getText()));
        infoB.setLevel(parseIntOrZero(charLevel.getText()));

        // Aggiorniamo la parte "stats"
        CharacterStatsBean statsB = currentBean.getStatsBean();
        statsB.setStrength(parseIntOrZero(charStrenght.getText()));
        statsB.setDexterity(parseIntOrZero(charDexerity.getText()));
        statsB.setConstitution(parseIntOrZero(charConstitution.getText()));
        statsB.setIntelligence(parseIntOrZero(charIntelligence.getText()));
        statsB.setWisdom(parseIntOrZero(charWisdom.getText()));
        statsB.setCharisma(parseIntOrZero(charCharisma.getText()));

        controller.updateCharacter(currentBean);

        if (parentBoundary != null) {
            parentBoundary.refreshTable();
        }
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Errore durante il cambio scena verso characterList.fxml", e);
        }

    }

    // -------------------------------------------------------------
    //         METODI DI COSTRUZIONE DEI DUE SOTTO-BEAN
    // -------------------------------------------------------------

    private CharacterInfoBean buildInfoBeanFromFields() {
        return new CharacterInfoBean(
                charName.getText(),
                charRace.getText(),
                parseIntOrZero(charAge.getText()),
                charClass.getText(),
                parseIntOrZero(charLevel.getText())
        );
    }

    private CharacterStatsBean buildStatsBeanFromFields() {
        return new CharacterStatsBean(
                parseIntOrZero(charStrenght.getText()),
                parseIntOrZero(charDexerity.getText()),
                parseIntOrZero(charConstitution.getText()),
                parseIntOrZero(charIntelligence.getText()),
                parseIntOrZero(charWisdom.getText()),
                parseIntOrZero(charCharisma.getText())
        );
    }

    /**
     * Helper: parse int con valore di default 0 in caso di errore.
     */
    private int parseIntOrZero(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
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
            throw new SceneChangeException("Errore durante il cambio scena verso characterList.fxml", e);
        }

    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) throws IOException {
        changeScene(SceneNames.CONSULT_RULES);
    }

    @FXML
    void onClickGoToHome(ActionEvent event) throws IOException {
        changeScene(SceneNames.HOME);
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) throws IOException {
        changeScene(SceneNames.JOIN_LOBBY);
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) throws IOException {
        changeScene(SceneNames.MANAGE_LOBBY);
    }

    @FXML
    void onClickUser(ActionEvent event) throws IOException {
        changeScene(SceneNames.USER);
    }

    @FXML
    void onclickGoToMyCharList(ActionEvent event) throws IOException {
        changeScene(SceneNames.CHARACTER_LIST);
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        // Usa SceneSwitcher per cambiare scena
        Stage currentStage = (Stage) characterSheetPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);  // Cambia scena con SceneSwitcher
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

}
