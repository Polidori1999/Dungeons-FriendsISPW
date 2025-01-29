package it.uniroma2.marchidori.maininterface.boundary;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageLobbyListBoundary {

    @FXML
    private AnchorPane manageLobbyListPane;

    @FXML
    private Button Consult_rules;

    @FXML
    private Button Joinlobby;

    @FXML
    private Button ManageLobby;

    @FXML
    private Button Mychar;

    @FXML
    private VBox Vbox;

    @FXML
    private Button goToHome;

    @FXML
    private Button newLobbyButton;

    @FXML
    private TableColumn<LobbyBean, String> tableViewDuration;

    @FXML
    private TableColumn<LobbyBean, String> tableViewLiveOrNot;

    @FXML
    private TableView<LobbyBean> tableViewLobby;

    @FXML
    private TableColumn<LobbyBean, Void> tableViewLobbyDelete;

    @FXML
    private TableColumn<LobbyBean, Void> tableViewLobbyEdit;

    @FXML
    private TableColumn<LobbyBean, String> tableViewLobbyName;

    @FXML
    private TableColumn<LobbyBean, String> tableViewMaxPlayers;

    @FXML
    private Button userButton;

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
        Stage stage = (Stage) manageLobbyListPane.getScene().getWindow(); // Alternativa: (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Crea una nuova scena e impostala nello stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
