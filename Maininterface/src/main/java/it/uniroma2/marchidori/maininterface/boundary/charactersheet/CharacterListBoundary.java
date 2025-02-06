package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.CharacterListController;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

import java.util.List;
import java.util.logging.Logger;

public class CharacterListBoundary implements UserAwareInterface, ControllerAwareInterface {

    @FXML
    protected AnchorPane characterPane;

    @FXML
    protected TableView<CharacterSheetBean> tableViewChar;

    @FXML
    protected TableColumn<CharacterSheetBean, String> tableViewCharName;

    @FXML
    protected TableColumn<CharacterSheetBean, String> tableViewCharRace;

    @FXML
    protected TableColumn<CharacterSheetBean, String> tableViewCharLevel;

    @FXML
    protected TableColumn<CharacterSheetBean, String> tableViewCharClass;

    @FXML
    protected TableColumn<CharacterSheetBean, Button> tableViewCharButton;

    @FXML
    protected TableColumn<CharacterSheetBean, Button> tableViewCharDownloadButton;  // colonna "Edit"

    @FXML
    protected TableColumn<CharacterSheetBean, Button> tableViewCharDelete; // colonna "Delete"

    @FXML
    protected AnchorPane confirmationPane;

    @FXML
    protected Button noButton;

    @FXML
    protected Button yesButton;

    @FXML
    protected Label timerLabel;

    @FXML
    protected Label confirmationLabel;


    @FXML
    protected Button newCharacterButton;

    protected UserBean currentUser;

    // La lista di Bean
    protected ObservableList<CharacterSheetBean> data = FXCollections.observableArrayList();

    // Controller
    protected CharacterListController controller;

    private static final Logger logger = Logger.getLogger(CharacterListBoundary.class.getName());



    protected void initialize() {
        // Inizializzo il controller


        data.clear();
        data.addAll(currentUser.getCharacterSheets());
        // Carico i personaggi iniziali (sotto forma di Bean)

        newCharacterButton.setVisible(false);
        newCharacterButton.setDisable(true);
        // Associazioni colonne -> campi del Bean
        tableViewCharName.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("name"));
        tableViewCharRace.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("race"));
        tableViewCharLevel.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("level"));
        tableViewCharClass.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("classe"));

        // Impostiamo la lista
        tableViewChar.setItems(data);
    }


    public void refreshTable() {
        if (currentUser != null) {
            data.clear();
            List<CharacterSheetBean> updatedList = currentUser.getCharacterSheets(); // Prendi i dati aggiornati
            data.addAll(updatedList); // Riaggiungi i dati aggiornati
            tableViewChar.refresh();
            logger.info(">>> DEBUG: Tabella aggiornata con personaggi aggiornati.");
        }
    }


    @FXML
    void onClickMyCharacter(ActionEvent event) {
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a characterList.fxml", e);
        }

    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) {
        try {
            changeScene(SceneNames.CONSULT_RULES);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a consultRules.fxml", e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) {
        try {
            changeScene(SceneNames.HOME);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a home.fxml", e);
        }
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.JOIN_LOBBY);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a joinLobby.fxml", e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.MANAGE_LOBBY_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a manageLobby.fxml", e);
        }
    }


    @FXML
    void onClickUser(ActionEvent event) {
        try {
            changeScene(SceneNames.USER);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a user.fxml", e);
        }
    }

    @FXML
    protected void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) characterPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);

        if (fxml.equals(SceneNames.CHARACTER_LIST)) {
            reloadCharacterList(); // Forza il caricamento dei dati aggiornati quando torni alla tabella
        }
    }


    // -------------- Utility --------------

    /**
     * Modificata in modo da restituire i dati effettivi di CharacterSheetBean
     * (invece della stringa fissa "???").
     */
    private record ReadOnlyObjectWrapperFactory<S>(String propertyName)
            implements Callback<TableColumn.CellDataFeatures<S, String>, javafx.beans.value.ObservableValue<String>> {

        @Override
        public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<S, String> param) {
            S rowItem = param.getValue();

            // Accertiamoci che l'oggetto nella riga sia un CharacterSheetBean
            if (rowItem instanceof CharacterSheetBean bean) {
                // In base al "propertyName", ritorniamo il valore appropriato
                return switch (propertyName) {
                    case "name" -> new ReadOnlyObjectWrapper<>(bean.getInfoBean().getName());
                    case "race" -> new ReadOnlyObjectWrapper<>(bean.getInfoBean().getRace());
                    case "level" -> new ReadOnlyObjectWrapper<>(String.valueOf(bean.getInfoBean().getLevel()));
                    case "classe" -> new ReadOnlyObjectWrapper<>(bean.getInfoBean().getClasse());
                    // se per qualche ragione arriva un propertyName sconosciuto, ritorna "???"
                    default -> new ReadOnlyObjectWrapper<>("???");
                };
            }

            return new ReadOnlyObjectWrapper<>("???");
        }
    }
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (CharacterListController) logicController;
    }

    public void reloadCharacterList() {
        if (currentUser != null) {
            data.clear();
            data.addAll(currentUser.getCharacterSheets()); // Carica i dati aggiornati dallo UserBean
            tableViewChar.refresh();
            logger.info(">>> DEBUG: Tabella ricaricata con personaggi aggiornati.");
        }
    }
}