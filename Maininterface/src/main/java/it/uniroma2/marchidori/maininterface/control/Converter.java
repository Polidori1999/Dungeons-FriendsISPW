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
import java.util.stream.Collectors;

/**
 * Classe che fornisce i metodi di conversione da Bean a Entity.
 */
public class Converter {

    private Converter(){
        // empty
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
        String password = userBean.getPassword();  // Assicurati che la password venga passata
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

        // Se il ruolo è presente, usa il costruttore con il ruolo
        if (role != null) {
            return new User(email, role, characterSheets, favouriteLobbies, joinedLobbies);
        } else {
            // Se non c'è un ruolo, usa il costruttore di default (ruolo PLAYER)
            return new User(email, password, characterSheets, favouriteLobbies, joinedLobbies);
        }
    }

    /**
     * Converte un CharacterSheetBean in un CharacterSheetEntity.
     */
    public static CharacterSheet characterSheetBeanToEntity(CharacterSheetBean csBean) {
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
    public static CharacterInfo characterInfoBeanToEntity(CharacterInfoBean infoBean) {
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
    public static CharacterStats characterStatsBeanToEntity(CharacterStatsBean statsBean) {
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
    public static Lobby lobbyBeanToEntity(LobbyBean lobbyBean) {
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
                lobbyBean.getNumberOfPlayers(),
                lobbyBean.getInfoLink()
        );
    }

    /**
     * Converte una lista che può contenere LobbyBean o Lobby (già convertite) in una lista di Lobby.
     */
    public static List<Lobby> convertLobbyList(List<LobbyBean> inputList) {
        List<Lobby> result = new ArrayList<>();
        if (inputList == null) {
            return result;
        }
        for (LobbyBean bean : inputList) {
            result.add(lobbyBeanToEntity(bean));
        }
        return result;
    }

    public static Lobby beanToEntity(LobbyBean bean) {
        return new Lobby(bean.getName(), bean.getDuration(), bean.getLiveOnline(), bean.isOwned(), bean.getNumberOfPlayers(), bean.getInfoLink());
    }

    public static Lobby stringToLobby(String lobbyData) {
        if (lobbyData == null || lobbyData.isEmpty()) {
            return null;
        }

        String[] dataParts = lobbyData.split(";");
        String lobbyName = dataParts.length > 0 ? dataParts[0] : "";
        String duration = dataParts.length > 1 ? dataParts[1] : "";
        String type = dataParts.length > 2 ? dataParts[2] : "";
        boolean owned = dataParts.length > 3 && Boolean.parseBoolean(dataParts[3]);
        int numberOfPlayers = dataParts.length > 4 ? Integer.parseInt(dataParts[4]) : 0;
        String infoLink = dataParts.length > 5 ? dataParts[5] : "";
        return new Lobby(lobbyName, duration, type, owned, numberOfPlayers, infoLink);
    }
    //////////////////////////////////////////////

    public static UserBean convert(User user) {
        if (user == null) {
            return null;
        }

        // Converte le liste di Lobby e CharacterSheet
        List<LobbyBean> favouriteLobbies = convertLobbyList2(user.getFavouriteLobbies());
        List<LobbyBean> joinedLobbies    = convertLobbyList2(user.getJoinedLobbies());
        List<CharacterSheetBean> sheetBeans = convertCharacterSheetList(user.getCharacterSheets());

        // Crea il UserBean
        return new UserBean(
                user.getEmail(),
                user.getPassword(),
                user.getRoleBehavior(),
                joinedLobbies,
                favouriteLobbies,
                sheetBeans
        );
    }

    /**
     * =====================
     *     METODI LOBBY
     * =====================
     */

    /**
     * Converte un singolo oggetto Lobby (entity) in un oggetto LobbyBean (bean).
     */
    public static LobbyBean convertLobby(Lobby lobby) {
        if (lobby == null) {
            return null;
        }
        // Usa il costruttore di LobbyBean che accetta un Lobby
        return new LobbyBean(lobby);
    }

    /**
     * Converte una lista di Lobby in una lista di LobbyBean.
     * Qui usiamo new ArrayList<>(...) per rendere la lista mutabile.
     */
    public static List<LobbyBean> convertLobbyList2(List<Lobby> lobbies) {
        if (lobbies == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(lobbies.stream()
                .map(Converter::convertLobby)
                .collect(Collectors.toList()));
    }

    /*
     * =====================
     *  METODI CHAR-SHEET
     * =====================
     */

    /**
     * Converte un singolo CharacterSheet (entity) in CharacterSheetBean (bean).
     */
    public static CharacterSheetBean convertCharacterSheet(CharacterSheet sheet) {
        if (sheet == null) {
            return null;
        }

        // Converte la parte "info"
        CharacterInfo info = sheet.getCharacterInfo();
        CharacterInfoBean infoBean = null;
        if (info != null) {
            infoBean = new CharacterInfoBean(
                    info.getName(),
                    info.getRace(),
                    info.getAge(),
                    info.getClasse(),
                    info.getLevel()
            );
        }

        // Converte la parte "stats"
        CharacterStats stats = sheet.getCharacterStats();
        CharacterStatsBean statsBean = null;
        if (stats != null) {
            statsBean = new CharacterStatsBean(
                    stats.getStrength(),
                    stats.getDexterity(),
                    stats.getIntelligence(),
                    stats.getWisdom(),
                    stats.getCharisma(),
                    stats.getConstitution()
            );
        }

        // Crea il CharacterSheetBean
        return new CharacterSheetBean(infoBean, statsBean);
    }

    /**
     * Converte una lista di CharacterSheet in una lista di CharacterSheetBean.
     * Anche qui restituiamo una nuova ArrayList per garantire la mutabilità.
     */
    public static List<CharacterSheetBean> convertCharacterSheetList(List<CharacterSheet> sheets) {
        if (sheets == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(sheets.stream()
                .map(Converter::convertCharacterSheet)
                .collect(Collectors.toList()));
    }
}
