package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterInfoBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterStatsBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.CharacterSheetController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.Alert;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CharacterSheetCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    private static final Logger logger = Logger.getLogger(CharacterSheetCLIBoundary.class.getName());

    private UserBean currentUser;
    private CharacterSheetController controller;

    // Modalità: true = creazione, false = modifica
    private boolean creationMode;
    // Il bean che rappresenta il Character Sheet
    private CharacterSheetBean currentBean;
    // Nome precedente (usato per l'update in modalità modifica)
    private String oldName;

    private final Scanner scanner = new Scanner(System.in);
    private final Jout jout = new Jout("CharacterSheetCLIBoundary");

    @Override
    public void run() throws IOException {
        if (currentUser == null) {
            jout.print("ERRORE: Utente non inizializzato.");
            return;
        }
        initializeBoundary();

        boolean exit = false;
        while (!exit) {
            displayCharacterSheet();
            displayMenu();
            String choice = prompt("Scegli un'opzione: ");
            switch (choice) {
                case "1":
                    editName();
                    break;
                case "2":
                    editRace();
                    break;
                case "3":
                    editClass();
                    break;
                case "4":
                    editAge();
                    break;
                case "5":
                    editLevel();
                    break;
                case "6":
                    editStrength();
                    break;
                case "7":
                    editDexterity();
                    break;
                case "8":
                    editConstitution();
                    break;
                case "9":
                    editIntelligence();
                    break;
                case "10":
                    editWisdom();
                    break;
                case "11":
                    editCharisma();
                    break;
                case "12":
                    onClickSaveCharacter();
                    exit = true;
                    break;
                case "13":
                    onClickGoBackToList();
                    exit = true;
                    break;
                case "0":
                    jout.print("Operazione annullata. Uscita senza salvare.");
                    exit = true;
                    break;
                default:
                    jout.print("Opzione non valida, riprova.");
                    break;
            }
            jout.print("");
        }
    }

    /**
     * Inizializza la boundary: se currentUser non ha selezionato un personaggio,
     * entra in modalità creazione, altrimenti in modalità modifica.
     */
    private void initializeBoundary() {
        // si suppone che il nome del personaggio sia salvato lì.
        String selected = currentUser.getSelectedLobbyName();
        if (selected == null || selected.isEmpty()) {
            creationMode = true;
            currentBean = new CharacterSheetBean();
            // Inizializza le parti info e stats se non già presenti
            currentBean.setInfoBean(new CharacterInfoBean());
            currentBean.setStatsBean(new CharacterStatsBean());
            oldName = null;
            clearFields();
            jout.print(">>> Modalità creazione attiva.");
        } else {
            creationMode = false;
            // Si suppone che il controller possa individuare il personaggio in base al nome
            currentBean = findCharByName(selected);
            oldName = selected;
            if (currentBean == null) {
                logger.log(Level.SEVERE, "Non ho trovato il personaggio con nome: {0}", selected);
                creationMode = true;
                currentBean = new CharacterSheetBean();
                currentBean.setInfoBean(new CharacterInfoBean());
                currentBean.setStatsBean(new CharacterStatsBean());
                clearFields();
            } else {
                jout.print(">>> Modalità modifica attiva per il personaggio: " + currentBean.getInfoBean().getName());
                populateFields(currentBean);
            }
        }
    }

    /**
     * Visualizza i dati attuali del Character Sheet.
     */
    private void displayCharacterSheet() {
        jout.print("=== Scheda del Personaggio ===");
        jout.print("Nome      : " + (currentBean.getInfoBean().getName() != null ? currentBean.getInfoBean().getName() : ""));
        jout.print("Razza     : " + (currentBean.getInfoBean().getRace() != null ? currentBean.getInfoBean().getRace() : ""));
        jout.print("Classe    : " + (currentBean.getInfoBean().getClasse() != null ? currentBean.getInfoBean().getClasse() : ""));
        jout.print("Età       : " + currentBean.getInfoBean().getAge());
        jout.print("Livello   : " + currentBean.getInfoBean().getLevel());
        jout.print("STR       : " + currentBean.getStatsBean().getStrength());
        jout.print("DEX       : " + currentBean.getStatsBean().getDexterity());
        jout.print("CON       : " + currentBean.getStatsBean().getConstitution());
        jout.print("INT       : " + currentBean.getStatsBean().getIntelligence());
        jout.print("WIS       : " + currentBean.getStatsBean().getWisdom());
        jout.print("CHA       : " + currentBean.getStatsBean().getCharisma());
    }

    /**
     * Visualizza il menu delle operazioni.
     */
    private void displayMenu() {
        jout.print("=== Menu Scheda Personaggio ===");
        jout.print("1. Modifica Nome");
        jout.print("2. Modifica Razza");
        jout.print("3. Modifica Classe");
        jout.print("4. Modifica Età");
        jout.print("5. Modifica Livello");
        jout.print("6. Modifica STR");
        jout.print("7. Modifica DEX");
        jout.print("8. Modifica CON");
        jout.print("9. Modifica INT");
        jout.print("10. Modifica WIS");
        jout.print("11. Modifica CHA");
        jout.print("12. Salva Scheda");
        jout.print("13. Torna alla Lista Personaggi");
        jout.print("0. Esci senza salvare");
    }

    /**
     * Richiede un input all'utente.
     */
    private String prompt(String message) {
        jout.print(message);
        return scanner.nextLine().trim();
    }

    // -------------------- Metodi di modifica dei campi --------------------

    private void editName() {
        String nome = prompt("Inserisci il nuovo nome: ");
        currentBean.getInfoBean().setName(nome);
    }

    private void editRace() {
        String race = prompt("Inserisci la razza: ");
        currentBean.getInfoBean().setRace(race);
    }

    private void editClass() {
        String clazz = prompt("Inserisci la classe: ");
        currentBean.getInfoBean().setClasse(clazz);
    }

    private void editAge() {
        String ageStr = prompt("Inserisci l'età: ");
        currentBean.getInfoBean().setAge(parseIntOrZero(ageStr));
    }

    private void editLevel() {
        String levelStr = prompt("Inserisci il livello: ");
        currentBean.getInfoBean().setLevel(parseIntOrZero(levelStr));
    }

    private void editStrength() {
        String str = prompt("Inserisci STR: ");
        currentBean.getStatsBean().setStrength(parseIntOrZero(str));
    }

    private void editDexterity() {
        String dex = prompt("Inserisci DEX: ");
        currentBean.getStatsBean().setDexterity(parseIntOrZero(dex));
    }

    private void editConstitution() {
        String con = prompt("Inserisci CON: ");
        currentBean.getStatsBean().setConstitution(parseIntOrZero(con));
    }

    private void editIntelligence() {
        String intel = prompt("Inserisci INT: ");
        currentBean.getStatsBean().setIntelligence(parseIntOrZero(intel));
    }

    private void editWisdom() {
        String wis = prompt("Inserisci WIS: ");
        currentBean.getStatsBean().setWisdom(parseIntOrZero(wis));
    }

    private void editCharisma() {
        String cha = prompt("Inserisci CHA: ");
        currentBean.getStatsBean().setCharisma(parseIntOrZero(cha));
    }

    // -------------------- Salvataggio e navigazione --------------------

    /**
     * Esegue la validazione e il salvataggio della scheda del personaggio.
     */
    private void onClickSaveCharacter() {
        // Validazione tramite il controller
        String validationErrors = controller.validate(currentBean);
        if (!validationErrors.isEmpty()) {
            Alert.showError("Errore di Validazione", validationErrors);
            return;
        }

        if (!creationMode) {
            controller.updateChar(oldName, currentBean);
            jout.print("Scheda aggiornata con successo.");
        } else {
            controller.createChar(currentBean);
            jout.print("Scheda creata con successo.");
        }
        // Resetta la selezione e torna alla lista dei personaggi
        currentUser.setSelectedLobbyName(null);
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena verso la lista dei personaggi.", e);
        }
    }

    /**
     * Torna alla lista dei personaggi senza salvare.
     */
    private void onClickGoBackToList() {
        try {
            changeScene(SceneNames.CHARACTER_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena verso la lista dei personaggi.", e);
        }
    }

    /**
     * Simula il cambio scena (in ambiente CLI si limita a stampare un messaggio e delega a SceneSwitcher).
     */
    private void changeScene(String fxml) throws IOException {
        jout.print("Cambio scena verso " + fxml + "...");
        SceneSwitcher.changeScene(null, fxml, currentUser);
    }

    // -------------------- Helper --------------------

    /**
     * Pulisce i campi impostando valori di default.
     */
    private void clearFields() {
        currentBean.getInfoBean().setName("");
        currentBean.getInfoBean().setRace("");
        currentBean.getInfoBean().setClasse("");
        currentBean.getInfoBean().setAge(0);
        currentBean.getInfoBean().setLevel(0);
        currentBean.getStatsBean().setStrength(10);
        currentBean.getStatsBean().setDexterity(10);
        currentBean.getStatsBean().setConstitution(10);
        currentBean.getStatsBean().setIntelligence(10);
        currentBean.getStatsBean().setWisdom(10);
        currentBean.getStatsBean().setCharisma(10);
    }

    /**
     * Popola (visualizza) i campi con i dati del CharacterSheetBean esistente.
     */
    private void populateFields(CharacterSheetBean bean) {
        jout.print("Popolamento dei campi con i dati esistenti:");
        jout.print("Nome      : " + bean.getInfoBean().getName());
        jout.print("Razza     : " + bean.getInfoBean().getRace());
        jout.print("Classe    : " + bean.getInfoBean().getClasse());
        jout.print("Età       : " + bean.getInfoBean().getAge());
        jout.print("Livello   : " + bean.getInfoBean().getLevel());
        jout.print("STR       : " + bean.getStatsBean().getStrength());
        jout.print("DEX       : " + bean.getStatsBean().getDexterity());
        jout.print("CON       : " + bean.getStatsBean().getConstitution());
        jout.print("INT       : " + bean.getStatsBean().getIntelligence());
        jout.print("WIS       : " + bean.getStatsBean().getWisdom());
        jout.print("CHA       : " + bean.getStatsBean().getCharisma());
    }

    /**
     * Prova a convertire una stringa in intero; restituisce 0 se il parsing fallisce.
     */
    private int parseIntOrZero(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            logger.severe(">>> ERRORE: Il valore inserito non è un numero valido: " + input);
            return 0;
        }
    }

    /**
     * Cerca nella lista dei personaggi dell'utente quello con il nome specificato.
     * (In ambiente CLI si suppone che currentUser.getCharacterSheets() contenga già i CharacterSheetBean.)
     */
    private CharacterSheetBean findCharByName(String name) {
        if (currentUser.getCharacterSheets() == null) {
            return null;
        }
        return currentUser.getCharacterSheets().stream()
                .filter(cs -> cs.getInfoBean().getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    // -------------------- Iniezione delle dipendenze --------------------

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (CharacterSheetController) logicController;
    }
}