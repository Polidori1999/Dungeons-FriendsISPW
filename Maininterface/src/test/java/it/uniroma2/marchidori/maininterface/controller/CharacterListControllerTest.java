package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.control.CharacterListController;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;
import static org.junit.jupiter.api.Assertions.*;

class CharacterListControllerTest {

    private CharacterListController controller;
    private UserBean testUserBean;
    private User testUser;


    private CharacterSheetBean createTestCharacterSheetBean(String name) {
        CharacterInfoBean infoBean = new CharacterInfoBean();
        infoBean.setName(name);
        infoBean.setRace("Human");
        infoBean.setAge(25);
        infoBean.setClasse("Warrior");
        infoBean.setLevel(5);

        CharacterStatsBean statsBean = new CharacterStatsBean();
        statsBean.setStrength(10);
        statsBean.setDexterity(10);
        statsBean.setIntelligence(10);
        statsBean.setWisdom(10);
        statsBean.setCharisma(10);
        statsBean.setConstitution(10);

        return new CharacterSheetBean(infoBean, statsBean);
    }


    private CharacterSheet createTestCharacterSheetEntity(String name) {
        CharacterInfo info = new CharacterInfo(name, "Human", 25, "Warrior", 5);
        CharacterStats stats = new CharacterStats(10, 10, 10, 10, 10, 10);
        return new CharacterSheet(info, stats);
    }

    @BeforeEach
    void setUp() {
        testUserBean = new UserBean("Test","test",PLAYER,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());

        testUser = new User("", PLAYER, new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        Session.getInstance().setCurrentUser(testUser);
        controller = new CharacterListController();
        controller.setCurrentUser(testUserBean);
    }


    @Test
    void testDeleteCharacter_ExistingCharacter() {
        CharacterSheetBean sheetBean = createTestCharacterSheetBean("TestCharacter");
        CharacterSheet sheetEntity = createTestCharacterSheetEntity("TestCharacter");

        testUserBean.getCharacterSheets().add(sheetBean);
        testUser.getCharacterSheets().add(sheetEntity);

        assertEquals(1, testUserBean.getCharacterSheets().size());
        assertEquals(1, testUser.getCharacterSheets().size());

        controller.deleteCharacter("TestCharacter");

        assertEquals(0, testUserBean.getCharacterSheets().size(),
                "La lista dei CharacterSheetBean dovrebbe essere vuota dopo l'eliminazione");
        assertEquals(0, testUser.getCharacterSheets().size(),
                "La lista delle entity CharacterSheet dovrebbe essere vuota dopo l'eliminazione");
    }


    @Test
    void testDeleteCharacter_NonExistingCharacter() {
        CharacterSheetBean sheetBean = createTestCharacterSheetBean("ExistingCharacter");
        CharacterSheet sheetEntity = createTestCharacterSheetEntity("ExistingCharacter");

        testUserBean.getCharacterSheets().add(sheetBean);
        testUser.getCharacterSheets().add(sheetEntity);

        assertEquals(1, testUserBean.getCharacterSheets().size());
        assertEquals(1, testUser.getCharacterSheets().size());

        controller.deleteCharacter("NonExistingCharacter");

        assertEquals(1, testUserBean.getCharacterSheets().size(),
                "La lista dei CharacterSheetBean non dovrebbe essere modificata");
        assertEquals(1, testUser.getCharacterSheets().size(),
                "La lista delle entity CharacterSheet non dovrebbe essere modificata");
    }


    @Test
    void testDeleteCharacter_NullUser() {
        controller.setCurrentUser(null);

        CharacterSheet sheetEntity = createTestCharacterSheetEntity("TestCharacter");
        testUser.getCharacterSheets().add(sheetEntity);

        controller.deleteCharacter("TestCharacter");

        assertEquals(1, testUser.getCharacterSheets().size(),
                "La lista delle entity CharacterSheet dovrebbe rimanere invariata se currentUser è null");
    }


    @Test
    void testDeleteCharacter_NullCharacterSheetsInUserBean() {
        testUserBean.setCharacterSheets(null);

        CharacterSheet sheetEntity = createTestCharacterSheetEntity("TestCharacter");
        testUser.getCharacterSheets().add(sheetEntity);

        controller.deleteCharacter("TestCharacter");

        assertEquals(1, testUser.getCharacterSheets().size(),
                "La lista delle entity CharacterSheet dovrebbe rimanere invariata se la lista del UserBean è null");
    }
}
