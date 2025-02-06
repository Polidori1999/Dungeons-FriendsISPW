package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;

import it.uniroma2.marchidori.maininterface.repository.LobbyRepository;

import it.uniroma2.marchidori.maininterface.entity.Lobby;

public class ManageLobbyController implements UserAwareInterface {
    public UserBean currentUser;
    public User currentEntity = Session.getCurrentUser();
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
        LobbyRepository.addLobby(newlobby);
        if (currentUser != null && currentUser.getJoinedLobbies() != null) {
            System.out.println(">>> Aggiungendo lobby a UserBean: " + newlobby.getLobbyName());
            currentUser.getJoinedLobbies().add(bean);
            currentEntity.getJoinedLobbies().add(newlobby);
            System.out.println(">>> Lista attuale personaggi: " + currentUser.getJoinedLobbies());
        } else {
            System.err.println(">>> ERRORE: currentUser è NULL in createlobby()!");
        }
    }


    public void updateLobby(String oldName, LobbyBean bean) {
        if (currentUser != null && currentUser.getJoinedLobbies() != null) {
            // Cerca la lobby nella lista dello user
            for (int i = 0; i < currentUser.getJoinedLobbies().size(); i++) {
                LobbyBean lobbyBean = currentUser.getJoinedLobbies().get(i);
                // Confronta usando oldName (il nome originale)
                if (lobbyBean.getName().equals(oldName)) {
                    // Converte il bean aggiornato in un'entità Lobby
                    Lobby updatedLobby = beanToEntity(bean);
                    // Aggiorna la lobby nella lista dello user
                    currentUser.getJoinedLobbies().set(i, bean);
                    currentEntity.getJoinedLobbies().set(i, updatedLobby);

                    // Aggiorna anche la repository:
                    // Rimuove la vecchia lobby e aggiunge quella aggiornata.
                    LobbyRepository.removeLobby(oldName);
                    LobbyRepository.addLobby(updatedLobby);
                    System.out.println(">>> Lobby aggiornata correttamente in UserBean e Repository.");
                    return;
                }
            }
            System.err.println(">>> ERRORE: Nessuna lobby trovata con il nome: " + oldName);
        } else {
            System.err.println(">>> ERRORE: currentUser o la lista delle lobby è NULL in updateLobby().");
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
