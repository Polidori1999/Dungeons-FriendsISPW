package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.CharacterStatsBean;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Boundary per mostrare/creare/modificare i dati di un CharacterSheet
 * in forma "spezzata" (info + statsScores).
 */
public class CharacterSheetBoundary {

    // ---------------------------------
    // LOGGING: sostituisce System.err
    // ---------------------------------
    private static final Logger LOGGER = Logger.getLogger(CharacterSheetBoundary.class.getName());

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

        // Sezione "stats"
        charStrenght.setText("");
        charDexerity.setText("");
        charConstitution.setText("");
        charIntelligence.setText("");
        charWisdom.setText("");
        charCharisma.setText("");
    }

    /**
     * Legge i valori da bean.info e bean.statsScores,
     * e li mostra nei campi di testo.
     */
    private void populateFieldsFromBean(CharacterSheetBean bean) {
        if (bean.getInfoBean() != null) {
            charName.setText(bean.getInfoBean().getName());
            charRace.setText(bean.getInfoBean().getRace());
            charAge.setText(String.valueOf(bean.getInfoBean().getAge())); // poichè dobbiamo convertire
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

    /**
     * Crea un CharacterInfoBean dai campi "info".
     */
    private CharacterInfoBean buildInfoBeanFromFields() {
        int ageInt = 0;
        int levelInt = 0;
        try {
            ageInt = Integer.parseInt(charAge.getText());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Errore nell'età impostata, uso 0 di default", e);
        }
        try {
            levelInt = Integer.parseInt(charLevel.getText());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Errore nel livello impostato, uso 0 di default", e);
        }

        return new CharacterInfoBean(
                charName.getText(),
                charRace.getText(),
                ageInt,
                charClass.getText(),
                levelInt
        );
    }

    /**
     * Crea un statsScoresBean dai campi "stats".
     */
    private CharacterStatsBean buildStatsBeanFromFields() {
        int strenghInt = 0;
        int dexInt     = 0;
        int constInt   = 0;
        int intellInt  = 0;
        int wisInt     = 0;
        int charismaInt= 0;

        try {
            strenghInt = Integer.parseInt(charStrenght.getText());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Valore di forza non valido, impostato a 0", e);
        }
        try {
            dexInt = Integer.parseInt(charDexerity.getText());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Valore di destrezza non valido, impostato a 0", e);
        }
        try {
            constInt = Integer.parseInt(charConstitution.getText());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Valore di costituzione non valido, impostato a 0", e);
        }
        try {
            intellInt = Integer.parseInt(charIntelligence.getText());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Valore di intelligenza non valido, impostato a 0", e);
        }
        try {
            wisInt = Integer.parseInt(charWisdom.getText());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Valore di saggezza non valido, impostato a 0", e);
        }
        try {
            charismaInt = Integer.parseInt(charCharisma.getText());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Valore di carisma non valido, impostato a 0", e);
        }

        return new CharacterStatsBean(
                strenghInt,
                dexInt,
                constInt,
                intellInt,
                wisInt,
                charismaInt
        );
    }

    // -------------------------------------------------------------
    //                 SALVATAGGIO (BOTTONE SAVE)
    // -------------------------------------------------------------
    @FXML
    void onClickSaveCharacter(ActionEvent event) {
        if (controller == null) {
            LOGGER.warning("Controller non impostato! Impossibile salvare il personaggio.");
            return;
        }

        if (creationMode) {
            // Creazione
            // 1) Ricaviamo un infoBean e un statsBean dai campi
            CharacterInfoBean infoBean = buildInfoBeanFromFields();
            CharacterStatsBean statsBean = buildStatsBeanFromFields();

            // 2) Creiamo un CharacterSheetBean che le raggruppa
            CharacterSheetBean newBean = new CharacterSheetBean(infoBean, statsBean);

            // 3) Passiamo il bean al controller per creare il personaggio
            controller.createCharacter(newBean);

            // 4) Avvisiamo la finestra di lista, se c'è
            if (parentBoundary != null) {
                parentBoundary.addNewCharacterBean(newBean);
            }
            LOGGER.info("Creato nuovo bean: " + newBean);

        } else {
            // Modifica di un bean esistente
            if (currentBean != null) {
                // Aggiorniamo i sub-bean
                if (currentBean.getInfoBean() == null) {
                    currentBean.setInfoBean(new CharacterInfoBean());
                }
                if (currentBean.getStatsBean() == null) {
                    currentBean.setStatsBean(new CharacterStatsBean());
                }

                // Aggiorniamo i dati "info"
                CharacterInfoBean infoB = currentBean.getInfoBean();
                infoB.setName(charName.getText());
                infoB.setRace(charRace.getText());

                try {
                    infoB.setAge(Integer.parseInt(charAge.getText()));
                } catch (NumberFormatException e) {
                    infoB.setAge(0);
                    LOGGER.log(Level.WARNING, "Valore di età non valido, impostato a 0", e);
                }
                try {
                    infoB.setLevel(Integer.parseInt(charLevel.getText()));
                } catch (NumberFormatException e) {
                    infoB.setLevel(0);
                    LOGGER.log(Level.WARNING, "Valore di livello non valido, impostato a 0", e);
                }

                // Aggiorniamo i dati "stats"
                CharacterStatsBean statsB = currentBean.getStatsBean();
                try {
                    statsB.setStrength(Integer.parseInt(charStrenght.getText()));
                } catch (NumberFormatException e) {
                    statsB.setStrength(0);
                    LOGGER.log(Level.WARNING, "Valore di forza non valido, impostato a 0", e);
                }
                try {
                    statsB.setDexterity(Integer.parseInt(charDexerity.getText()));
                } catch (NumberFormatException e) {
                    statsB.setDexterity(0);
                    LOGGER.log(Level.WARNING, "Valore di destrezza non valido, impostato a 0", e);
                }
                try {
                    statsB.setConstitution(Integer.parseInt(charConstitution.getText()));
                } catch (NumberFormatException e) {
                    statsB.setConstitution(0);
                    LOGGER.log(Level.WARNING, "Valore di costituzione non valido, impostato a 0", e);
                }
                try {
                    statsB.setIntelligence(Integer.parseInt(charIntelligence.getText()));
                } catch (NumberFormatException e) {
                    statsB.setIntelligence(0);
                    LOGGER.log(Level.WARNING, "Valore di intelligenza non valido, impostato a 0", e);
                }
                try {
                    statsB.setWisdom(Integer.parseInt(charWisdom.getText()));
                } catch (NumberFormatException e) {
                    statsB.setWisdom(0);
                    LOGGER.log(Level.WARNING, "Valore di saggezza non valido, impostato a 0", e);
                }
                try {
                    statsB.setCharisma(Integer.parseInt(charCharisma.getText()));
                } catch (NumberFormatException e) {
                    statsB.setCharisma(0);
                    LOGGER.log(Level.WARNING, "Valore di carisma non valido, impostato a 0", e);
                }

                // 2) Avvisiamo il controller che aggiorni il personaggio esistente
                controller.updateCharacter(currentBean);

                LOGGER.info("Aggiornato bean esistente: " + currentBean);

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
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/it/uniroma2/marchidori/maininterface/" + fxml)
            );
            Parent root = loader.load();

            Stage stage = (Stage) characterSheetPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException e) {
            // In un passaggio successivo, potresti anche lanciare una SceneChangeException
            LOGGER.log(
                    Level.SEVERE,
                    String.format("Errore nel cambio scena a %s", fxml),
                    e
            );//SEVERE perchè è grave

        }
    }
}
