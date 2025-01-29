package it.uniroma2.marchidori.maininterface.boundary;

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

public class CharacterSheetBoundary {

    @FXML
    private AnchorPane characterSheetPane;

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

    // Flag: true se stiamo creando, false se modificando
    private boolean creationMode;
    // Bean esistente (in caso di modifica)
    private CharacterSheetBean currentBean;

    // Riferimento al controller BCE
    private CharacterSheetController controller;
    // Riferimento alla finestra di lista, se vogliamo avvisarla
    private CharacterListBoundary parentBoundary;

    public void setController(CharacterSheetController controller) {
        this.controller = controller;
    }

    public void setParentBoundary(CharacterListBoundary parentBoundary) {
        this.parentBoundary = parentBoundary;
    }

    public void setCreationMode(boolean creationMode) {
        this.creationMode = creationMode;
        if (creationMode) {
            clearFields();
            currentBean = null;
        }
    }

    public void setCharacterSheetBean(CharacterSheetBean bean) {
        this.currentBean = bean;
        this.creationMode = false;
        populateFieldsFromBean(bean);
    }

    private void clearFields() {
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

    private void populateFieldsFromBean(CharacterSheetBean bean) {
        charName.setText(bean.getName());
        charRace.setText(bean.getRace());
        charAge.setText(bean.getAge());
        charClass.setText(bean.getClasse());
        charStrenght.setText(bean.getStrength());
        charDexerity.setText(bean.getDexterity());
        charConstitution.setText(bean.getConstitution());
        charIntelligence.setText(bean.getIntelligence());
        charWisdom.setText(bean.getWisdom());
        charCharisma.setText(bean.getCharisma());
        charLevel.setText(bean.getLevel());
    }

    @FXML
    void onClickSaveCharacter(ActionEvent event) {
        if (controller == null) {
            System.out.println("ERRORE: Controller non impostato!");
            return;
        }

        if (creationMode) {
            // Creiamo un nuovo bean dai campi
            CharacterSheetBean newBean = new CharacterSheetBean(
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
            // Chiamata al controller
            controller.createCharacter(newBean);

            // Avvisiamo la finestra di lista
            if (parentBoundary != null) {
                parentBoundary.addNewCharacterBean(newBean);
            }
            System.out.println("Creato nuovo bean: " + newBean);

        } else {
            // Modifica
            if (currentBean != null) {
                currentBean.setName(charName.getText());
                currentBean.setRace(charRace.getText());
                currentBean.setAge(charAge.getText());
                currentBean.setClasse(charClass.getText());
                currentBean.setLevel(charLevel.getText());
                currentBean.setStrength(charStrenght.getText());
                currentBean.setDexterity(charDexerity.getText());
                currentBean.setIntelligence(charIntelligence.getText());
                currentBean.setWisdom(charWisdom.getText());
                currentBean.setCharisma(charCharisma.getText());
                currentBean.setConstitution(charConstitution.getText());

                // Aggiorniamo anche nel controller
                controller.updateCharacter(currentBean);

                System.out.println("Aggiornato bean esistente: " + currentBean);

                // Avvisiamo la finestra di lista di refreshare
                if (parentBoundary != null) {
                    parentBoundary.refreshTable();
                }
            }
        }

        // Chiudiamo la finestra
        Stage stage = (Stage) characterSheetPane.getScene().getWindow();
        stage.close();
    }

    ////////////////////////metodi navigazione
    @FXML
    void onClickGoBackToList(ActionEvent event) throws IOException {
        // Invece di ricaricare CharacterList.fxml, ci limitiamo a chiudere questa finestra
        Stage stage = (Stage) characterSheetPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) throws IOException {
        try {
            changeScene("consultRules.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) {
        try {
            changeScene("home.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) {
        try {
            changeScene("joinLobby.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        try {
            changeScene("manageLobbyList.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickUser(ActionEvent event) {
        try {
            changeScene("user.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @FXML
    void onclickGoToMyCharList(ActionEvent event) {
        try {
            changeScene("characterList.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        // Carica il file FXML della seconda scena
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/" + fxml));
        Parent root = loader.load();

        // Ottieni lo stage attuale
        Stage stage = (Stage) characterSheetPane.getScene().getWindow(); // Alternativa: (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Crea una nuova scena e impostala nello stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
