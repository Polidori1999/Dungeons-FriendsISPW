package it.uniroma2.marchidori.maininterface.scenemanager;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.*;
import it.uniroma2.marchidori.maininterface.boundary.charactersheet.*;
import it.uniroma2.marchidori.maininterface.boundary.consultrules.ConsultRulesBoundary;
import it.uniroma2.marchidori.maininterface.boundary.consultrules.ConsultRulesDMORPlayerBoundary;
import it.uniroma2.marchidori.maininterface.boundary.consultrules.ConsultRulesGuestBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyGuestBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyPlayerBoundary;
import it.uniroma2.marchidori.maininterface.boundary.login.LoginBoundary;
import it.uniroma2.marchidori.maininterface.boundary.login.RegisterBoundary;
import it.uniroma2.marchidori.maininterface.boundary.managelobby.*;
import it.uniroma2.marchidori.maininterface.boundary.user.UserBoundary;
import it.uniroma2.marchidori.maininterface.boundary.user.UserDMBoundary;
import it.uniroma2.marchidori.maininterface.boundary.user.UserGuestBoundary;
import it.uniroma2.marchidori.maininterface.boundary.user.UserPlayerBoundary;
import it.uniroma2.marchidori.maininterface.control.*;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.enumerate.SceneIdEnum;
import it.uniroma2.marchidori.maininterface.utils.Pair;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static it.uniroma2.marchidori.maininterface.enumerate.SceneIdEnum.HOME;
import static it.uniroma2.marchidori.maininterface.factory.BoundaryFactory.createBoundary;
import static it.uniroma2.marchidori.maininterface.factory.ControllerFactory.createController;


public class SceneSwitcher {
    public static final Logger logger = Logger.getLogger(SceneSwitcher.class.getName());
    

    // Mappa (ruolo, scena) -> classe della boundary
    private static final Map<Pair<RoleEnum, SceneIdEnum>, Class<?>> ROLE_SCENE_MAP = new HashMap<>();

