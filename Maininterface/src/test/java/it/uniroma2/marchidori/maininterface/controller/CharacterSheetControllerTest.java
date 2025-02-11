package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.entity.CharacterSheet;
import it.uniroma2.marchidori.maininterface.entity.CharacterInfo;
import it.uniroma2.marchidori.maininterface.entity.CharacterStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class CharacterSheetControllerTest {

    private CharacterSheetController controller;
    private CharacterSheet characterSheet;

    @BeforeEach
    void setUp() {
        controller = new CharacterSheetController();
    }

    @Test
    void testBeanToEntityConversion_ValidBean() {
        CharacterInfoBean infoBean = new CharacterInfoBean();
        infoBean.setName("Hero");
        infoBean.setRace("Human");
        infoBean.setAge(30);
        infoBean.setClasse("Warrior");
        infoBean.setLevel(10);

        CharacterStatsBean statsBean = new CharacterStatsBean();
        statsBean.setStrength(15);
        statsBean.setDexterity(12);
        statsBean.setIntelligence(10);
        statsBean.setWisdom(8);
        statsBean.setCharisma(14);
        statsBean.setConstitution(13);

        CharacterSheetBean sheetBean = new CharacterSheetBean();
        sheetBean.setInfoBean(infoBean);
        sheetBean.setStatsBean(statsBean);

        characterSheet = controller.beanToEntity(sheetBean);

        assertNotNull(characterSheet, "La conversione non dovrebbe restituire null");

        CharacterInfo infoEntity = characterSheet.getCharacterInfo();
        assertNotNull(infoEntity, "CharacterInfo non dovrebbe essere null");
        assertEquals("Hero", infoEntity.getName(), "Il nome deve corrispondere");
        assertEquals("Human", infoEntity.getRace(), "La razza deve corrispondere");
        assertEquals(30, infoEntity.getAge(), "L'età deve corrispondere");
        assertEquals("Warrior", infoEntity.getClasse(), "La classe deve corrispondere");
        assertEquals(10, infoEntity.getLevel(), "Il livello deve corrispondere");

        CharacterStats statsEntity = characterSheet.getCharacterStats();
        assertNotNull(statsEntity, "CharacterStats non dovrebbe essere null");
        assertEquals(15, statsEntity.getStrength(), "La forza deve corrispondere");
        assertEquals(12, statsEntity.getDexterity(), "La destrezza deve corrispondere");
        assertEquals(10, statsEntity.getIntelligence(), "L'intelligenza deve corrispondere");
        assertEquals(8, statsEntity.getWisdom(), "La saggezza deve corrispondere");
        assertEquals(14, statsEntity.getCharisma(), "Il carisma deve corrispondere");
        assertEquals(13, statsEntity.getConstitution(), "La costituzione deve corrispondere");
    }

    @Test
    void testBeanToEntityConversion_NullBean() {
        characterSheet = controller.beanToEntity(null);
        assertEquals(null, characterSheet);
    }


    @Test
    void testBeanToEntityConversion_PartialInfo() {
        CharacterSheetBean sheetBean = new CharacterSheetBean();

        CharacterInfoBean infoBean = new CharacterInfoBean();
        infoBean.setName(null); // Nome nullo
        infoBean.setRace("Elf");
        infoBean.setAge(100);
        infoBean.setClasse("Archer");
        infoBean.setLevel(7);
        sheetBean.setInfoBean(infoBean);

        CharacterStatsBean statsBean = new CharacterStatsBean();
        statsBean.setStrength(12);
        statsBean.setDexterity(15);
        statsBean.setIntelligence(10);
        statsBean.setWisdom(8);
        statsBean.setCharisma(11);
        statsBean.setConstitution(13);
        sheetBean.setStatsBean(statsBean);

        characterSheet = controller.beanToEntity(sheetBean);

        CharacterInfo infoEntity = characterSheet.getCharacterInfo();
        assertNull(infoEntity.getName(), "Il nome dovrebbe essere null");
        assertEquals("Elf", infoEntity.getRace(), "La razza deve corrispondere");
        assertEquals(100, infoEntity.getAge(), "L'età deve corrispondere");
        assertEquals("Archer", infoEntity.getClasse(), "La classe deve corrispondere");
        assertEquals(7, infoEntity.getLevel(), "Il livello deve corrispondere");

        CharacterStats statsEntity = characterSheet.getCharacterStats();
        assertEquals(12, statsEntity.getStrength(), "La forza deve corrispondere");
        assertEquals(15, statsEntity.getDexterity(), "La destrezza deve corrispondere");
        assertEquals(10, statsEntity.getIntelligence(), "L'intelligenza deve corrispondere");
        assertEquals(8, statsEntity.getWisdom(), "La saggezza deve corrispondere");
        assertEquals(11, statsEntity.getCharisma(), "Il carisma deve corrispondere");
        assertEquals(13, statsEntity.getConstitution(), "La costituzione deve corrispondere");
    }
}
