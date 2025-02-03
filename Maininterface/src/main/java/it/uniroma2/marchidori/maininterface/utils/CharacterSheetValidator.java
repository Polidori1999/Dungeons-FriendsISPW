package it.uniroma2.marchidori.maininterface.utils;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;

public class CharacterSheetValidator {
    public static String validate(CharacterSheetBean characterSheet) {
        StringBuilder errors = new StringBuilder();

        if (characterSheet.getInfoBean().getName() == null || characterSheet.getInfoBean().getName().trim().isEmpty()) {
            errors.append("Il nome non può essere vuoto.\n");
        }
        if (characterSheet.getInfoBean().getRace() == null || characterSheet.getInfoBean().getRace().trim().isEmpty()) {
            errors.append("La razza non può essere vuota.\n");
        }
        if (characterSheet.getInfoBean().getClasse() == null || characterSheet.getInfoBean().getClasse().trim().isEmpty()) {
            errors.append("La classe non può essere vuota.\n");
        }
        if (characterSheet.getInfoBean().getLevel() < 1) {
            errors.append("Il livello deve essere almeno 1.\n");
        }

        // Controllo sugli attributi (devono essere tra 1 e 20)
        if (characterSheet.getStatsBean().getStrength() < 1 || characterSheet.getStatsBean().getStrength() > 20) {
            errors.append("La forza deve essere tra 1 e 20.\n");
        }
        if (characterSheet.getStatsBean().getDexterity() < 1 || characterSheet.getStatsBean().getDexterity() > 20) {
            errors.append("La destrezza deve essere tra 1 e 20.\n");
        }
        if (characterSheet.getStatsBean().getIntelligence() < 1 || characterSheet.getStatsBean().getIntelligence() > 20) {
            errors.append("L'intelligenza deve essere tra 1 e 20.\n");
        }
        if (characterSheet.getStatsBean().getWisdom() < 1 || characterSheet.getStatsBean().getWisdom() > 20) {
            errors.append("La saggezza deve essere tra 1 e 20.\n");
        }
        if (characterSheet.getStatsBean().getCharisma() < 1 || characterSheet.getStatsBean().getCharisma() > 20) {
            errors.append("Il carisma deve essere tra 1 e 20.\n");
        }
        if (characterSheet.getStatsBean().getConstitution() < 1 || characterSheet.getStatsBean().getConstitution() > 20) {
            errors.append("La costituzione deve essere tra 1 e 20.\n");
        }
        if (characterSheet.getInfoBean().getLevel() < 1 || characterSheet.getInfoBean().getLevel() > 20) {
            errors.append("Il livello deve essere tra 1 e 20.\n");
        }


        return errors.toString();
    }
}
