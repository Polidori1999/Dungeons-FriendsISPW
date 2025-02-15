package it.uniroma2.marchidori.maininterface.scenemanager;

import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;

public enum SceneConfigCLIEnum {
    // ========== ESEMPI DI DEFINIZIONE ENUM CON REFLECTION ==========

    CHAR_LIST_DM_CLI(
            "characterList.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.CharacterListDMCLIBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.CharacterListController")
    ),

    CHAR_LIST_PLAYER_CLI(
            CHAR_LIST_DM_CLI.fxmlPath,
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.CharacterListPlayerCLIBoundary"),
            CHAR_LIST_DM_CLI.controllerClass
    ),


    CHAR_SHEET_CLI(
            "characterSheet.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.CharacterSheetCLIBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.CharacterSheetController")
    ),

    CONSULT_RULES_CLI(
            "consultRules.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.ConsultRulesCLIBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.ConsultRulesController")
    ),


    JOIN_LOBBY_DM_CLI(
            "joinLobby.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.JoinLobbyDMCLIBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.JoinLobbyController")
    ),

    JOIN_LOBBY_PLAYER_CLI(
            JOIN_LOBBY_DM_CLI.fxmlPath,
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.JoinLobbyPlayerCLIBoundary"),
            JOIN_LOBBY_DM_CLI.controllerClass
        ),

    LOGIN_CLI(
            "login.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.LoginCLIBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.LoginController")
    ),

    REGISTER_CLI(
            "register.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.RegisterCLIBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.RegisterController")
    ),

    MANAGE_LOBBY_CLI(
            "manageLobby.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.ManageLobbyCLIBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.ManageLobbyController")
    ),

    MANAGE_LOBBY_LIST_DM_CLI(
            "manageLobbyList.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.ManageLobbyListDMCLIBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.ManageLobbyListController")
    ),

    MANAGE_LOBBY_LIST_PLAYER_CLI(
        "manageLobbyList.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.ManageLobbyListPlayerCLIBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.ManageLobbyListController")
    ),

    HOME_CLI(
            "home.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.HomeCLIBoundary"),
            null // se non hai un controller
    ),

    USER_CLI(
            "user.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundarycli.UserCLIBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.UserController")
    );

    // Punto e virgola ^ dopo l'ultima costante.

    private final String fxmlPath;
    private final Class<?> boundaryClass;
    private final Class<?> controllerClass;

    SceneConfigCLIEnum(String fxmlPath, Class<?> boundaryClass, Class<?> controllerClass) {
        this.fxmlPath = fxmlPath;
        this.boundaryClass = boundaryClass;
        this.controllerClass = controllerClass;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public Class<?> getBoundaryClass() {
        return boundaryClass;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    // =====================================
    // HELPER METHOD PER GESTIRE L'EXCEPTION
    // =====================================
    private static Class<?> forNameNoException(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            // Convertiamo in un'eccezione "unchecked" (Runtime)
            throw new SceneChangeException(className);
        }
    }
}
