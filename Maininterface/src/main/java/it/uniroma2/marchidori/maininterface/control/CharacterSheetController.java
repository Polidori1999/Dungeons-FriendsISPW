package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.*;

/**
 * Control che gestisce la logica di manipolazione
 *
 * di CharacterSheet, mantenendo una lista in memoria.
 */
public class CharacterSheetController implements UserAwareInterface {

    private UserBean currentUser;
    private User currentEntity = Session.getCurrentUser();
    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    public CharacterSheetController(){}

    public CharacterSheetController(UserBean currentUser) {
        if (currentUser == null) {
            throw new IllegalArgumentException("UserBean passato a CharacterSheetController è NULL!");
        }
        this.currentUser = currentUser;
        System.out.println("CharacterSheetController creato con UserBean: " + this.currentUser);
    }

    /**
     * Crea un nuovo personaggio, partendo dal Bean.
     * Converte il CharacterSheetBean (spezzato in info e ability)
     * in una Entity "CharacterSheet" e la aggiunge alla lista in memoria.
     */
    public void createChar(CharacterSheetBean bean) {
        if (bean == null) {
            System.err.println(">>> ERRORE: Il Bean passato a createCharacter() è NULL!");
            return;
        }

        CharacterSheet newChar = beanToEntity(bean);
        if (currentUser != null) {
            System.out.println(">>> Aggiungendo lobby a UserBean: " + newChar.getCharacterInfo().getName());
            currentUser.addCharacterSheet(bean);
            currentEntity.addCharacterSheet(newChar);
            System.out.println(">>> Lista attuale personaggi: " + currentUser.getJoinedLobbies());
        } else {
            System.err.println(">>> ERRORE: currentUser è NULL in createlobby()!");
        }
    }


    /**
     * Aggiorna un personaggio esistente (cerca per nome).
     * Usa i campi di CharacterSheetBean (spezzati in info e ability).
     */
    public void updateChar(String oldName, CharacterSheetBean bean) {
        if (currentUser != null && currentUser.getCharacterSheets() != null) {
            // Cerca la lobby nella lista dello user
            for (int i = 0; i < currentUser.getCharacterSheets().size(); i++) {
                CharacterSheetBean cs = currentUser.getCharacterSheets().get(i);
                // Confronta usando oldName (il nome originale)
                if (cs.getInfoBean().getName().equals(oldName)) {
                    // Converte il bean aggiornato in un'entità Lobby
                    CharacterSheet updatedLobby = beanToEntity(bean);
                    // Aggiorna la lobby nella lista dello user
                    currentUser.getCharacterSheets().set(i, bean);
                    currentEntity.getCharacterSheets().set(i, updatedLobby);
                    // Aggiorna anche la repository:
                    // Rimuove la vecchia lobby e aggiunge quella aggiornata.
                    System.out.println(">>> Lobby aggiornata correttamente in UserBean e Repository.");
                    return;
                }
            }
            System.err.println(">>> ERRORE: Nessuna lobby trovata con il nome: " + oldName);
        } else {
            System.err.println(">>> ERRORE: currentUser o la lista delle lobby è NULL in updateLobby().");
        }
    }








    // -------------------------------------------------------------
    //                      METODI PRIVATI
    // -------------------------------------------------------------

    /**
     * Converte da Entity pura a Bean complesso.
     * - Crea un CharacterInfoBean con i dati generici
     * - Crea un AbilityScoresBean con i punteggi
     * - Inserisce tutto nel CharacterSheetBean
     */
    private CharacterSheetBean entityToBean(CharacterSheet cs) {
        // Crea la parte "info"
        CharacterInfoBean infoBean = new CharacterInfoBean(
                cs.getName(),
                cs.getRace(),
                cs.getAge(),
                cs.getClasse(),
                cs.getLevel()
        );

        // Crea la parte "ability scores"
        CharacterStatsBean abilityBean = new CharacterStatsBean(
                cs.getStrength(),
                cs.getDexterity(),
                cs.getIntelligence(),
                cs.getWisdom(),
                cs.getCharisma(),
                cs.getConstitution()
        );

        // Infine crea il bean complessivo
        return new CharacterSheetBean(infoBean, abilityBean);
    }

    /**
     * Converte da Bean "spezzato" a Entity pura.
     */
    private CharacterSheet beanToEntity(CharacterSheetBean bean) {
        CharacterInfoBean infoBean = bean.getInfoBean();
        CharacterStatsBean statsBean = bean.getStatsBean();

        CharacterInfo infoEntity = new CharacterInfo(
                infoBean.getName(),
                infoBean.getRace(),
                infoBean.getAge(),
                infoBean.getClasse(),
                infoBean.getLevel()
        );
        CharacterStats statsEntity = new CharacterStats(
                statsBean.getStrength(),
                statsBean.getDexterity(),
                statsBean.getIntelligence(),
                statsBean.getWisdom(),
                statsBean.getCharisma(),
                statsBean.getConstitution()
        );
        return new CharacterSheet(infoEntity, statsEntity);
    }
}
