package it.uniroma2.marchidori.maininterface.boundary.user;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.UserController;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.DM;
import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;

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
    protected UserController controller;
    private static final Logger LOGGER = Logger.getLogger(UserBoundary.class.getName());


    /**
     * Invocato automaticamente da JavaFX dopo l'iniezione dei nodi @FXML.
     */
    @FXML
    protected void initialize() {
        if (currentUser != null) {
            roleUser.setText(currentUser.getRoleBehavior().getRoleName());
            emailUser.setText(currentUser.getEmail());
            switchRoleButton.setText("Switch Role to " + currentUser.getRoleBehavior().getRoleName());
        }
    }


    @FXML
    protected void onClickGoToConsultRules(ActionEvent event) {
        try {
            changeScene(SceneNames.CONSULT_RULES);
        } catch (Exception e) {
            throw new SceneChangeException("Error during change scene from user to consult rules.", e);
        }
    }

    @FXML
    protected void onClickGoToHome(ActionEvent event) {
        try {
            changeScene(SceneNames.HOME);
        } catch (Exception e) {
            throw new SceneChangeException("Error during change scene from user to home.", e);
        }
    }

    @FXML
    protected void onClickGoToJoinLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.JOIN_LOBBY);
        } catch (Exception e) {
            throw new SceneChangeException("Error during change scene from user to join lobby.", e);
        }
    }

    @FXML
    protected void onClickGoToManageLobby(ActionEvent event) {
        try {
            changeScene(SceneNames.MANAGE_LOBBY_LIST);
        } catch (Exception e) {
            throw new SceneChangeException("Error during change scene from user to manage lobby list.", e);
        }
    }

    @FXML
    protected void onClickLogOut(ActionEvent event) {
        try {
            Session.getInstance().clear();
            changeScene(SceneNames.LOGIN);
        } catch (Exception e) {
            throw new SceneChangeException("Error during change scene from user to login.", e);
        }
    }

    @FXML
    protected void onClickMyCharacter(ActionEvent event){
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (Exception e) {
            throw new SceneChangeException("Error during change scene from user to my character list.", e);
        }
    }

    @FXML
    protected void onClickUser(ActionEvent event) {
        try {
            changeScene(SceneNames.USER);
        } catch (IOException e) {
            throw new SceneChangeException("Error during change scene from user to user.", e);
        }
    }

    @FXML
    protected void onClickSwitchRole(ActionEvent event) {
        String switchTo= "Switch Role to";
        controller.switchRole(currentUser.getRoleBehavior());
        roleUser.setText(currentUser.getRoleBehavior().getRoleName());
        if(currentUser.getRoleBehavior() == PLAYER){
            switchRoleButton.setText(switchTo + DM.getRoleName());
            return;
        }else{
            switchRoleButton.setText(switchTo + PLAYER.getRoleName());
        }
        LOGGER.log(Level.INFO, () -> "Switched role to: " + currentUser.getRoleBehavior().getRoleName());

    }

    @FXML
    protected void changeScene(String fxml) throws IOException {
        Stage currentStage = (Stage) userPane.getScene().getWindow();
        SceneSwitcher.changeScene(currentStage, fxml, currentUser);  // Cambia scena con SceneSwitcher
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (UserController) logicController;
    }
}
