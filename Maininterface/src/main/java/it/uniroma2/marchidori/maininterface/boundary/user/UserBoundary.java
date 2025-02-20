package it.uniroma2.marchidori.maininterface.boundary.user;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.control.UserController;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.DM;
import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;
import static it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher.logger;

public class UserBoundary implements UserAwareInterface, ControllerAwareInterface {

    @FXML
    protected Button consultRules;

    @FXML
    protected Button joinLobby;

    @FXML
    protected Button manageLobby;

    @FXML
    protected Button myChar;

    @FXML
    protected VBox vBox;

    @FXML
    protected TextField emailUser;

    @FXML
    protected Button goToHome;

    @FXML
    protected Button logOutButton;

    @FXML
    protected TextField roleUser;

    @FXML
    protected Button userButton;

    @FXML
    protected TextField userName;

    @FXML
    protected AnchorPane userPane;

    @FXML
    protected Button switchRoleButton;

    protected User currentEntity = Session.getInstance().getCurrentUser();
    protected UserBean currentUser;
    protected UserController controller;//reference al controller relativo
    private static final Logger LOGGER = Logger.getLogger(UserBoundary.class.getName());
    private static final String SWITCHTO = "Switch Role to ";

    //metodo di inizializzazione della GUI
    @FXML
    protected void initialize() {
        if (currentUser != null) {
            roleUser.setText(currentUser.getRoleBehavior().getRoleName());
            emailUser.setText(currentUser.getEmail());
            setSwitchRoleButtonText(currentUser.getRoleBehavior());
        }
    }

    //cambio di testo del bottone per lo switch role
    private void setSwitchRoleButtonText(RoleEnum role){
        if(role == PLAYER){
            switchRoleButton.setText(SWITCHTO + DM.getRoleName());
        }else{
            switchRoleButton.setText(SWITCHTO + PLAYER.getRoleName());
        }
    }


    //metodo di navigazione tra le GUI
    @FXML
    protected void onNavigationButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String fxml = (String) sourceButton.getUserData();

        // Esegui il cambio scena
        Stage currentStage = (Stage) userPane.getScene().getWindow();
        try {
            SceneSwitcher.changeScene(currentStage, fxml, currentUser);
        } catch (IOException e) {
            // Se preferisci, potresti usare un messaggio più "dinamico", come:
            // "Error during change scene from ManageLobbyListBoundary to " + fxml
            throw new SceneChangeException("Error during change scene.", e);
        }
    }


    //gestione vento click del bottone switch role
    @FXML
    protected void onClickSwitchRole(ActionEvent event) {
        logger.info("Ruolo prima dello switch: " + currentUser.getRoleBehavior());
        controller.switchRole(currentUser.getRoleBehavior());
        currentUser = Converter.convert(Session.getInstance().getCurrentUser());
        roleUser.setText(currentUser.getRoleBehavior().getRoleName());
        setSwitchRoleButtonText(currentUser.getRoleBehavior());
        LOGGER.info("Switched role to: " + currentUser.getRoleBehavior().getRoleName());
    }


    //metodi per la realizzazione delle interfacce userawareinterface e controllerawareinterface
    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (UserController) logicController;
    }
}
