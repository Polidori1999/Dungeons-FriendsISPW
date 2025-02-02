package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.entity.CharacterInfo;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.CharacterStats;


import java.util.ArrayList;
import java.util.List;

/**
 * Control che gestisce la logica di manipolazione
 * di CharacterSheet, mantenendo una lista in memoria.
 */
public class CharacterSheetController {
    private UserBean currentUser;
    public CharacterSheetController(UserBean currentUser) {
        if (currentUser == null) {
            throw new IllegalArgumentException("UserBean passato a CharacterSheetController è NULL!");
        }
        this.currentUser = currentUser;
        System.out.println("CharacterSheetController creato con UserBean: " + this.currentUser);
    }

    //commentato per debug
    /*// Riferimento allo userBean corrente (che contiene i characterSheets dell'utente)
    private UserBean currentUser;

    // Costruttore che riceve lo UserBean corrente
    public CharacterSheetController(UserBean currentUser) {
        this.currentUser = currentUser;
    }*/


    /**
     * Restituisce tutti i personaggi come Bean (per la UI).
     * Ogni Entity viene convertita in un CharacterSheetBean
     * che a sua volta contiene un CharacterInfoBean e un AbilityScoresBean.
     */
    public List<CharacterSheetBean> getAllCharacters() {
        List<CharacterSheetBean> beans = new ArrayList<>();
        if (currentUser != null && currentUser.getCharacterSheets() != null) {
            for (CharacterSheet cs : currentUser.getCharacterSheets()) {
                beans.add(entityToBean(cs));
            }
        }
        return beans;
    }

    /**
     * Crea un nuovo personaggio, partendo dal Bean.
     * Converte il CharacterSheetBean (spezzato in info e ability)
     * in una Entity "CharacterSheet" e la aggiunge alla lista in memoria.
     */
    public void createCharacter(CharacterSheetBean bean) {
        if (bean == null || bean.getInfoBean() == null) {
            System.err.println(">>> ERRORE: Il Bean passato a createCharacter() è NULL!");
            return;
        }

        CharacterSheet newCS = beanToEntity(bean);
        if (currentUser != null) {
            System.out.println(">>> Aggiungendo personaggio a UserBean: " + newCS.getName());
            currentUser.addCharacterSheet(newCS);
            System.out.println(">>> Lista attuale personaggi: " + currentUser.getCharacterSheets());
        } else {
            System.err.println(">>> ERRORE: currentUser è NULL in createCharacter()!");
        }
    }







    /**
     * Aggiorna un personaggio esistente (cerca per nome).
     * Usa i campi di CharacterSheetBean (spezzati in info e ability).
     */
    public void updateCharacter(CharacterSheetBean bean) {
        if (currentUser != null && currentUser.getCharacterSheets() != null) {
            CharacterSheet existing = findByName(bean.getInfoBean().getName());
            if (existing != null) {
                // Aggiorniamo la parte "info"
                existing.setRace(bean.getInfoBean().getRace());
                existing.setAge(bean.getInfoBean().getAge());
                existing.setClasse(bean.getInfoBean().getClasse());
                existing.setLevel(bean.getInfoBean().getLevel());

                // Aggiorniamo la parte "abilityScores"
                existing.setStrength(bean.getStatsBean().getStrength());
                existing.setDexterity(bean.getStatsBean().getDexterity());
                existing.setIntelligence(bean.getStatsBean().getIntelligence());
                existing.setWisdom(bean.getStatsBean().getWisdom());
                existing.setCharisma(bean.getStatsBean().getCharisma());
                existing.setConstitution(bean.getStatsBean().getConstitution());
            }
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

    /**
     * Cerca un personaggio (Entity) in base al nome.
     */
    private CharacterSheet findByName(String name) {
        if (currentUser != null && currentUser.getCharacterSheets() != null) {
            for (CharacterSheet cs : currentUser.getCharacterSheets()) {
                if (cs.getName().equals(name)) {
                    return cs;
                }
            }
        }
        return null;
    }
}
