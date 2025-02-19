package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;

import it.uniroma2.marchidori.maininterface.entity.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CharacterSheetController implements UserAwareInterface {

    private UserBean currentUser;
    private final Jout jout = new Jout("CharacterSheetController");

    private User currentEntity = Session.getInstance().getCurrentUser();

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
    public void createChar(CharacterSheetBean characterSheetBean) {
        // Aggiungi il nuovo character sheet alla lista dell'utente
        currentUser.getCharacterSheets().add(characterSheetBean);
        currentEntity.getCharacterSheets().add(Converter.characterSheetBeanToEntity(characterSheetBean));
        // Converti il bean in entity e aggiungilo allo User Entity (per la persistenza)

        // Log: stampa il numero di personaggi attuali e i loro nomi
        jout.print("Dopo createChar, currentUser ha " + currentUser.getCharacterSheets().size() + " personaggi.");
        // Ora aggiorna il file (usa updateUsersEntityData per riscrivere completamente il file)
        UserDAO dao = Session.getInstance().getUserDAO();
        dao.updateUsersEntityData(currentEntity);
    }



    /**
     * Aggiorna un personaggio esistente (cerca per nome).
     * Usa i campi di CharacterSheetBean (spezzati in info e ability).
     */
    public void updateChar(String oldName, CharacterSheetBean characterSheetBean) {
        if (currentUser != null && currentUser.getCharacterSheets() != null) {
            boolean found = false;
            // Cerca la lobby nella lista dello user
            for (int i = 0; i < currentUser.getCharacterSheets().size(); i++) {
                CharacterSheetBean cs = currentUser.getCharacterSheets().get(i);
                // Confronta usando oldName (il nome originale)
                if (cs.getInfoBean().getName().equals(oldName)) {

                    currentUser.getCharacterSheets().set(i, characterSheetBean);
                    // Converte il bean aggiornato in un'entità CharacterSheet
                    CharacterSheet updatedCharacter = Converter.characterSheetBeanToEntity(characterSheetBean);
                    // Aggiorna la lobby nella lista dello user

                    currentEntity.getCharacterSheets().set(i, updatedCharacter);
                    found=true;
                    break;
                }
            }
            if(!found) {
                logger.log(Level.SEVERE,()->"Errore nessun personaggio trovato");
                return;
            }
            UserDAO userDAO = Session.getInstance().getUserDAO();
            userDAO.updateUsersEntityData(currentEntity);



        }else {
            logger.severe(">>> ERRORE: currentUser o la lista delle lobby è NULL in updateLobby().");
        }
    }

    // -------------------------------------------------------------
    //                      METODI PRIVATI
    // -------------------------------------------------------------


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
