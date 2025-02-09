package it.uniroma2.marchidori.maininterface.enumerate;

public enum SceneConfigEnum {




    CHAR_LIST_DM(
            "characterList.fxml",
            it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterListDMBoundary.class,
            it.uniroma2.marchidori.maininterface.control.CharacterListController.class
    ),
    CHAR_LIST_PLAYER(
            CHAR_LIST_DM.fxmlPath,
            it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterListPlayerBoundary.class,
            it.uniroma2.marchidori.maininterface.control.CharacterListController.class
    ),
    CHAR_LIST_GUEST(
            CHAR_LIST_DM.fxmlPath,
            it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterListGuestBoundary.class,
            it.uniroma2.marchidori.maininterface.control.CharacterListController.class
    ),
    CHAR_SHEET(
            "characterSheet.fxml",
            it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterSheetBoundary.class,
            it.uniroma2.marchidori.maininterface.control.CharacterSheetController.class
    ),
    CONSULT_RULES(
            "consultRules.fxml",
            it.uniroma2.marchidori.maininterface.boundary.consultrules.ConsultRulesBoundary.class,
            it.uniroma2.marchidori.maininterface.control.ConsultRulesController.class
    ),
    CONSULT_RULES_GUEST(
            "consultRules.fxml",
            it.uniroma2.marchidori.maininterface.boundary.consultrules.ConsultRulesGuestBoundary.class,
            it.uniroma2.marchidori.maininterface.control.ConsultRulesController.class
    ),
    JOIN_LOBBY_DM(
            "joinLobby.fxml",
            it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyBoundary.class,
            it.uniroma2.marchidori.maininterface.control.JoinLobbyController.class
    ),
    JOIN_LOBBY_PLAYER(
            JOIN_LOBBY_DM.fxmlPath,
            it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyPlayerBoundary.class,
            it.uniroma2.marchidori.maininterface.control.JoinLobbyController.class
    ),

    JOIN_LOBBY_GUEST(
            JOIN_LOBBY_DM.fxmlPath,
            it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyGuestBoundary.class,
            it.uniroma2.marchidori.maininterface.control.JoinLobbyController.class
    ),
    LOGIN(
            "login.fxml",
            it.uniroma2.marchidori.maininterface.boundary.login.LoginBoundary.class,
            it.uniroma2.marchidori.maininterface.control.LoginController.class
    ),
    REGISTER(
            "register.fxml",
            it.uniroma2.marchidori.maininterface.boundary.login.RegisterBoundary.class,
            it.uniroma2.marchidori.maininterface.control.RegisterController.class
    ),
    MANAGE_LOBBY(
            "manageLobby.fxml",
            it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyBoundary.class,
            it.uniroma2.marchidori.maininterface.control.ManageLobbyController.class
    ),
    MANAGE_LOBBY_LIST_DM(
            "manageLobbyList.fxml",
            it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyListDMBoundary.class,
            it.uniroma2.marchidori.maininterface.control.ManageLobbyListController.class
    ),
    MANAGE_LOBBY_LIST_PLAYER(
            MANAGE_LOBBY_LIST_DM.fxmlPath,
            it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyListPlayerBoundary.class,
            it.uniroma2.marchidori.maininterface.control.ManageLobbyListController.class
    ),
    MANAGE_LOBBY_LIST_GUEST(
            MANAGE_LOBBY_LIST_DM.fxmlPath,
            it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyListGuestBoundary.class,
            it.uniroma2.marchidori.maininterface.control.ManageLobbyListController.class
    ),
    HOME(
            "home.fxml",
            it.uniroma2.marchidori.maininterface.boundary.HomeBoundary.class,
            null
    ),
    USER(
            "user.fxml",
            it.uniroma2.marchidori.maininterface.boundary.user.UserBoundary.class,
            it.uniroma2.marchidori.maininterface.control.UserController.class
    ),
    USER_GUEST(
            "user.fxml",
            it.uniroma2.marchidori.maininterface.boundary.user.UserGuestBoundary.class,
            it.uniroma2.marchidori.maininterface.control.UserController.class
    ),
    ;

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
}

