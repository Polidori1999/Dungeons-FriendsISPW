package it.uniroma2.marchidori.maininterface.boundary.charactersheet;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;

import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import it.uniroma2.marchidori.maininterface.utils.TableColumnUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Logger;

public class CharacterListPlayerBoundary extends CharacterListDMBoundary {

    private static final Logger logger = Logger.getLogger(CharacterListPlayerBoundary.class.getName());

    // Colonna per il pulsante Edit (aggiunta)
    @FXML
    protected TableColumn<CharacterSheetBean, Button> tableViewCharButton;

    @Override
    @FXML
    protected void initialize() {
        super.initialize();
        data.clear();
        data.addAll(currentUser.getCharacterSheets());

        // Abilita il pulsante per creare un nuovo personaggio
        newCharacterButton.setVisible(true);
        newCharacterButton.setDisable(false);

        // Setup della colonna Edit
        TableColumnUtils.setupButtonColumn(tableViewCharButton, "Edit", this::editChar);
        tableViewCharButton.setVisible(true);


        logger.info("Player Boundary inizializzato (edit e create abilitati).");

    }

    /**
     * Handler per la creazione di un nuovo personaggio.
     */
    @Override
    @FXML
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();

        if (SceneNames.MANAGE_LOBBY.equals(fxml)) {  // ipotizzando di avere una costante dedicata
            newChar();
        } else {
            super.onNavigationButtonClick(event);
        }

    }

    protected void newChar() {
        // Imposta la lobby da editare nel currentUser
        currentUser.setSelectedLobbyName(null);
        // Cambia scena (usa il metodo protetto ereditato dalla superclasse)
        try {
            changeScene(SceneNames.CHARACTER_SHEET);
            reloadCharacterList();
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (crea Lobby).", e);
        }
    }
    /**
     * Handler per l'edit di un personaggio esistente.
     */
    private void editChar(CharacterSheetBean bean) {
        if (bean != null && bean.getInfoBean() != null) {
            logger.info("editChar: bean passato con nome: " + bean.getInfoBean().getName());
        } else {
            logger.warning("editChar: bean o infoBean è null");
        }
        currentUser.setSelectedLobbyName(bean.getInfoBean().getName());
        logger.info("editChar: selectedLobbyName settato a: " + currentUser.getSelectedLobbyName());

        try {
            SceneSwitcher.changeScene(
                    (Stage) characterPane.getScene().getWindow(),
                    SceneNames.CHARACTER_SHEET,
                    currentUser
            );
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena per edit del personaggio.", e);
        }
    }
}
