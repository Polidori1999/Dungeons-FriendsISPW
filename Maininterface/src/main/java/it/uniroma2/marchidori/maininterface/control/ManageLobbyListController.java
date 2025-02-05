package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.CharacterInfo;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.CharacterStats;
import it.uniroma2.marchidori.maininterface.entity.Lobby;

import java.util.ArrayList;
import java.util.List;

public class ManageLobbyListController implements UserAwareInterface {
    private UserBean currentUser;

    public ManageLobbyListController() {}



    public List<LobbyBean> getAllLobbies() {
        List<LobbyBean> beans = new ArrayList<>();
        if (currentUser != null && currentUser.getJoinedLobbies() != null) {
            for (Lobby cs : currentUser.getJoinedLobbies()) {
                beans.add(entityToBean(cs)); // **Converte sempre i dati aggiornati**
            }
        }
        return beans;
    }


    private LobbyBean entityToBean(Lobby cs) {
        // Infine crea il bean complessivo
        return new LobbyBean(cs.getDuration(),
                cs.getLobbyName(),
                cs.getType(),
                cs.getNumberOfPlayers(),
                cs.isOwned());
    }

    /**
     * Converte da Bean "spezzato" a Entity pura.
     */
    private Lobby beanToEntity(LobbyBean bean) {
        return new Lobby(bean.getName(), bean.getDuration(), bean.getType(), bean.isOwned(), bean.getNumberOfPlayers());
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
