package it.uniroma2.marchidori.maininterface.scenemanager;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.JoinLobbyBoundary;
import it.uniroma2.marchidori.maininterface.boundary.ManageLobbyBoundary;
import it.uniroma2.marchidori.maininterface.boundary.UserBoundary;
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
    // Può contenere *qualunque* tipo (non solo chi estende/implementa UserBoundary)
    private static final Map<Pair<RoleEnum, SceneIdEnum>, Class<?>> ROLE_SCENE_MAP = new HashMap<>();

    static {
        // Qui ci metti le tue boundary, DM e Player
        // (DM -> ManageLobbyListDMBoundary, etc.)
        // Non importa se implementano UserBoundary o no, la mappa non lo richiede più.
    }

    private SceneSwitcher() {
        // costruttore privato
    }

    public static void changeScene(Stage currentStage, String fxmlPath, UserBean currentUser) throws IOException {

        SceneIdEnum sceneId = getSceneIdFromFxml(fxmlPath);
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
        // 1) Se implementa un'interfaccia "UserBoundary", passiamo l'utente
        if (controller instanceof UserBoundary userBoundary) {
            userBoundary.setCurrentUser(currentUser);
        }

        // 2) Se implementa "ManageLobbyBoundary", facciamo un init differente
        // (Solo un esempio, se hai un'altra interfaccia per la manage-lobby)
        if (controller instanceof ManageLobbyBoundary mlBoundary) {
            mlBoundary.seCurrentUser(currentUser);
        }

        // 3) Se extends BaseBoundary, puoi fare altre azioni (es. passare uno Stage)
        if (controller instanceof JoinLobbyBoundary jlBoundary) {
            jlBoundary.setCurrentUser(currentUser);
             // Esempio
        }
        // e così via... per ogni "boundary generica" che vuoi gestire

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
            case "user.fxml"            -> SceneIdEnum.USER;
            default -> throw new IllegalArgumentException("FXML non riconosciuto: " + fxmlPath);
        };
    }
}
