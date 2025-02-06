package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.Lobby;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;



public class ManageLobbyListController implements UserAwareInterface {
    public UserBean currentUser;
    public User currentEntity = Session.getCurrentUser();

    public ManageLobbyListController() {}




    private LobbyBean entityToBean(Lobby cs) {
        // Infine crea il bean complessivo
        return new LobbyBean(cs.getDuration(),
                cs.getLobbyName(),
                cs.getType(),
                cs.getNumberOfPlayers(),
                cs.isOwned());
    }

    public void deleteLobby(String lobbyName) {
        if (currentUser != null && currentUser.getJoinedLobbies() != null) {
            for (int i = 0; i < currentUser.getJoinedLobbies().size(); i++) {
                if (currentUser.getJoinedLobbies().get(i).getName().equals(lobbyName)) {
                    currentUser.getJoinedLobbies().remove(i);
                    currentEntity.getJoinedLobbies().remove(i);
                    System.out.println(">>> DEBUG: Personaggio eliminato dallo UserBean: " + lobbyName);
                    return;
                }
            }
            System.err.println(">>> ERRORE: Nessun personaggio trovato con il nome: " + lobbyName);
        } else {
            System.err.println(">>> ERRORE: currentUser o lista personaggi NULL in deleteCharacter()");
        }
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
