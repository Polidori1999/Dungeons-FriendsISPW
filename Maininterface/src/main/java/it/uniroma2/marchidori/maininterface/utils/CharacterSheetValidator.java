package it.uniroma2.marchidori.maininterface.utils;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;

public class CharacterSheetValidator {

    private CharacterSheetValidator() {}


    public static String validate(CharacterSheetBean characterSheet) {
        StringBuilder errors = new StringBuilder();

        // Validazione dei campi di testo (non vuoti)
        validateNotEmpty(characterSheet.getInfoBean().getName(), "Il nome", errors);
        validateNotEmpty(characterSheet.getInfoBean().getRace(), "La razza", errors);
        validateNotEmpty(characterSheet.getInfoBean().getClasse(), "La classe", errors);

        // Validazione del livello (tra 1 e 20)
        validateRange(characterSheet.getInfoBean().getLevel(), "Il livello", errors);

        // Validazione degli attributi (devono essere tra 1 e 20)
        validateRange(characterSheet.getStatsBean().getStrength(), "La forza", errors);
        validateRange(characterSheet.getStatsBean().getDexterity(), "La destrezza", errors);
        validateRange(characterSheet.getStatsBean().getIntelligence(), "L'intelligenza", errors);
        validateRange(characterSheet.getStatsBean().getWisdom(), "La saggezza", errors);
        validateRange(characterSheet.getStatsBean().getCharisma(), "Il carisma", errors);
        validateRange(characterSheet.getStatsBean().getConstitution(), "La costituzione", errors);

        return errors.toString();
    }

    /**
     * Verifica che il valore testuale non sia null o vuoto.
     *
     * @param value il valore da controllare
     * @param fieldName il nome del campo (per il messaggio di errore)
     * @param errors lo StringBuilder su cui appendere eventuali errori
     */
    private static void validateNotEmpty(String value, String fieldName, StringBuilder errors) {
        if (value == null || value.trim().isEmpty()) {
            errors.append(fieldName).append(" non può essere vuoto.\n");
        }
    }

    /**
     * Verifica che un valore numerico sia compreso tra min e max (inclusi).
     *
     * @param value     il valore da controllare
     * @param fieldName il nome del campo (per il messaggio di errore)
     * @param errors    lo StringBuilder su cui appendere eventuali errori
     */
    private static void validateRange(int value, String fieldName, StringBuilder errors) {
        if (value < 1 || value > 20) {
            errors.append(fieldName)
                    .append(" deve essere tra ")
                    .append(1)
                    .append(" e ")
                    .append(20)
                    .append(".\n");
        }
    }
}