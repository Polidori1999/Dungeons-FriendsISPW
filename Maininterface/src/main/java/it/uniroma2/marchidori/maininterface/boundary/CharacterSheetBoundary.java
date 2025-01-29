package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.AbilityScoresBean;
import it.uniroma2.marchidori.maininterface.bean.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Boundary per mostrare/creare/modificare i dati di un CharacterSheet
 * in forma "spezzata" (info + abilityScores).
 */
public class CharacterSheetBoundary {

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


    // ---------------------- FIELDS: sezione "abilityScores" ----------------------
    @FXML
    private TextField charStrenght;   // STR
    @FXML
    private TextField charDexerity;   // DEX
    @FXML
    private TextField charConstitution; // CON
    @FXML
    private TextField charIntelligence; // INT
    @FXML
    private TextField charWisdom;    // WIS
    @FXML
    private TextField charCharisma;  // CHA

    // Flag: true se stiamo creando, false se stiamo modificando
    private boolean creationMode;

    // Bean esistente (in caso di modifica)
    private CharacterSheetBean currentBean;

    // Riferimento al controller BCE
    private CharacterSheetController controller;

    // Riferimento alla finestra di lista (se vogliamo avvisarla di aggiornamenti)
    private CharacterListBoundary parentBoundary;

    // -------------------------------------------------------------
    //            METODI DI CONFIGURAZIONE DELLA BOUNDARY
    // -------------------------------------------------------------
    public void setController(CharacterSheetController controller) {
        this.controller = controller;
    }

    public void setParentBoundary(CharacterListBoundary parentBoundary) {
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
    //                   METODI DI UTILITY
    // -------------------------------------------------------------
    private void clearFields() {
        // Sezione "info"
        charName.setText("");
        charRace.setText("");
        charAge.setText("");
        charClass.setText("");
        charLevel.setText("");

        // Sezione "ability"
        charStrenght.setText("");
        charDexerity.setText("");
        charConstitution.setText("");
        charIntelligence.setText("");
        charWisdom.setText("");
        charCharisma.setText("");
    }

    /**
     * Legge i valori da bean.info e bean.abilityScores,
     * e li mostra nei campi di testo.
     */
    private void populateFieldsFromBean(CharacterSheetBean bean) {
        if (bean.getInfo() != null) {
            charName.setText(bean.getInfo().getName());
            charRace.setText(bean.getInfo().getRace());
            charAge.setText(bean.getInfo().getAge());
            charClass.setText(bean.getInfo().getClasse());
            charLevel.setText(bean.getInfo().getLevel());
        }
        if (bean.getAbilityScores() != null) {
            charStrenght.setText(bean.getAbilityScores().getStrength());
            charDexerity.setText(bean.getAbilityScores().getDexterity());
            charConstitution.setText(bean.getAbilityScores().getConstitution());
            charIntelligence.setText(bean.getAbilityScores().getIntelligence());
            charWisdom.setText(bean.getAbilityScores().getWisdom());
            charCharisma.setText(bean.getAbilityScores().getCharisma());
        }
    }

    /**
     * Crea un CharacterInfoBean dai campi "info".
     */
    private CharacterInfoBean buildInfoBeanFromFields() {
        return new CharacterInfoBean(
                charName.getText(),
                charRace.getText(),
                charAge.getText(),
                charClass.getText(),
                charLevel.getText()
        );
    }

    /**
     * Crea un AbilityScoresBean dai campi "ability".
     */
    private AbilityScoresBean buildAbilityBeanFromFields() {
        return new AbilityScoresBean(
                charStrenght.getText(),
                charDexerity.getText(),
                charIntelligence.getText(),
                charWisdom.getText(),
                charCharisma.getText(),
                charConstitution.getText()
        );
    }

    // -------------------------------------------------------------
    //                 SALVATAGGIO (BOTTONE SAVE)
    // -------------------------------------------------------------
    @FXML
    void onClickSaveCharacter(ActionEvent event) {
        if (controller == null) {
            System.err.println("ERRORE: Controller non impostato!");
            return;
        }

        if (creationMode) {
            // Creazione
            // 1) Ricaviamo un infoBean e un abilityBean dai campi
            CharacterInfoBean infoBean = buildInfoBeanFromFields();
            AbilityScoresBean abilityBean = buildAbilityBeanFromFields();

            // 2) Creiamo un CharacterSheetBean che le raggruppa
            CharacterSheetBean newBean = new CharacterSheetBean(infoBean, abilityBean);

            // 3) Passiamo il bean al controller per creare il personaggio
            controller.createCharacter(newBean);

            // 4) Avvisiamo la finestra di lista, se c'è
            if (parentBoundary != null) {
                parentBoundary.addNewCharacterBean(newBean);
            }
            System.out.println("Creato nuovo bean: " + newBean);

        } else {
            // Modifica di un bean esistente
            if (currentBean != null) {
                // Aggiorniamo i sub-bean
                // (se i sub-bean erano null, creiamoli: dipende dal tuo design)
                if (currentBean.getInfo() == null) {
                    currentBean.setInfo(new CharacterInfoBean());
                }
                if (currentBean.getAbilityScores() == null) {
                    currentBean.setAbilityScores(new AbilityScoresBean());
                }

                // Aggiorniamo i dati "info"
                currentBean.getInfo().setName(charName.getText());
                currentBean.getInfo().setRace(charRace.getText());
                currentBean.getInfo().setAge(charAge.getText());
                currentBean.getInfo().setClasse(charClass.getText());
                currentBean.getInfo().setLevel(charLevel.getText());

                // Aggiorniamo i dati "ability"
                currentBean.getAbilityScores().setStrength(charStrenght.getText());
                currentBean.getAbilityScores().setDexterity(charDexerity.getText());
                currentBean.getAbilityScores().setIntelligence(charIntelligence.getText());
                currentBean.getAbilityScores().setWisdom(charWisdom.getText());
                currentBean.getAbilityScores().setCharisma(charCharisma.getText());
                currentBean.getAbilityScores().setConstitution(charConstitution.getText());

                // 2) Avvisiamo il controller che aggiorni il personaggio esistente
                controller.updateCharacter(currentBean);

                System.out.println("Aggiornato bean esistente: " + currentBean);

                // 3) Avvisiamo la finestra di lista di refresh
                if (parentBoundary != null) {
                    parentBoundary.refreshTable();
                }
            }
        }

        // Chiudiamo la finestra
        Stage stage = (Stage) characterSheetPane.getScene().getWindow();
        stage.close();
    }

    // -------------------------------------------------------------
    //                     NAVIGAZIONE (RESTO)
    // -------------------------------------------------------------
    @FXML
    void onClickGoBackToList(ActionEvent event) throws IOException {
        Stage stage = (Stage) characterSheetPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) {
        changeScene("consultRules.fxml");
    }

    @FXML
    void onClickGoToHome(ActionEvent event) {
        changeScene("home.fxml");
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) {
        changeScene("joinLobby.fxml");
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        changeScene("manageLobby.fxml");
    }

    @FXML
    void onClickUser(ActionEvent event) {
        changeScene("user.fxml");
    }

    @FXML
    void onclickGoToMyCharList(ActionEvent event) {
        changeScene("characterList.fxml");
    }

    // -------------------------------------------------------------
    //                 CAMBIO SCENA (changeScene)
    // -------------------------------------------------------------
    private void changeScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/" + fxml));
            Parent root = loader.load();

            Stage stage = (Stage) characterSheetPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
