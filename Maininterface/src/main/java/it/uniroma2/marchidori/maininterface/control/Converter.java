package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;

import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.CharacterInfo;
import it.uniroma2.marchidori.maininterface.entity.CharacterStats;

import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;

import java.util.ArrayList;
import java.util.List;


/**
 * Classe che fornisce i metodi di conversione da Bean a Entity.
 */
public class Converter {

    private Converter(){
        //empty
    }

    /**
     * Metodo principale: converte un oggetto UserBean in un oggetto User.
     *
     * @param userBean l'oggetto bean da convertire
     * @return l'oggetto entity User corrispondente oppure null se userBean è null
     */
    public static User userBeanToEntity(UserBean userBean) {
        if (userBean == null) {
            return null;
        }

        // Campi semplici
        String email = userBean.getEmail();
        String password=userBean.getPassword();
        RoleEnum role = userBean.getRoleBehavior();

        // Conversione CharacterSheetBean -> CharacterSheet
        List<CharacterSheet> characterSheets = new ArrayList<>();
        if (userBean.getCharacterSheets() != null) {
            for (CharacterSheetBean csBean : userBean.getCharacterSheets()) {
                CharacterSheet characterSheet = characterSheetBeanToEntity(csBean);
                if (characterSheet != null) {
                    characterSheets.add(characterSheet);
                }
            }
        }

        // Conversione delle liste di lobby
        List<Lobby> favouriteLobbies = convertLobbyList(userBean.getFavouriteLobbies());
        List<Lobby> joinedLobbies = convertLobbyList(userBean.getJoinedLobbies());

        // Creazione dell'User
        if (role != null) {
            // Se è presente un ruolo specifico, usiamo il costruttore con role.
            return new User(email, password ,role, characterSheets, favouriteLobbies, joinedLobbies);
        } else {
            // Altrimenti usiamo il costruttore di default (che setta il role a PLAYER di default).
            return new User(email, characterSheets, favouriteLobbies, joinedLobbies);
        }
    }

    /**
     * Converte un CharacterSheetBean in un CharacterSheet.
     */
    private static CharacterSheet characterSheetBeanToEntity(CharacterSheetBean csBean) {
        if (csBean == null) {
            return null;
        }
        // Converte la parte "info"
        CharacterInfo characterInfo = characterInfoBeanToEntity(csBean.getInfoBean());
        // Converte la parte "stats"
        CharacterStats characterStats = characterStatsBeanToEntity(csBean.getStatsBean());
        return new CharacterSheet(characterInfo, characterStats);
    }

    /**
     * Converte un CharacterInfoBean in un CharacterInfo.
     */
    private static CharacterInfo characterInfoBeanToEntity(CharacterInfoBean infoBean) {
        if (infoBean == null) {
            return null;
        }
        // Usa il costruttore di CharacterInfo
        // (String name, String race, int age, String classe, int level)
        return new CharacterInfo(
                infoBean.getName(),
                infoBean.getRace(),
                infoBean.getAge(),
                infoBean.getClasse(),
                infoBean.getLevel()
        );
    }

    /**
     * Converte un CharacterStatsBean in un CharacterStats.
     */
    private static CharacterStats characterStatsBeanToEntity(CharacterStatsBean statsBean) {
        if (statsBean == null) {
            return null;
        }
        // Usa il costruttore di CharacterStats
        // (int strength, int dexterity, int intelligence, int wisdom, int charisma, int constitution)
        return new CharacterStats(
                statsBean.getStrength(),
                statsBean.getDexterity(),
                statsBean.getIntelligence(),
                statsBean.getWisdom(),
                statsBean.getCharisma(),
                statsBean.getConstitution()
        );
    }

    /**
     * Converte un LobbyBean in un Lobby.
     */
    private static Lobby lobbyBeanToEntity(LobbyBean lobbyBean) {
        if (lobbyBean == null) {
            return null;
        }
        // Usa il costruttore di Lobby
        // (String lobbyName, String duration, String type, boolean owned, int numberOfPlayers)
        return new Lobby(
                lobbyBean.getName(),
                lobbyBean.getDuration(),
                lobbyBean.getLiveOnline(),
                lobbyBean.isOwned(),
                lobbyBean.getNumberOfPlayers()
        );
    }

    /**
     * Converte una lista che può contenere LobbyBean o Lobby (già convertite) in una lista di Lobby.
     */
    private static List<Lobby> convertLobbyList(List<LobbyBean> inputList) {
        List<Lobby> result = new ArrayList<>();
        if (inputList == null) {
            return result;
        }
        for (LobbyBean bean : inputList) {
            result.add(lobbyBeanToEntity(bean));
        }
        return result;
    }

    public Lobby beanToEntity(LobbyBean bean) {
        return new Lobby(bean.getName(), bean.getDuration(), bean.getLiveOnline(), bean.isOwned(), bean.getNumberOfPlayers());
    }
}
