package it.uniroma2.marchidori.maininterface.scenemanager;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.enumerate.SceneIdEnum;
import it.uniroma2.marchidori.maininterface.factory.BoundaryFactory;
import it.uniroma2.marchidori.maininterface.factory.ControllerFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class SceneSwitcher {
    public static final Logger logger = Logger.getLogger(SceneSwitcher.class.getName());

    private SceneSwitcher() {}

    public static void changeScene(Stage currentStage, String fxmlPath, UserBean currentUser) throws IOException {
        RoleEnum role = (currentUser != null) ? currentUser.getRoleBehavior() : RoleEnum.NONE;
        SceneIdEnum sceneId = getSceneIdFromFxml(fxmlPath);

        if (Session.getInstance().getCLI()) {
            SceneConfigCLIEnum config = RoleSceneMap.getCLIConfig(role, sceneId);
            Object boundaryInstance = createAndInjectBoundary(config, currentUser);
            logger.info(">>> [SceneSwitcher] Boundary risolta: " + config.getBoundaryClass().getSimpleName());
            logger.info(">>> [SceneSwitcher] Istanza della boundary creata: " + boundaryInstance.getClass().getSimpleName());

            if (boundaryInstance instanceof RunInterface runInterface) {
                runInterface.run();
            }
            return;
        }

        // Evitare scene duplicate
        if (!Session.getInstance().getCLI() && currentStage.getScene() != null &&
                fxmlPath.equals(currentStage.getScene().getUserData())) {
            return; // Se la scena è già attiva, non fare nulla
        }

        SceneConfigEnum config = RoleSceneMap.getConfig(role, sceneId);
        Object boundaryInstance = createAndInjectBoundary(config, currentUser);
        logger.info(">>> [SceneSwitcher] Boundary risolta: " + config.getBoundaryClass().getSimpleName());
        logger.info(">>> [SceneSwitcher] Istanza della boundary creata: " + boundaryInstance.getClass().getSimpleName());

        Parent root = loadFXML(fxmlPath, boundaryInstance);
        Scene newScene = new Scene(root);
        currentStage.setScene(newScene);
        currentStage.show();
    }

    /**
     * Metodo helper che crea la boundary e, se previsto, il relativo controller,
     * iniettando il currentUser in entrambi.
     *
     * Il parametro 'config' può essere un'istanza di SceneConfigCLIEnum o SceneConfigEnum,
     * purché entrambi offrano i metodi getBoundaryClass() e getControllerClass().
     */
    private static Object createAndInjectBoundary(Object config, UserBean currentUser) {
        Class<?> boundaryClass;
        Class<?> controllerClass;

        // Determina le classi in base al tipo di config passato
        if (config instanceof SceneConfigCLIEnum cliConfig) {
            boundaryClass = cliConfig.getBoundaryClass();
            controllerClass = cliConfig.getControllerClass();
        } else if (config instanceof SceneConfigEnum guiConfig) {
            boundaryClass = guiConfig.getBoundaryClass();
            controllerClass = guiConfig.getControllerClass();
        } else {
            throw new IllegalArgumentException("Tipo di config non supportato: " + config.getClass());
        }

        Object boundaryInstance = BoundaryFactory.createBoundary(boundaryClass);
        injectCurrentUser(boundaryInstance, Converter.convert(Session.getInstance().getCurrentUser()));

        if (controllerClass != null) {
            Object controllerInstance = ControllerFactory.createController(controllerClass);
            injectCurrentUser(controllerInstance, Converter.convert(Session.getInstance().getCurrentUser()));
            injectControllerIntoBoundary(controllerInstance, boundaryInstance);
        }

        return boundaryInstance;
    }


    private static void injectControllerIntoBoundary(Object controller, Object boundary) {
        if (controller != null && boundary instanceof it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface controllerAware) {
            controllerAware.setLogicController(controller);
        } else {
            logger.info(">>> [SceneSwitcher] Nessun controller da iniettare in " + boundary.getClass().getSimpleName());
        }
    }

    private static void injectCurrentUser(Object target, UserBean currentUser) {
        if (currentUser != null && target instanceof it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface userAware) {
            userAware.setCurrentUser(currentUser);
        } else {

            logger.info(">>> [SceneSwitcher] Nessun currentUser da iniettare in " + target.getClass().getSimpleName());
        }
    }

    private static Parent loadFXML(String fxmlPath, Object controller) throws IOException {
        String fullPath = "/it/uniroma2/marchidori/maininterface/" + fxmlPath;
        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fullPath));
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
