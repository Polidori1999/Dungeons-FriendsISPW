package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.entity.*;
import it.uniroma2.marchidori.maininterface.utils.CharacterSheetDownloadTask;

import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CharacterSheetController implements UserAwareInterface {

    private UserBean currentUser;
    private User currentEntity;

    private static final Logger logger = Logger.getLogger(CharacterSheetController.class.getName());

    public CharacterSheetController() {
        // empty
        currentEntity = Session.getInstance().getCurrentUser();

    }

    public CharacterSheetController(UserBean currentUser) {
        if (currentUser == null) {
            throw new IllegalArgumentException("UserBean passato a CharacterSheetController è NULL!");
        }
        this.currentUser = currentUser;
        logger.info(() -> "CharacterSheetController creato con UserBean: " + this.currentUser);
    }

    /**
     * Crea un nuovo personaggio, partendo dal Bean.
     * Converte il CharacterSheetBean (spezzato in info e ability)
     * in una Entity "CharacterSheet" e la aggiunge alla lista in memoria.
     */
    public void createChar(CharacterSheetBean bean) {
        if (bean == null) {
            logger.severe(">>> ERRORE: Il Bean passato a createCharacter() è NULL!");
            return;
        }

        CharacterSheet newChar = beanToEntity(bean);
        if (currentUser != null) {
            logger.info(">>> Aggiungendo lobby a UserBean: " + newChar.getCharacterInfo().getName());
            currentUser.getCharacterSheets().add(bean);
            currentEntity.getCharacterSheets().add(newChar);
            logger.info(">>> Lista attuale personaggi: " + currentUser.getJoinedLobbies());
        } else {
            logger.severe(">>> ERRORE: currentUser è NULL in createlobby()!");
        }
    }

    /**
     * Aggiorna un personaggio esistente (cerca per nome).
     * Usa i campi di CharacterSheetBean (spezzati in info e ability).
     */
    public void updateChar(String oldName, CharacterSheetBean bean) {
        if (currentUser != null && currentUser.getCharacterSheets() != null) {
            // Cerca la lobby nella lista dello user
            for (int i = 0; i < currentUser.getCharacterSheets().size(); i++) {
                CharacterSheetBean cs = currentUser.getCharacterSheets().get(i);
                // Confronta usando oldName (il nome originale)
                if (cs.getInfoBean().getName().equals(oldName)) {
                    // Converte il bean aggiornato in un'entità CharacterSheet
                    CharacterSheet updatedCharacter = beanToEntity(bean);
                    // Aggiorna la lobby nella lista dello user
                    currentUser.getCharacterSheets().set(i, bean);
                    currentEntity.getCharacterSheets().set(i, updatedCharacter);
                    // Aggiorna anche la repository:
                    // Rimuove la vecchia lobby e aggiunge quella aggiornata.
                    logger.info(">>> Lobby aggiornata correttamente in UserBean e Repository.");
                    return;
                }
            }
            logger.log(Level.SEVERE, () -> ">>> ERRORE: Nessuna lobby trovata con il nome: " + oldName);
        } else {
            logger.severe(">>> ERRORE: currentUser o la lista delle lobby è NULL in updateLobby().");
        }
    }

    // -------------------------------------------------------------
    //                      METODI PRIVATI
    // -------------------------------------------------------------

    private CharacterSheet beanToEntity(CharacterSheetBean bean) {
        return getCharacterSheet(bean);
    }

    static CharacterSheet getCharacterSheet(CharacterSheetBean bean) {
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

    public CharacterSheetDownloadTask getDownloadTask(CharacterSheetBean bean) {
        return getCharacterSheetDownloadTask(bean, logger);
    }

    static CharacterSheetDownloadTask getCharacterSheetDownloadTask(CharacterSheetBean bean, Logger logger) {
        try {
            // Ottieni la cartella di download dinamica
            String userHome = System.getProperty("user.home");
            String downloadFolder = Paths.get(userHome, "Downloads").toString();

            // Crea il nome del file
            String fileName = "character_" + bean.getInfoBean().getName() + ".txt";
            String destinationPath = Paths.get(downloadFolder, fileName).toString();

            logger.info(() -> ">>> DEBUG: Percorso di download: " + destinationPath);

            // Crea e restituisci il task di download
            return new CharacterSheetDownloadTask(bean, destinationPath);

        } catch (Exception e) {
            logger.severe("Errore durante il download: " + e.getMessage());
            return null;
        }
    }

    public String validate(CharacterSheetBean characterSheet) {
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

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }
}
