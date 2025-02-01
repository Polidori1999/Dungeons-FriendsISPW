package it.uniroma2.marchidori.maininterface.scenemanager;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.*;
import it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterListBoundary;
import it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterListDMBoundary;
import it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterListPlayerBoundary;
import it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterSheetBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyDMBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyPlayerBoundary;
import it.uniroma2.marchidori.maininterface.boundary.login.LoginBoundary;
import it.uniroma2.marchidori.maininterface.boundary.login.RegisterBoundary;
import it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyBoundary;
import it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyListBoundary;
import it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyListDMBoundary;
import it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyListPlayerBoundary;
import it.uniroma2.marchidori.maininterface.boundary.user.UserBoundary;
import it.uniroma2.marchidori.maininterface.boundary.user.UserDMBoundary;
import it.uniroma2.marchidori.maininterface.boundary.user.UserPlayerBoundary;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.enumerate.SceneIdEnum;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.utils.Pair;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * SceneSwitcher: It manages the scene change based on the role and the scene ID.
 */
public class SceneSwitcher {

    // Map (role, scene) -> controller class.
    private static final Map<Pair<RoleEnum, SceneIdEnum>, Class<?>> ROLE_SCENE_MAP = new HashMap<>();

    static {
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListDMBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListPlayerBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.JOIN_LOBBY), JoinLobbyDMBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.JOIN_LOBBY), JoinLobbyPlayerBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_LIST), CharacterListDMBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_LIST), CharacterListPlayerBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.USER), UserDMBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.USER), UserPlayerBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_SHEET), CharacterSheetBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_SHEET), CharacterSheetBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.HOME), HomeBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.HOME), HomeBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.LOGIN), LoginBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.LOGIN), LoginBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.REGISTER), RegisterBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.REGISTER), RegisterBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY), ManageLobbyBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY), ManageLobbyBoundary.class);
        ROLE_SCENE_MAP.put(new Pair<>(RoleEnum.NONE, SceneIdEnum.LOGIN), LoginBoundary.class);
    }

    // Private constructor to prevent instantiation.
    private SceneSwitcher() {
    }

    public static void changeScene(Stage currentStage, String fxmlPath, UserBean currentUser) throws IOException {
        SceneIdEnum sceneId = getSceneIdFromFxml(fxmlPath);
        RoleEnum role = (currentUser != null) ? currentUser.getRoleBehavior() : RoleEnum.NONE;
        Class<?> controllerClass = getControllerClass(role, sceneId);
        Object controller = instantiateController(controllerClass);
        injectCurrentUser(controller, currentUser);
        Parent root = loadFXML(fxmlPath, controller);

        Scene newScene = new Scene(root);
        currentStage.setScene(newScene);
        currentStage.show();
    }

    private static Class<?> getControllerClass(RoleEnum role, SceneIdEnum sceneId) {
        Class<?> controllerClass = ROLE_SCENE_MAP.get(new Pair<>(role, sceneId));
        if (controllerClass == null) {
            throw new IllegalArgumentException("Nessun controller definito per ruolo = "
                    + role + " e scena = " + sceneId);
        }
        return controllerClass;
    }

    private static Object instantiateController(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new SceneChangeException("Impossibile creare il controller: " + controllerClass.getName(), e);
        }
    }

    private static void injectCurrentUser(Object controller, UserBean currentUser) {
        if (currentUser == null) {
            return;
        }
        // We use the "instanceof" operator to verify and pass the currentUser.
        if (controller instanceof HomeBoundary homeBoundary) {
            homeBoundary.setCurrentUser(currentUser);
        }
        if (controller instanceof RegisterBoundary registerBoundary) {
            registerBoundary.setCurrentUser(currentUser);
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
        if (controller instanceof UserBoundary userBoundary) {
            userBoundary.setCurrentUser(currentUser);
        }
        if (controller instanceof ManageLobbyBoundary mlBoundary) {
            mlBoundary.setCurrentUser(currentUser);
        }
        if (controller instanceof JoinLobbyBoundary jlBoundary) {
            jlBoundary.setCurrentUser(currentUser);
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
            default -> throw new IllegalArgumentException("FXML not recognized: " + fxmlPath);
        };
    }
}
