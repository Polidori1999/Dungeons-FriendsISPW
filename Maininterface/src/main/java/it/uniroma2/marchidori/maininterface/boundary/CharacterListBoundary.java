package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CharacterListBoundary implements Initializable {

    @FXML
    private AnchorPane CharacterPane;

    @FXML
    private TableView<CharacterSheetBean> tableViewChar;

    @FXML
    private TableColumn<CharacterSheetBean, String> tableViewCharName;

    @FXML
    private TableColumn<CharacterSheetBean, String> tableViewCharRace;

    @FXML
    private TableColumn<CharacterSheetBean, String> tableViewCharAge;

    @FXML
    private TableColumn<CharacterSheetBean, String> tableViewCharClass;

    @FXML
    private TableColumn<CharacterSheetBean, Button> tableViewCharButton;  // colonna "Edit"
    @FXML
    private TableColumn<CharacterSheetBean, Button> tableViewCharDelete; // colonna "Delete"

    // La lista di Bean
    private final ObservableList<CharacterSheetBean> data = FXCollections.observableArrayList();

    // Controller
    private CharacterSheetController controller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inizializzo il controller
        controller = new CharacterSheetController();

        // Carico i personaggi iniziali (sotto forma di Bean)
        data.addAll(controller.getAllCharacters());

        // Associazioni colonne -> campi del Bean
        tableViewCharName.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("name"));
        tableViewCharRace.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("race"));
        tableViewCharAge.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("age"));
        tableViewCharClass.setCellValueFactory(new ReadOnlyObjectWrapperFactory<>("classe"));

        // Colonna EDIT
        tableViewCharButton.setCellValueFactory(cellData -> {
            Button editBtn = new Button("Edit");
            return new ReadOnlyObjectWrapper<>(editBtn);
        });
        tableViewCharButton.setCellFactory(col -> new TableCell<CharacterSheetBean, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    item.setOnAction(e -> {
                        CharacterSheetBean selectedChar = getTableView().getItems().get(getIndex());
                        openEditCharacterModal(selectedChar);
                    });
                }
            }
        });

        // Colonna DELETE
        tableViewCharDelete.setCellValueFactory(cellData -> {
            Button deleteBtn = new Button("Delete");
            return new ReadOnlyObjectWrapper<>(deleteBtn);
        });
        tableViewCharDelete.setCellFactory(col -> new TableCell<CharacterSheetBean, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);
                    item.setOnAction(e -> {
                        // Rimuoviamo il Bean dalla tabella
                        CharacterSheetBean selectedBean = getTableView().getItems().get(getIndex());
                        getTableView().getItems().remove(selectedBean);
                        // Potresti anche voler aggiornare la lista nel controller,
                        // se implementi un metodo "controller.deleteCharacter(selectedBean);"
                    });
                }
            }
        });

        // Impostiamo la lista
        tableViewChar.setItems(data);
    }

    @FXML
    void onClickNewCharacter(ActionEvent event) {
        openCreateCharacterModal();
    }

    // Apertura finestra secondaria per creare
    private void openCreateCharacterModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/characterSheet.fxml"));
            Parent root = loader.load();

            // Otteniamo il controller della finestra di dettaglio
            CharacterSheetBoundary sheetController = loader.getController();
            // Creazione mode
            sheetController.setCreationMode(true);
            // Passiamo un riferimento al controller BCE
            sheetController.setController(controller);
            // Passiamo un riferimento a "questa" boundary
            sheetController.setParentBoundary(this);

            Stage modalStage = new Stage();
            modalStage.setTitle("Nuovo Personaggio");
            modalStage.initOwner(CharacterPane.getScene().getWindow());
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();

            // Al ritorno, se un personaggio è stato creato,
            // la finestra secondaria potrebbe aver aggiunto
            // un Bean con "addNewCharacterBean(...)"
            tableViewChar.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Apertura finestra secondaria per edit
    private void openEditCharacterModal(CharacterSheetBean beanToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/characterSheet.fxml"));
            Parent root = loader.load();

            CharacterSheetBoundary sheetController = loader.getController();
            // No creationMode
            sheetController.setCreationMode(false);
            // Passiamo il Bean esistente
            sheetController.setCharacterSheetBean(beanToEdit);
            // Passiamo il controller BCE
            sheetController.setController(controller);
            // Passiamo la boundary
            sheetController.setParentBoundary(this);

            Stage modalStage = new Stage();
            modalStage.setTitle("Modifica Personaggio");
            modalStage.initOwner(CharacterPane.getScene().getWindow());
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();

            // Al ritorno, potremmo refreshare
            tableViewChar.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo chiamato dalla finestra di dettaglio (CharacterSheetBoundary)
     * quando viene creato un NUOVO personaggio (Bean).
     */
    public void addNewCharacterBean(CharacterSheetBean newBean) {
        data.add(newBean);
        tableViewChar.refresh();
    }

    // Se vuoi reimportare la lista dal controller, puoi farlo qui.
    // Oppure, se preferisci, basta refresh.
    public void refreshTable() {
        tableViewChar.refresh();
    }

    // Altri metodi di navigazione (onClickGoToHome, etc.) invariati

    @FXML
    void onClickGoToConsultRules(ActionEvent event) {
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
            changeScene("manageLobby.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) {
        try {
            changeScene("characterList.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickNewLobby(ActionEvent event) {
        try {
            changeScene("newLobby.fxml");
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
    private void changeScene(String fxml) throws IOException {

        // Carica il file FXML della seconda scena
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/uniroma2/marchidori/maininterface/" + fxml));
        Parent root = loader.load();

        // Ottieni lo stage attuale
        Stage stage = (Stage) CharacterPane.getScene().getWindow(); // Alternativa: (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Crea una nuova scena e impostala nello stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    // ...

    // -------------- Utility --------------
    /**
     * Per semplificare la setCellValueFactory, usiamo una piccola classe interna
     * che restituisce la property con un ReadOnlyObjectWrapper.
     */
    private static class ReadOnlyObjectWrapperFactory<S> implements javafx.util.Callback<TableColumn.CellDataFeatures<S,String>, javafx.beans.value.ObservableValue<String>> {
        private final String propertyName;
        public ReadOnlyObjectWrapperFactory(String propertyName) {
            this.propertyName = propertyName;
        }
        @Override
        public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<S, String> param) {
            // Reflection super semplice (o potresti usare PropertyValueFactory).
            // Oppure puoi usare un lambda, come hai fatto a mano.
            // Qui metto un esempio veloce per illustrare l'idea.
            // Invece, potresti fare a mano: new ReadOnlyObjectWrapper<>(param.getValue().getName()) etc.
            return new ReadOnlyObjectWrapper<>("???");
        }
    }
}