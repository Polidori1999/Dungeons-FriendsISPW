package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;

import it.uniroma2.marchidori.maininterface.entity.Lobby;

public class ManageLobbyController implements UserAwareInterface {
    private UserBean currentUser;

    public ManageLobbyController(){}

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }


    public ManageLobbyController(UserBean currentUser) {
        if (currentUser == null) {
            throw new IllegalArgumentException("UserBean passato a CharacterSheetController è NULL!");
        }
        this.currentUser = currentUser;
        System.out.println("CharacterSheetController creato con UserBean: " + this.currentUser);
    }



    public void createLobby(LobbyBean bean) {
        if (bean == null) {
            System.err.println(">>> ERRORE: Il Bean passato a createCharacter() è NULL!");
            return;
        }

        Lobby newlobby = beanToEntity(bean);
        if (currentUser != null) {
            System.out.println(">>> Aggiungendo lobby a UserBean: " + newlobby.getLobbyName());
            currentUser.addLobby(newlobby);
            System.out.println(">>> Lista attuale personaggi: " + currentUser.getJoinedLobbies());
        } else {
            System.err.println(">>> ERRORE: currentUser è NULL in createlobby()!");
        }
    }


    public void updateLobby(String oldName, LobbyBean bean) {
        if (currentUser != null && currentUser.getJoinedLobbies() != null) {
            for (int i = 0; i < currentUser.getJoinedLobbies().size(); i++) {
                Lobby lobby = currentUser.getJoinedLobbies().get(i);
                // Confronta utilizzando oldName (il nome originale)
                if (lobby.getLobbyName().equals(oldName)) {
                    currentUser.getJoinedLobbies().set(i, beanToEntity(bean));
                    return;
                }
            }
            System.err.println(">>> ERRORE: Nessun personaggio trovato con il nome: " + oldName);
        } else {
            System.err.println(">>> ERRORE: currentUser o lista personaggi NULL in updateCharacter()");
        }
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

}
