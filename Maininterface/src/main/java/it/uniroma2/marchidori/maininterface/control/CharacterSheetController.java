package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.characterSheet.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.bean.characterSheet.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.characterSheet.CharacterSheetBean;
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

    private final List<CharacterSheet> allCharacters;

    public CharacterSheetController() {
        // Carichiamo qualche personaggio di esempio (Entity pura)
        allCharacters = new ArrayList<>();
        // 1) Aragorn
        CharacterInfo infoAragorn = new CharacterInfo("Aragorn", "Human", 25, "Ranger", 5);
        CharacterStats statsAragorn = new CharacterStats(16, 14, 12, 10, 8, 13);
        allCharacters.add(new CharacterSheet(infoAragorn, statsAragorn));

        // 2) Legolas
        CharacterInfo infoLegolas = new CharacterInfo("Legolas", "Elf", 90, "Archer", 7);
        CharacterStats statsLegolas = new CharacterStats(14, 18, 10, 12, 16, 10);
        allCharacters.add(new CharacterSheet(infoLegolas, statsLegolas));
    }

    /**
     * Restituisce tutti i personaggi come Bean (per la UI).
     * Ogni Entity viene convertita in un CharacterSheetBean
     * che a sua volta contiene un CharacterInfoBean e un AbilityScoresBean.
     */
    public List<CharacterSheetBean> getAllCharacters() {
        List<CharacterSheetBean> beans = new ArrayList<>();
        for (CharacterSheet cs : allCharacters) {
            beans.add(entityToBean(cs));
        }
        return beans;
    }

    /**
     * Crea un nuovo personaggio, partendo dal Bean.
     * Converte il CharacterSheetBean (spezzato in info e ability)
     * in una Entity "CharacterSheet" e la aggiunge alla lista in memoria.
     */
    public void createCharacter(CharacterSheetBean bean) {
        CharacterSheet newCS = beanToEntity(bean);
        allCharacters.add(newCS);
    }

    /**
     * Aggiorna un personaggio esistente (cerca per nome).
     * Usa i campi di CharacterSheetBean (spezzati in info e ability).
     */
    public void updateCharacter(CharacterSheetBean bean) {
        // Troviamo la Entity esistente basandoci sul nome
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
        for (CharacterSheet cs : allCharacters) {
            if (cs.getName().equals(name)) {
                return cs;
            }
        }
        return null;
    }

}
