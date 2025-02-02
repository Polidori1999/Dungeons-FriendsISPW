package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException; // <<-- IMPORT ECCEZIONE DEDICATA
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class CharacterListBoundary implements UserAwareInterface {

    @FXML
    protected AnchorPane characterPane;

    @FXML
    protected TableView<CharacterSheetBean> tableViewChar;

    @FXML
    private TableColumn<CharacterSheetBean, String> tableViewCharName;

    @FXML
    private TableColumn<CharacterSheetBean, String> tableViewCharRace;

    @FXML
    private TableColumn<CharacterSheetBean, String> tableViewCharAge;

    @FXML
    private TableColumn<CharacterSheetBean, String> tableViewCharClass;

    @FXML
    protected TableColumn<CharacterSheetBean, Button> tableViewCharButton;  // colonna "Edit"

    @FXML
    protected TableColumn<CharacterSheetBean, Button> tableViewCharDelete; // colonna "Delete"

    @FXML
    protected Button newCharacterButton;

    protected UserBean currentUser;

    // La lista di Bean
    protected ObservableList<CharacterSheetBean> data = FXCollections.observableArrayList();

    // Controller
    protected CharacterSheetController controller;


    protected void initialize() {
        // Inizializzo il controller

        if (controller == null && currentUser != null) {
            this.controller = new CharacterSheetController(currentUser);
        }
        // Carico i personaggi iniziali (sotto forma di Bean)


        // Associazioni colonne -> campi del Bean
        tableViewCharName.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("name"));
        tableViewCharRace.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("race"));
        tableViewCharAge.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("age"));
        tableViewCharClass.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("classe"));



        // Impostiamo la lista
        tableViewChar.setItems(data);
    }


    public void refreshTable() {
        tableViewChar.refresh();
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
            changeScene(SceneNames.MANAGE_LOBBY);
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
    private void changeScene(String fxml) throws IOException {
        // Usa SceneSwitcher per cambiare scena
        Stage currentStage = (Stage) characterPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);  // Cambia scena con SceneSwitcher
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
                    case "age" -> new ReadOnlyObjectWrapper<>(String.valueOf(bean.getInfoBean().getAge()));
                    case "classe" -> new ReadOnlyObjectWrapper<>(bean.getInfoBean().getClasse());
                    // se per qualche ragione arriva un propertyName sconosciuto, ritorna "???"
                    default -> new ReadOnlyObjectWrapper<>("???");
                };
            }

            return new ReadOnlyObjectWrapper<>("???");
        }
    }
    //eliminare la systemout poichè debug ok
    @Override
    public void setCurrentUser(UserBean user) {
        System.out.println("SetCurrentUser chiamato con: " + user);
        this.currentUser = user;
        this.controller = new CharacterSheetController(currentUser);

        if (this.controller == null) {
            System.err.println("ERRORE: controller non è stato inizializzato!");
        } else {
            System.out.println("Controller inizializzato correttamente: " + this.controller);
        }
    }




}
