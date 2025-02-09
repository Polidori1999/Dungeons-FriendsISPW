package it.uniroma2.marchidori.maininterface.scenemanager;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.*;
import it.uniroma2.marchidori.maininterface.boundary.charactersheet.*;
import it.uniroma2.marchidori.maininterface.boundary.consultrules.ConsultRulesBoundary;
import it.uniroma2.marchidori.maininterface.boundary.consultrules.ConsultRulesGuestBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyGuestBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyPlayerBoundary;
import it.uniroma2.marchidori.maininterface.boundary.login.LoginBoundary;
import it.uniroma2.marchidori.maininterface.boundary.login.RegisterBoundary;
import it.uniroma2.marchidori.maininterface.boundary.managelobby.*;
import it.uniroma2.marchidori.maininterface.boundary.user.UserBoundary;
import it.uniroma2.marchidori.maininterface.boundary.user.UserGuestBoundary;
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

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.USER), UserBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.USER), UserBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.USER), UserGuestBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_SHEET), CharacterSheetBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_SHEET), CharacterSheetBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CHARACTER_SHEET), CharacterSheetBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, HOME), HomeBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, HOME), HomeBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.GUEST, HOME), HomeBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.LOGIN), LoginBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.LOGIN), LoginBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.REGISTER), RegisterBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.REGISTER), RegisterBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY), ManageLobbyBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY), ManageLobbyBoundary.class);

        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CONSULT_RULES), ConsultRulesBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CONSULT_RULES), ConsultRulesBoundary.class);
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

        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.LOGIN), LoginController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.LOGIN), LoginController.class);

        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.REGISTER), RegisterController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.REGISTER), RegisterController.class);


        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY), ManageLobbyController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY), ManageLobbyController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.MANAGE_LOBBY), ManageLobbyController.class);

        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CONSULT_RULES), ConsultRulesController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CONSULT_RULES), ConsultRulesController.class);
        ROLE_CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CONSULT_RULES), ConsultRulesController.class);
    }

    private SceneSwitcher() {}

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

        injectCurrentUser(boundaryInstance, currentUser);


        Class<?> controllerClass = getControllerClass(role, sceneId);
        if(controllerClass != null) {
            Object controllerInstance = createController(controllerClass);
            injectCurrentUser(controllerInstance, currentUser);
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
     * Le assunzioni sono:
     * - Il nome della classe controller è ottenuto sostituendo "Boundary" con "Controller" nel nome della boundary.
     * - La classe controller si trova in "it.uniroma2.marchidori.maininterface.controller".
     * - Il costruttore del controller prevede un parametro UserBean.
     * - La boundary espone un metodo pubblico "setController(...)" per iniettare il controller.
     */

    private static void injectControllerIntoBoundary(Object controller, Object boundary) {
        if (controller != null && boundary instanceof ControllerAwareInterface controllerAware) {
            controllerAware.setLogicController(controller);
        } else {
            logger.info(">>> [SceneSwitcher] Nessun controller da iniettare in " + boundary.getClass().getSimpleName());
        }
    }



    private static void injectCurrentUser(Object target, UserBean currentUser) {
        if (currentUser != null && target instanceof UserAwareInterface userAware) {
            userAware.setCurrentUser(currentUser);
        } else {
            logger.info(">>> [SceneSwitcher] Nessun currentUser da iniettare in " + target.getClass().getSimpleName());
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
            case "home.fxml"            -> SceneIdEnum.HOME;
            case "user.fxml"            -> SceneIdEnum.USER;
            case "register.fxml"        -> SceneIdEnum.REGISTER;
            case "login.fxml"           -> SceneIdEnum.LOGIN;
            case "consultRules.fxml"    -> SceneIdEnum.CONSULT_RULES;
            default -> throw new IllegalArgumentException("FXML not recognized: " + fxmlPath);
        };
    }




}
