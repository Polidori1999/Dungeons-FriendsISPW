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
 * Esempio di SceneSwitcher col "runtime check" per più boundary differenti.
 */
public class SceneSwitcher {

    // Mappa (ruolo, scena) -> classe generica
    // Nota: Per le scene che non richiedono un ruolo, utilizzare RoleEnum.NONE.
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

    private SceneSwitcher() {
        // costruttore privato
    }

    public static void changeScene(Stage currentStage, String fxmlPath, UserBean currentUser) throws IOException {

        SceneIdEnum sceneId = getSceneIdFromFxml(fxmlPath);

        // Se currentUser è null, utilizziamo RoleEnum.NONE
        RoleEnum role = (currentUser != null) ? currentUser.getRoleBehavior() : RoleEnum.NONE;

        // Otteniamo la classe associata alla coppia (ruolo, scena)
        Class<?> controllerClass = ROLE_SCENE_MAP.get(new Pair<>(role, sceneId));
        if (controllerClass == null) {
            throw new IllegalArgumentException("Nessun controller definito per ruolo = "
                    + role + " e scena = " + sceneId);
        }

        // Istanziazione via reflection
        Object controller;
        try {
            controller = controllerClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new SceneChangeException("Impossibile creare il controller: " + controllerClass.getName(), e);
        }

        // =======================
        // CHECK RUNTIME MULTIPLI
        // =======================
        // Se currentUser non è null, passalo al controller
        if (currentUser != null) {
            if(controller instanceof HomeBoundary homeBoundary) {
                homeBoundary.setCurrentUser(currentUser);
            }

            if(controller instanceof RegisterBoundary registerBoundary) {
                registerBoundary.setCurrentUser(currentUser);
            }

            if(controller instanceof CharacterSheetBoundary charBoundary) {
                charBoundary.setCurrentUser(currentUser);
            }

            if(controller instanceof CharacterListBoundary charLBoundary) {
                charLBoundary.setCurrentUser(currentUser);
            }

            if(controller instanceof ManageLobbyListBoundary manageLobbyListBoundary) {
                manageLobbyListBoundary.setCurrentUser(currentUser);
            }

            // 1) Se implementa un'interfaccia "UserBoundary", passiamo l'utente
            if (controller instanceof UserBoundary userBoundary) {
                userBoundary.setCurrentUser(currentUser);
            }

            // 2) Se implementa "ManageLobbyBoundary", facciamo un init differente
            if (controller instanceof ManageLobbyBoundary mlBoundary) {
                mlBoundary.setCurrentUser(currentUser);
            }

            // 3) Se implementa "JoinLobbyBoundary", facciamo altre operazioni
            if (controller instanceof JoinLobbyBoundary jlBoundary) {
                jlBoundary.setCurrentUser(currentUser);
            }
        }

        // Ora carichiamo l'FXML
        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(
                "/it/uniroma2/marchidori/maininterface/" + fxmlPath
        ));
        loader.setController(controller);

        Parent root = loader.load();
        Scene newScene = new Scene(root);
        currentStage.setScene(newScene);
        currentStage.show();
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
            default -> throw new IllegalArgumentException("FXML non riconosciuto: " + fxmlPath);
        };
    }
}