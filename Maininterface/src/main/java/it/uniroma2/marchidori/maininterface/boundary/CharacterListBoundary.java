package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException; // <<-- IMPORT ECCEZIONE DEDICATA
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
import javafx.util.Callback;

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
        tableViewCharButton.setCellFactory(col -> new TableCell<>() {
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
        tableViewCharDelete.setCellFactory(col -> new TableCell<>() {
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
                        // Se necessario, chiamare anche un metodo su controller
                        // (ad esempio "controller.deleteCharacter(selectedBean);")
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
            // Passiamo il controller BCE
            sheetController.setController(controller);
            // Passiamo la boundary
            sheetController.setParentBoundary(this);

            Stage modalStage = new Stage();
            modalStage.setTitle("Nuovo Personaggio");
            modalStage.initOwner(CharacterPane.getScene().getWindow());
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();

            // Refresh della tabella dopo la chiusura della finestra
            tableViewChar.refresh();

        } catch (IOException e) {
            // Usare la "dedicated exception" SceneChangeException
            throw new SceneChangeException("Errore nel cambio scena (nuovo personaggio).", e);
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

            // Refresh
            tableViewChar.refresh();

        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena (modifica personaggio).", e);
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

    public void refreshTable() {
        tableViewChar.refresh();
    }

    @FXML
    void onClickGoToConsultRules(ActionEvent event) {
        try {
            changeScene("consultRules.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a consultRules.fxml", e);
        }
    }

    @FXML
    void onClickGoToHome(ActionEvent event) {
        try {
            changeScene("home.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a home.fxml", e);
        }
    }

    @FXML
    void onClickGoToJoinLobby(ActionEvent event) {
        try {
            changeScene("joinLobby.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a joinLobby.fxml", e);
        }
    }

    @FXML
    void onClickGoToManageLobby(ActionEvent event) {
        try {
            changeScene("manageLobby.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a manageLobby.fxml", e);
        }
    }

    @FXML
    void onClickMyCharacter(ActionEvent event) {
        try {
            changeScene("characterList.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a characterList.fxml", e);
        }
    }

    @FXML
    void onClickNewLobby(ActionEvent event) {
        try {
            changeScene("newLobby.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a newLobby.fxml", e);
        }
    }

    @FXML
    void onClickUser(ActionEvent event) {
        try {
            changeScene("user.fxml");
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambiare scena a user.fxml", e);
        }
    }

    @FXML
    private void changeScene(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/it/uniroma2/marchidori/maininterface/" + fxml)
        );
        Parent root = loader.load();
        Stage stage = (Stage) CharacterPane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
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
}
