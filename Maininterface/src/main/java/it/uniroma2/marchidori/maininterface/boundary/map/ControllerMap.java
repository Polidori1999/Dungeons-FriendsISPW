package it.uniroma2.marchidori.maininterface.boundary.map;

import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import it.uniroma2.marchidori.maininterface.control.CharacterListController;
import it.uniroma2.marchidori.maininterface.control.ConsultRulesController;
import it.uniroma2.marchidori.maininterface.control.JoinLobbyController;
import it.uniroma2.marchidori.maininterface.control.LoginController;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyController;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyListController;
import it.uniroma2.marchidori.maininterface.control.RegisterController;
import it.uniroma2.marchidori.maininterface.control.UserController;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.enumerate.SceneIdEnum;
import it.uniroma2.marchidori.maininterface.utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class ControllerMap {

    private ControllerMap(){
        //EMPTY BUILDER
    }

    protected static final Map<Pair<RoleEnum, SceneIdEnum>, Class<?>> CONTROLLER_MAP = new HashMap<>();

    static {
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListController.class);

        CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.JOIN_LOBBY), JoinLobbyController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.JOIN_LOBBY), JoinLobbyController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.JOIN_LOBBY), JoinLobbyController.class);

        CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CHARACTER_LIST), CharacterListController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_LIST), CharacterListController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_LIST), CharacterListController.class);

        CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.USER), UserController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.USER), UserController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.USER), UserController.class);

        CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_SHEET), CharacterSheetController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_SHEET), CharacterSheetController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CHARACTER_SHEET), CharacterSheetController.class);

        CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.LOGIN), LoginController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.LOGIN), LoginController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.NONE, SceneIdEnum.LOGIN), LoginController.class);

        CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.REGISTER), RegisterController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.REGISTER), RegisterController.class);

        CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY), ManageLobbyController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY), ManageLobbyController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.MANAGE_LOBBY), ManageLobbyController.class);

        CONTROLLER_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CONSULT_RULES), ConsultRulesController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CONSULT_RULES), ConsultRulesController.class);
        CONTROLLER_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CONSULT_RULES), ConsultRulesController.class);
    }

    public static Class<?> getControllerClass(RoleEnum role, SceneIdEnum sceneId) {
        if (sceneId == SceneIdEnum.HOME) {
            return null; // In questo caso non è previsto un controller per la home
        }
        Class<?> controllerClass = CONTROLLER_MAP.get(new Pair<>(role, sceneId));
        if (controllerClass == null) {
            throw new IllegalArgumentException("Nessun controller definito per ruolo = "
                    + role + " e scena = " + sceneId);
        }
        return controllerClass;
    }
}
