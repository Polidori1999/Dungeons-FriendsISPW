package it.uniroma2.marchidori.maininterface.control;


import it.uniroma2.marchidori.maininterface.bean.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Control che gestisce la logica di manipolazione
 * di CharacterSheet, mantenendo una lista in memoria.
 */
public class CharacterSheetController {

    private List<CharacterSheet> allCharacters;

    public CharacterSheetController() {
        // Carichiamo qualche personaggio di esempio
        allCharacters = new ArrayList<>();
        allCharacters.add(new CharacterSheet("Aragorn", "Human", "25", "Ranger", "5",
                "16", "14", "12", "10", "8", "13"));
        allCharacters.add(new CharacterSheet("Legolas", "Elf", "90", "Archer", "7",
                "14", "18", "10", "12", "16", "10"));
    }

    /**
     * Restituisce tutti i personaggi come Bean (per la UI).
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
     */
    public void createCharacter(CharacterSheetBean bean) {
        CharacterSheet newCS = beanToEntity(bean);
        allCharacters.add(newCS);
    }

    /**
     * Aggiorna un personaggio esistente (lo cerchiamo per nome).
     */
    public void updateCharacter(CharacterSheetBean bean) {
        CharacterSheet existing = findByName(bean.getName());
        if (existing != null) {
            existing.setRace(bean.getRace());
            existing.setAge(bean.getAge());
            existing.setClasse(bean.getClasse());
            existing.setLevel(bean.getLevel());
            existing.setStrength(bean.getStrength());
            existing.setDexterity(bean.getDexterity());
            existing.setIntelligence(bean.getIntelligence());
            existing.setWisdom(bean.getWisdom());
            existing.setCharisma(bean.getCharisma());
            existing.setConstitution(bean.getConstitution());
        }
    }

    // -- Conversioni

    private CharacterSheetBean entityToBean(CharacterSheet cs) {
        return new CharacterSheetBean(
                cs.getName(),
                cs.getRace(),
                cs.getAge(),
                cs.getClasse(),
                cs.getLevel(),
                cs.getStrength(),
                cs.getDexterity(),
                cs.getIntelligence(),
                cs.getWisdom(),
                cs.getCharisma(),
                cs.getConstitution()
        );
    }

    private CharacterSheet beanToEntity(CharacterSheetBean bean) {
        return new CharacterSheet(
                bean.getName(),
                bean.getRace(),
                bean.getAge(),
                bean.getClasse(),
                bean.getLevel(),
                bean.getStrength(),
                bean.getDexterity(),
                bean.getIntelligence(),
                bean.getWisdom(),
                bean.getCharisma(),
                bean.getConstitution()
        );
    }

    private CharacterSheet findByName(String name) {
        for (CharacterSheet cs : allCharacters) {
            if (cs.getName().equals(name)) {
                return cs;
            }
        }
        return null;
    }
}
