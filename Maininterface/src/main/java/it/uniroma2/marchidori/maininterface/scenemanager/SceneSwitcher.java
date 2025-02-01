package it.uniroma2.marchidori.maininterface.scenemanager;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.*;
import it.uniroma2.marchidori.maininterface.boundary.characterSheet.CharacterListDMBoundary;
import it.uniroma2.marchidori.maininterface.boundary.characterSheet.CharacterListPlayerBoundary;
import it.uniroma2.marchidori.maininterface.boundary.characterSheet.CharacterSheetBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinLobby.JoinLobbyBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinLobby.JoinLobbyDMBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinLobby.JoinLobbyPlayerBoundary;
import it.uniroma2.marchidori.maininterface.boundary.login.LoginBoundary;
import it.uniroma2.marchidori.maininterface.boundary.login.RegisterBoundary;
import it.uniroma2.marchidori.maininterface.boundary.manageLobby.ManageLobbyBoundary;
import it.uniroma2.marchidori.maininterface.boundary.manageLobby.ManageLobbyListDMBoundary;
import it.uniroma2.marchidori.maininterface.boundary.manageLobby.ManageLobbyListPlayerBoundary;
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
    }

    private SceneSwitcher() {
        // costruttore privato
    }

    public static void changeScene(Stage currentStage, String fxmlPath, UserBean currentUser) throws IOException {

        SceneIdEnum sceneId = getSceneIdFromFxml(fxmlPath);
        // Se currentUser è null oppure se la scena non richiede un ruolo, utilizziamo RoleEnum.NONE
        RoleEnum role = currentUser.getRoleBehavior();

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
        // Se currentUser non è null, possiamo effettuare i runtime checks
        if (currentUser != null) {
            // 1) Se implementa un'interfaccia "UserBoundary", passiamo l'utente
            if (controller instanceof UserBoundary userBoundary) {
                userBoundary.setCurrentUser(currentUser);
            }

            // 2) Se implementa "ManageLobbyBoundary", facciamo un init differente
            if (controller instanceof ManageLobbyBoundary mlBoundary) {
                mlBoundary.seCurrentUser(currentUser);
            }

            // 3) Se implementa "JoinLobbyBoundary", facciamo altre operazioni
            if (controller instanceof JoinLobbyBoundary jlBoundary) {
                jlBoundary.setCurrentUser(currentUser);
                // Esempio
            }
        }
        // Altrimenti (se currentUser è null) non vengono effettuati runtime check relativi all'utente

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
            case "login.fxml"        -> SceneIdEnum.LOGIN;
            default -> throw new IllegalArgumentException("FXML non riconosciuto: " + fxmlPath);
        };
    }
}
