package it.uniroma2.marchidori.maininterface.boundary.map;

import it.uniroma2.marchidori.maininterface.boundary.HomeBoundary;
import it.uniroma2.marchidori.maininterface.boundary.user.UserBoundary;
import it.uniroma2.marchidori.maininterface.boundary.user.UserGuestBoundary;
import it.uniroma2.marchidori.maininterface.boundary.consultrules.ConsultRulesBoundary;
import it.uniroma2.marchidori.maininterface.boundary.consultrules.ConsultRulesGuestBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyGuestBoundary;
import it.uniroma2.marchidori.maininterface.boundary.joinlobby.JoinLobbyPlayerBoundary;
import it.uniroma2.marchidori.maininterface.boundary.login.LoginBoundary;
import it.uniroma2.marchidori.maininterface.boundary.login.RegisterBoundary;
import it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyBoundary;
import it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyListDMBoundary;
import it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyListGuestBoundary;
import it.uniroma2.marchidori.maininterface.boundary.managelobby.ManageLobbyListPlayerBoundary;
import it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterListDMBoundary;
import it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterListPlayerBoundary;
import it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterListGuestBoundary;
import it.uniroma2.marchidori.maininterface.boundary.charactersheet.CharacterSheetBoundary;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.enumerate.SceneIdEnum;
import it.uniroma2.marchidori.maininterface.utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class BoundaryMap {

    private BoundaryMap(){
        //empty builder
    }

    protected static final Map<Pair<RoleEnum, SceneIdEnum>, Class<?>> BOUNDARY_MAP = new HashMap<>();

    static {
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListDMBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListPlayerBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.MANAGE_LOBBY_LIST), ManageLobbyListGuestBoundary.class);

        BOUNDARY_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.JOIN_LOBBY), JoinLobbyBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.JOIN_LOBBY), JoinLobbyPlayerBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.JOIN_LOBBY), JoinLobbyGuestBoundary.class);

        BOUNDARY_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_LIST), CharacterListDMBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_LIST), CharacterListPlayerBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CHARACTER_LIST), CharacterListGuestBoundary.class);

        BOUNDARY_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.USER), UserBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.USER), UserBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.USER), UserGuestBoundary.class);

        BOUNDARY_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CHARACTER_SHEET), CharacterSheetBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CHARACTER_SHEET), CharacterSheetBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CHARACTER_SHEET), CharacterSheetBoundary.class);

        BOUNDARY_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.HOME), HomeBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.HOME), HomeBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.HOME), HomeBoundary.class);

        BOUNDARY_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.LOGIN), LoginBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.LOGIN), LoginBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.NONE, SceneIdEnum.LOGIN), LoginBoundary.class);

        BOUNDARY_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.REGISTER), RegisterBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.REGISTER), RegisterBoundary.class);

        BOUNDARY_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.MANAGE_LOBBY), ManageLobbyBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.MANAGE_LOBBY), ManageLobbyBoundary.class);

        BOUNDARY_MAP.put(new Pair<>(RoleEnum.PLAYER, SceneIdEnum.CONSULT_RULES), ConsultRulesBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.DM, SceneIdEnum.CONSULT_RULES), ConsultRulesBoundary.class);
        BOUNDARY_MAP.put(new Pair<>(RoleEnum.GUEST, SceneIdEnum.CONSULT_RULES), ConsultRulesGuestBoundary.class);
    }

    public static Class<?> getBoundaryClass(RoleEnum role, SceneIdEnum sceneId) {
        Class<?> boundaryClass = BOUNDARY_MAP.get(new Pair<>(role, sceneId));
        if (boundaryClass == null) {
            throw new IllegalArgumentException("Nessuna boundary definita per ruolo = "
                    + role + " e scena = " + sceneId);
        }
        return boundaryClass;
    }
}