    static {
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListDMBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListPlayerBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListGuestBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.JOIN_LOBBY), JoinLobbyBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.JOIN_LOBBY), JoinLobbyPlayerBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.JOIN_LOBBY), JoinLobbyGuestBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_LIST), CharacterListDMBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_LIST), CharacterListPlayerBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CHARACTER_LIST), CharacterListGuestBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.USER), UserDMBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.USER), UserPlayerBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.USER), UserGuestBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_SHEET), CharacterSheetBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_SHEET), CharacterSheetBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CHARACTER_SHEET), CharacterSheetBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, HOME), HomeBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, HOME), HomeBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.GUEST, HOME), HomeBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.LOGIN), LoginBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.LOGIN), LoginBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.LOGIN), LoginBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.REGISTER), RegisterBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.REGISTER), RegisterBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY), ManageLobbyBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY), ManageLobbyBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.NONE, SceneIdEnum.LOGIN), LoginBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CONSULT_RULES), ConsultRulesBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CONSULT_RULES), ConsultRulesDMORPlayerBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CONSULT_RULES), ConsultRulesGuestBoundary.class);
    }

    private static final Map<Pair<RoleEnum, SceneIdEnum>, Class<?>> ROLE_CONTROLLER_MAP = new HashMap<>();

    static{
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListController.class);

        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.JOIN_LOBBY), JoinLobbyController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.JOIN_LOBBY), JoinLobbyController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.JOIN_LOBBY), JoinLobbyController.class);

        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CHARACTER_LIST), CharacterListController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_LIST), CharacterListController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_LIST), CharacterListController.class);

        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.USER), UserController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.USER), UserController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.USER), UserController.class);

        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_SHEET), CharacterSheetController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_SHEET), CharacterSheetController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CHARACTER_SHEET), CharacterSheetController.class);

        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.LOGIN), LoginController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.LOGIN), LoginController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.LOGIN), LoginController.class);

        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.REGISTER), RegisterController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.REGISTER), RegisterController.class);


        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY), ManageLobbyController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY), ManageLobbyController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.MANAGE_LOBBY), ManageLobbyController.class);

        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.NONE, SceneIdEnum.LOGIN), LoginController.class);

        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CONSULT_RULES), ConsultRulesController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CONSULT_RULES), ConsultRulesController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CONSULT_RULES), ConsultRulesController.class);
    }

    private SceneSwitcher() {}

    private static final String Y = " con ruolo: ";


    public static void changeScene(Stage currentStage, String fxmlPath, UserBean currentUser) throws IOException {
        //evitare scene duplicate
        if (currentStage.getScene() != null && fxmlPath.equals(currentStage.getScene().getUserData())) {
            return; // Se la scena è già attiva, non fare nulla
        }
        // Ottieni lo SceneIdEnum corrispondente al file FXML richiesto
        SceneIdEnum sceneId = getSceneIdFromFxml(fxmlPath);
        //TOLTO PER MODIFICARE
        RoleEnum role = (currentUser != null) ? currentUser.getRoleBehavior() : RoleEnum.NONE;



        // Risolvi la classe boundary in base al mapping
        Class<?> boundaryClass = getBoundaryClass(role, sceneId);
        logger.info(">>> [SceneSwitcher] Boundary risolta: " + boundaryClass.getSimpleName());

        // Istanzia la boundary
        Object boundaryInstance = createBoundary(boundaryClass);
        logger.info(">>> [SceneSwitcher] Istanza della boundary creata: " + boundaryInstance.getClass().getSimpleName());

        injectCurrentUserBoundary(boundaryInstance, currentUser);


        Class<?> controllerClass = getControllerClass(role, sceneId);
        if(controllerClass != null) {
            Object controllerInstance = createController(controllerClass);
            injectCurrentUserController(controllerInstance, currentUser);
            injectControllerIntoBoundary(controllerInstance,boundaryInstance);

        }

        // Carica il file FXML con il controller specificato (la boundary che ora contiene anche il controller associato)
        Parent root = loadFXML(fxmlPath, boundaryInstance);
        Scene newScene = new Scene(root);
        currentStage.setScene(newScene);
        currentStage.show();
    }



    private static Class<?> getBoundaryClass(RoleEnum role, SceneIdEnum sceneId) {
        Class<?> boundaryClass = ROLE_SCENE_MAP.get(new Pair<>(role, sceneId));


        if (boundaryClass == null) {
            throw new IllegalArgumentException("Nessuna boundary definita per ruolo = "
                    + role + " e scena = " + sceneId);
        }
        return boundaryClass;
    }

    private static Class<?> getControllerClass(RoleEnum role, SceneIdEnum sceneId) {
        if(sceneId == HOME){
            return null;
        }

        Class<?> controllerClass = ROLE_CONTROLLER_MAP.get(new Pair<>(role, sceneId));

        if (controllerClass == null) {
            throw new IllegalArgumentException("Nessuna boundary definita per ruolo = "
                    + role + " e scena = " + sceneId);
        }
        return controllerClass;
    }

    /**
     * Inietta nella boundary il controller associato.
     *
     * Le assunzioni sono:
     * - Il nome della classe controller è ottenuto sostituendo "Boundary" con "Controller" nel nome della boundary.
     * - La classe controller si trova in "it.uniroma2.marchidori.maininterface.controller".
     * - Il costruttore del controller prevede un parametro UserBean.
     * - La boundary espone un metodo pubblico "setController(...)" per iniettare il controller.
     */

    private static final String X = ">>> [SceneSwitcher] Nessun currentUser da iniettare.";

    private static void injectControllerIntoBoundary(Object controller, Object boundary) {


        if (controller == null) {
            logger.info(X);
            return;
        }

        if (boundary instanceof UserBoundary userBoundary) {
            userBoundary.setLogicController(controller);
        }

        if (boundary instanceof CharacterSheetBoundary charBoundary) {
            charBoundary.setLogicController(controller);
        }
        if (boundary instanceof CharacterListBoundary charLBoundary) {
            charLBoundary.setLogicController(controller);
        }
        if (boundary instanceof ManageLobbyListBoundary manageLobbyListBoundary) {
            manageLobbyListBoundary.setLogicController(controller);
        }
        if(boundary instanceof ManageLobbyBoundary manageLobbyBoundary) {
            manageLobbyBoundary.setLogicController(controller);
        }
        if (boundary instanceof JoinLobbyBoundary jlBoundary) {
            jlBoundary.setLogicController(controller);
        }
        if (boundary instanceof ConsultRulesBoundary consultRulesBoundary) {
            consultRulesBoundary.setLogicController(controller);
        }

        if (boundary instanceof RegisterBoundary registerBoundary) {
            registerBoundary.setLogicController(controller);
        }
        if (boundary instanceof LoginBoundary loginBoundary) {
            loginBoundary.setLogicController(controller);
        }


    }



    private static void injectCurrentUserBoundary(Object controller, UserBean currentUser) {
        if (currentUser == null) {
            logger.info(X);
            logger.info(">>> ERRORE: currentUser è NULL! La scena potrebbe avere problemi.");
            return;
        }

        logger.info(">>> [SceneSwitcher] Iniezione utente nel controller " + controller.getClass().getSimpleName() +
                Y + currentUser.getRoleBehavior());

        if (controller instanceof RegisterBoundary registerBoundary) {
            registerBoundary.setCurrentUser(currentUser);
        }

        if (controller instanceof UserBoundary userBoundary) {
            userBoundary.setCurrentUser(currentUser);
        }
        if (controller instanceof HomeBoundary homeBoundary) {
            homeBoundary.setCurrentUser(currentUser);
        }
        if (controller instanceof CharacterSheetBoundary charBoundary) {
            charBoundary.setCurrentUser(currentUser);
        }
        if (controller instanceof CharacterListBoundary charLBoundary) {
            charLBoundary.setCurrentUser(currentUser);
        }
        if (controller instanceof ManageLobbyListBoundary manageLobbyListBoundary) {
            manageLobbyListBoundary.setCurrentUser(currentUser);
        }
        if(controller instanceof ManageLobbyBoundary manageLobbyBoundary) {
            manageLobbyBoundary.setCurrentUser(currentUser);
        }
        if (controller instanceof JoinLobbyBoundary jlBoundary) {
            jlBoundary.setCurrentUser(currentUser);
        }
        if (controller instanceof ConsultRulesBoundary consultRulesBoundary) {
            consultRulesBoundary.setCurrentUser(currentUser);
        }
        if (controller instanceof LoginBoundary loginBoundary) {
            loginBoundary.setCurrentUser(currentUser);
        }
    }


    private static void injectCurrentUserController(Object controller, UserBean currentUser) {
        if (currentUser == null) {
            logger.info(X);
            return;
        }

        logger.info(">>> [SceneSwitcher] Iniezione utente nel controller " + controller.getClass().getSimpleName() +
                Y + currentUser.getRoleBehavior());

        if (controller instanceof RegisterController registerController) {
            registerController.setCurrentUser(currentUser);
        }

        if (controller instanceof UserController userController) {
            userController.setCurrentUser(currentUser);
        }
        if (controller instanceof CharacterSheetController charController) {
            charController.setCurrentUser(currentUser);
        }
        if (controller instanceof CharacterListController charLController) {
            charLController.setCurrentUser(currentUser);
        }
        if (controller instanceof ManageLobbyListController manageLobbyListController) {
            manageLobbyListController.setCurrentUser(currentUser);
        }
        if(controller instanceof ManageLobbyController manageLobbyController) {
            manageLobbyController.setCurrentUser(currentUser);
        }
        if (controller instanceof JoinLobbyController jlController) {
            jlController.setCurrentUser(currentUser);
        }
        if (controller instanceof ConsultRulesController consultRulesController) {
            consultRulesController.setCurrentUser(currentUser);
        }
        if (controller instanceof LoginController loginController) {
            loginController.setCurrentUser(currentUser);
        }
    }





    private static Parent loadFXML(String fxmlPath, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(
                "/it/uniroma2/marchidori/maininterface/" + fxmlPath));
        loader.setController(controller);

        return loader.load();
    }

    private static SceneIdEnum getSceneIdFromFxml(String fxmlPath) {
        return switch (fxmlPath) {
            case "manageLobbyList.fxml" -> SceneIdEnum.MANAGE_LOBBY_LIST;
            case "joinLobby.fxml"       -> SceneIdEnum.JOIN_LOBBY;
            case "characterList.fxml"   -> SceneIdEnum.CHARACTER_LIST;
            case "characterSheet.fxml"  -> SceneIdEnum.CHARACTER_SHEET;
            case "manageLobby.fxml"     -> SceneIdEnum.MANAGE_LOBBY;
            case "home.fxml"            -> HOME;
            case "user.fxml"            -> SceneIdEnum.USER;
            case "register.fxml"        -> SceneIdEnum.REGISTER;
            case "login.fxml"           -> SceneIdEnum.LOGIN;
            case "consultRules.fxml"    -> SceneIdEnum.CONSULT_RULES;
            default -> throw new IllegalArgumentException("FXML not recognized: " + fxmlPath);
        };
    }




}
