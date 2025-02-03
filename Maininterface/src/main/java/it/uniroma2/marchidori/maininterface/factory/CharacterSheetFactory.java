package it.uniroma2.marchidori.maininterface.factory;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;

public class CharacterSheetFactory {

    private CharacterSheetFactory() {}


    public static CharacterSheetBean createCharacterSheet() {
        return new CharacterSheetBean(new CharacterInfoBean("", "", 1, "", 1), new CharacterStatsBean(10, 10, 10, 10, 10, 10));
    }

}
