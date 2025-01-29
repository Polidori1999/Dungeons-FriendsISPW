package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.AbilityScoresBean;
import it.uniroma2.marchidori.maininterface.bean.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;

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
        allCharacters.add(new CharacterSheet("Aragorn", "Human", "25", "Ranger", "5",
                "16", "14", "12", "10", "8", "13"));
        allCharacters.add(new CharacterSheet("Legolas", "Elf", "90", "Archer", "7",
                "14", "18", "10", "12", "16", "10"));
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
        CharacterSheet existing = findByName(bean.getInfo().getName());
        if (existing != null) {
            // Aggiorniamo la parte "info"
            existing.setRace(bean.getInfo().getRace());
            existing.setAge(bean.getInfo().getAge());
            existing.setClasse(bean.getInfo().getClasse());
            existing.setLevel(bean.getInfo().getLevel());

            // Aggiorniamo la parte "abilityScores"
            existing.setStrength(bean.getAbilityScores().getStrength());
            existing.setDexterity(bean.getAbilityScores().getDexterity());
            existing.setIntelligence(bean.getAbilityScores().getIntelligence());
            existing.setWisdom(bean.getAbilityScores().getWisdom());
            existing.setCharisma(bean.getAbilityScores().getCharisma());
            existing.setConstitution(bean.getAbilityScores().getConstitution());
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
        AbilityScoresBean abilityBean = new AbilityScoresBean(
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
        CharacterInfoBean info = bean.getInfo();
        AbilityScoresBean ability = bean.getAbilityScores();

        return new CharacterSheet(
                info.getName(),
                info.getRace(),
                info.getAge(),
                info.getClasse(),
                info.getLevel(),
                ability.getStrength(),
                ability.getDexterity(),
                ability.getIntelligence(),
                ability.getWisdom(),
                ability.getCharisma(),
                ability.getConstitution()
        );
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
