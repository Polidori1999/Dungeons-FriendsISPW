package it.uniroma2.marchidori.maininterface.scenemanager;

import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;

public enum SceneConfigEnum {

    // ========== ESEMPI DI DEFINIZIONE ENUM CON REFLECTION ==========

    CHAR_LIST_DM(
            "characterList.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterListDMBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.CharacterListController")
    ),

    CHAR_LIST_PLAYER(
            CHAR_LIST_DM.fxmlPath,
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterListPlayerBoundary"),
            CHAR_LIST_DM.controllerClass    ),

    CHAR_LIST_GUEST(
            CHAR_LIST_DM.fxmlPath,
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterListGuestBoundary"),
            CHAR_LIST_DM.controllerClass    ),

    CHAR_SHEET(
            "characterSheet.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterSheetBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.CharacterSheetController")
    ),

    CONSULT_RULES(
            "consultRules.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.consultrules.ConsultRulesBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.ConsultRulesController")
    ),

    CONSULT_RULES_GUEST(
            "consultRules.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.consultrules.ConsultRulesGuestBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.ConsultRulesController")
    ),

    JOIN_LOBBY_DM(
            "joinLobby.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.JoinLobbyController")
    ),

    JOIN_LOBBY_PLAYER(
            JOIN_LOBBY_DM.fxmlPath,
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyPlayerBoundary"),
            JOIN_LOBBY_DM.controllerClass    ),

    JOIN_LOBBY_GUEST(
            JOIN_LOBBY_DM.fxmlPath,
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyGuestBoundary"),
            JOIN_LOBBY_DM.controllerClass    ),

    LOGIN(
            "login.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.login.LoginBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.LoginController")
    ),

    REGISTER(
            "register.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.login.RegisterBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.RegisterController")
    ),

    MANAGE_LOBBY(
            "manageLobby.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.ManageLobbyController")
    ),

    MANAGE_LOBBY_LIST_DM(
            "manageLobbyList.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyListDMBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.ManageLobbyListController")
    ),

    MANAGE_LOBBY_LIST_PLAYER(
            MANAGE_LOBBY_LIST_DM.fxmlPath,
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyListPlayerBoundary"),
            MANAGE_LOBBY_LIST_DM.controllerClass    ),

    MANAGE_LOBBY_LIST_GUEST(
            MANAGE_LOBBY_LIST_DM.fxmlPath,
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyListGuestBoundary"),
            MANAGE_LOBBY_LIST_DM.controllerClass
    ),

    HOME(
            "home.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.HomeBoundary"),
            null // se non hai un controller
    ),

    USER(
            "user.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.user.UserBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.UserController")
    ),

    USER_GUEST(
            "user.fxml",
            forNameNoException("it.uniroma2.marchidori.maininterface.boundary.user.UserGuestBoundary"),
            forNameNoException("it.uniroma2.marchidori.maininterface.control.UserController")
    );

    // Punto e virgola ^ dopo l'ultima costante.

    private final String fxmlPath;
    private final Class<?> boundaryClass;
    private final Class<?> controllerClass;

    SceneConfigEnum(String fxmlPath, Class<?> boundaryClass, Class<?> controllerClass) {
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
