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


public class Converter {

    private Converter(){
        // empty
    }


    //converte user bean in user entity
    public static User userBeanToEntity(UserBean userBean) {
        if (userBean == null) {
            return null;
        }


        String email = userBean.getEmail();
        String password = userBean.getPassword();
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


    //Converte un CharacterSheetBean in un CharacterSheetEntity.
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


    //Converte un CharacterInfoBean in un CharacterInfo.
    public static CharacterInfo characterInfoBeanToEntity(CharacterInfoBean infoBean) {
        if (infoBean == null) {
            return null;
        }
        return new CharacterInfo(
                infoBean.getName(),
                infoBean.getRace(),
                infoBean.getAge(),
                infoBean.getClasse(),
                infoBean.getLevel()
        );
    }


    //Converte un CharacterStatsBean in un CharacterStats.
    public static CharacterStats characterStatsBeanToEntity(CharacterStatsBean statsBean) {
        if (statsBean == null) {
            return null;
        }
        // Usa il costruttore di CharacterStats
        return new CharacterStats(
                statsBean.getStrength(),
                statsBean.getDexterity(),
                statsBean.getIntelligence(),
                statsBean.getWisdom(),
                statsBean.getCharisma(),
                statsBean.getConstitution()
        );
    }


    //Converte un LobbyBean in un Lobby
    public static Lobby lobbyBeanToEntity(LobbyBean lobbyBean) {
        if (lobbyBean == null) {
            return null;
        }
        return new Lobby(
                lobbyBean.getName(),
                lobbyBean.getDuration(),
                lobbyBean.getLiveOnline(),
                lobbyBean.getMaxOfPlayers(),
                lobbyBean.getOwner(),
                lobbyBean.getInfoLink(),
                lobbyBean.getJoinedPlayersCount()
        );
    }


    //Converte una lista che può contenere LobbyBean in una lista di Lobby.
    public static List<Lobby> convertLobbyList(List<LobbyBean> inputList) {
        List<Lobby> result = new ArrayList<>();
        if (inputList == null) {
            return result;
        }
        //per ogni bean esegue conversione
        for (LobbyBean bean : inputList) {
            result.add(lobbyBeanToEntity(bean));
        }
        return result;
    }



    //converte user in user bean
    public static UserBean convert(User user) {
        if (user == null) {
            return null;
        }

        // Converte le liste di Lobby e CharacterSheet
        List<LobbyBean> favouriteLobbies = convertLobbyListEntityToBean(user.getFavouriteLobbies());
        List<LobbyBean> joinedLobbies    = convertLobbyListEntityToBean(user.getJoinedLobbies());
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

    //converte lobby entity in lobby bean
    public static LobbyBean lobbyEntityToBean(Lobby lobby) {
        if (lobby == null) {
            return null;
        }
        return new LobbyBean(
                lobby.getLobbyName(),
                lobby.getDuration(),
                lobby.getLiveOnline(),
                lobby.getMaxOfPlayers(),
                lobby.getOwner(),
                lobby.getInfoLink(),
                lobby.getJoinedPlayersCount()
        );
    }



    //converte lista di lobby in lista di lobbybean
    public static List<LobbyBean> convertLobbyListEntityToBean(List<Lobby> lobbies) {
        if (lobbies == null) {
            return new ArrayList<>();
        }
        List<LobbyBean> result = new ArrayList<>();
        for (Lobby lobbyBean : lobbies) {
            result.add(lobbyEntityToBean(lobbyBean));
        }
        return result;
    }


    //converte charactersheet entity in charactersheet bean
    public static CharacterSheetBean characterSheetEntityToBean(CharacterSheet sheet) {
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


    //converte una lista di CharacterSheet in una lista di CharacterSheetBean.
    public static List<CharacterSheetBean> convertCharacterSheetList(List<CharacterSheet> sheets) {
        if (sheets == null) {
            return new ArrayList<>();
        }
        ArrayList<CharacterSheetBean> result = new ArrayList<>();
        for(CharacterSheet cs : sheets){
            result.add(characterSheetEntityToBean(cs));
        }
        return result;
    }
}