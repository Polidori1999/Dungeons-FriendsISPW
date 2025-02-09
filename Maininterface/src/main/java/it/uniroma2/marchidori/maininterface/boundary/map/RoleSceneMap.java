package it.uniroma2.marchidori.maininterface.boundary.map;

import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.enumerate.SceneConfigEnum;
import it.uniroma2.marchidori.maininterface.enumerate.SceneIdEnum;
import it.uniroma2.marchidori.maininterface.utils.Pair;

import java.util.HashMap;
import java.util.Map;

import static it.uniroma2.marchidori.maininterface.enumerate.SceneConfigEnum.*;

public class RoleSceneMap {

    private RoleSceneMap(){
        //empty builder
    }

    private static final Map<Pair<RoleEnum, SceneIdEnum>, SceneConfigEnum> MAPPING = new HashMap<>();
    static {
        MAPPING.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY_LIST), MANAGE_LOBBY_LIST_DM);
        MAPPING.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY_LIST), MANAGE_LOBBY_LIST_PLAYER);
        MAPPING.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.MANAGE_LOBBY_LIST), MANAGE_LOBBY_LIST_GUEST);

        MAPPING.put(new Pair<>(RoleEnum.DM, SceneIdEnum.JOIN_LOBBY), JOIN_LOBBY_DM);
        MAPPING.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.JOIN_LOBBY), JOIN_LOBBY_PLAYER);
        MAPPING.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.JOIN_LOBBY), JOIN_LOBBY_GUEST);

        MAPPING.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_LIST), CHAR_LIST_DM);
        MAPPING.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_LIST), CHAR_LIST_PLAYER);
        MAPPING.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CHARACTER_LIST), CHAR_LIST_GUEST);

        MAPPING.put(new Pair<>(RoleEnum.DM, SceneIdEnum.USER), USER);
        MAPPING.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.USER), USER);
        MAPPING.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.USER), USER_GUEST);

        MAPPING.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_SHEET), CHAR_SHEET);
        MAPPING.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_SHEET), CHAR_SHEET);
        MAPPING.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CHARACTER_SHEET), CHAR_SHEET);

        MAPPING.put(new Pair<>(RoleEnum.DM, SceneIdEnum.HOME), HOME);
        MAPPING.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.HOME), HOME);
        MAPPING.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.HOME), HOME);

        MAPPING.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.LOGIN), LOGIN);
        MAPPING.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.LOGIN), LOGIN);
        MAPPING.put(new Pair<>(RoleEnum.NONE, SceneIdEnum.LOGIN), LOGIN);

        MAPPING.put(new Pair<>(RoleEnum.DM, SceneIdEnum.REGISTER), REGISTER);
        MAPPING.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.REGISTER), REGISTER);

        MAPPING.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY), MANAGE_LOBBY);
        MAPPING.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY), MANAGE_LOBBY);

        MAPPING.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CONSULT_RULES), CONSULT_RULES);
        MAPPING.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CONSULT_RULES), CONSULT_RULES);
        MAPPING.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CONSULT_RULES), CONSULT_RULES_GUEST);
    }

    public static SceneConfigEnum getConfig(RoleEnum role, SceneIdEnum sceneId) {
        SceneConfigEnum config = MAPPING.get(new Pair<>(role, sceneId));
        if (config == null) {
            throw new IllegalArgumentException("Nessuna configurazione trovata per: "
                    + role + " / " + sceneId);
        }
        return config;
    }
}