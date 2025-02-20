package it.uniroma2.marchidori.maininterface.controller;

import it.uniroma2.marchidori.maininterface.control.CharacterListController;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;


import it.uniroma2.marchidori.maininterface.entity.*;
import it.uniroma2.marchidori.maininterface.factory.UserDAOFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static it.uniroma2.marchidori.maininterface.enumerate.RoleEnum.PLAYER;
import static org.junit.jupiter.api.Assertions.*;

////////////////////////////////Leonardo Polidori/////////////////////////////////////

class CharacterListControllerTest {

    private CharacterListController controller; //reference al controller relativo
    private UserBean testUserBean; //userbean di test
    private User testUser; //user entity di test

    // Costanti per i valori di default
    private static final String DEFAULT_RACE = "Human";
    private static final int DEFAULT_AGE = 25;
    private static final String DEFAULT_CLASS = "Warrior";
    private static final int DEFAULT_LEVEL = 5;
    private static final int DEFAULT_STAT = 10;

    //metodo per creare una scheda personaggio bean di test
    private CharacterSheetBean createTestCharacterSheetBean(String name) {
        CharacterInfoBean infoBean = new CharacterInfoBean();
        infoBean.setName(name);
        infoBean.setRace(DEFAULT_RACE);
        infoBean.setAge(DEFAULT_AGE);
        infoBean.setClasse(DEFAULT_CLASS);
        infoBean.setLevel(DEFAULT_LEVEL);

        CharacterStatsBean statsBean = new CharacterStatsBean();
        statsBean.setStrength(DEFAULT_STAT);
        statsBean.setDexterity(DEFAULT_STAT);
        statsBean.setIntelligence(DEFAULT_STAT);
        statsBean.setWisdom(DEFAULT_STAT);
        statsBean.setCharisma(DEFAULT_STAT);
        statsBean.setConstitution(DEFAULT_STAT);

        return new CharacterSheetBean(infoBean, statsBean);
    }

    //crea una scheda personaggio entity di test
    private CharacterSheet createTestCharacterSheetEntity(String name) {
        CharacterInfo info = new CharacterInfo(name, DEFAULT_RACE, DEFAULT_AGE, DEFAULT_CLASS, DEFAULT_LEVEL);
        CharacterStats stats = new CharacterStats(DEFAULT_STAT, DEFAULT_STAT, DEFAULT_STAT, DEFAULT_STAT, DEFAULT_STAT, DEFAULT_STAT);
        return new CharacterSheet(info, stats);
    }


    //Metodo helper per aggiungere un foglio personaggio sia al bean che all'entity.

    private void addCharacterSheetToUser(String name) {
        testUserBean.getCharacterSheets().add(createTestCharacterSheetBean(name));
        testUser.getCharacterSheets().add(createTestCharacterSheetEntity(name));
    }

    @BeforeEach
    void setUp() {
        testUserBean = new UserBean("Test", "test", PLAYER, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        testUser = new User("", PLAYER, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Session.getInstance().setCurrentUser(testUser);
        controller = new CharacterListController();
        controller.setCurrentUser(testUserBean);
    }

    //test per verificare se il delete di un personaggio esistente funziona
    @Test
    void testDeleteCharacter_ExistingCharacter() {
        addCharacterSheetToUser("TestCharacter"); //aggiungo ad entity e bean di test


        //verifico se l aggiunta ad entrambi sia stata eseguita
        assertEquals(1, testUserBean.getCharacterSheets().size());
        assertEquals(1, testUser.getCharacterSheets().size());
        Session.getInstance().setUserDAO(UserDAOFactory.getInstance().getUserDAO(Session.getInstance().getDB()));

        controller.deleteCharacter("TestCharacter"); //chiamata del controller per il delete

        //mi assicuro che avvenga il delete
        assertEquals(0, testUserBean.getCharacterSheets().size(),
                "La lista dei CharacterSheetBean dovrebbe essere vuota dopo l'eliminazione");
        assertEquals(0, testUser.getCharacterSheets().size(),
                "La lista delle entity CharacterSheet dovrebbe essere vuota dopo l'eliminazione");
    }

    //test per verificare il funzionamento del delete di una scheda personaggio non esistente
    @Test
    void testDeleteCharacter_NonExistingCharacter() {
        addCharacterSheetToUser("ExistingCharacter");

        //verifico l aggiunta sia avvenuta correttamente
        assertEquals(1, testUserBean.getCharacterSheets().size());
        assertEquals(1, testUser.getCharacterSheets().size());

        controller.deleteCharacter("NonExistingCharacter");//chiamata al controller per il delete

        //verifico che il delete non sia avvenuto poiche personaggio non esistente
        assertEquals(1, testUserBean.getCharacterSheets().size(),
                "La lista dei CharacterSheetBean non dovrebbe essere modificata");
        assertEquals(1, testUser.getCharacterSheets().size(),
                "La lista delle entity CharacterSheet non dovrebbe essere modificata");
    }

    //verifico che il metodo delete funzioni con uno user nullo e che non crei errori
    @Test
    void testDeleteCharacter_NullUser() {
        controller.setCurrentUser(null);

        // Aggiungo solo l'entity, dato che il bean non viene usato se currentUser è null
        testUser.getCharacterSheets().add(createTestCharacterSheetEntity("TestCharacter"));

        controller.deleteCharacter("TestCharacter");

        assertEquals(1, testUser.getCharacterSheets().size(),
                "La lista delle entity CharacterSheet dovrebbe rimanere invariata se currentUser è null");
    }

    //test per verificare che delete funzioni con una lista nulla e che non crei errori
    @Test
    void testDeleteCharacter_NullCharacterSheetsInUserBean() {
        testUserBean.setCharacterSheets(null);

        testUser.getCharacterSheets().add(createTestCharacterSheetEntity("TestCharacter"));

        controller.deleteCharacter("TestCharacter");

        assertEquals(1, testUser.getCharacterSheets().size(),
                "La lista delle entity CharacterSheet dovrebbe rimanere invariata se la lista del UserBean è null");
    }
}