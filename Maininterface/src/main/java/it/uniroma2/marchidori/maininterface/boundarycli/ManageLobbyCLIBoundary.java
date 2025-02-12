package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.LobbyBean;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ManageLobbyController;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.factory.LobbyFactory;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.Alert;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageLobbyCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    private static final Logger LOGGER = Logger.getLogger(ManageLobbyCLIBoundary.class.getName());

    private UserBean currentUser;
    private ManageLobbyController controller;

    // Modalità: true = creazione nuova lobby, false = modifica di una lobby esistente
    private boolean creationMode;
    // Bean della lobby in creazione/modifica
    private LobbyBean currentBean;
    // Vecchio nome della lobby, utile per l’update
    private String oldName;

    private final Scanner scanner = new Scanner(System.in);
    private final Jout jout = new Jout("ManageLobbyCLIBoundary");

    @Override
    public void run() throws IOException {
        if (currentUser == null) {
            jout.print("ERRORE: Utente non inizializzato.");
            return;
        }
        // Inizializza la boundary (simile all'initialize() della versione JavaFX)
        initializeBoundary();

        boolean exit = false;
        while (!exit) {
            displayCurrentLobbyInfo();
            displayMenu();
            String choice = prompt("Scegli un'opzione: ");
            switch (choice) {
                case "1":
                    editLobbyName();
                    break;
                case "2":
                    editLiveOnline();
                    break;
                case "3":
                    editMaxPlayers();
                    break;
                case "4":
                    editDuration();
                    break;
                case "5":
                    onClickSaveLobby();
                    exit = true;
                    break;
                case "6":
                    onClickGoBackToListOfLobbies();
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
     * Simula l'inizializzazione della boundary.
     * Se currentUser ha un lobby selezionata, entra in modalità modifica;
     * altrimenti, in modalità creazione.
     */
    private void initializeBoundary() {
        String selected = currentUser.getSelectedLobbyName();
        if (selected == null || selected.isEmpty()) {
            creationMode = true;
            currentBean = LobbyFactory.createBean();
            oldName = null;
            clearFields();
            jout.print("Modalità creazione attiva.");
        } else {
            creationMode = false;
            currentBean = controller.findLobbyByName(selected, currentUser.getJoinedLobbies());
            oldName = selected;
            if (currentBean == null) {
                LOGGER.log(Level.SEVERE, "Non ho trovato la lobby con nome: {0}", selected);
                creationMode = true;
                currentBean = LobbyFactory.createBean();
                clearFields();
            } else {
                jout.print("Modalità modifica attiva per lobby: " + currentBean.getName());
                populateFields(currentBean);
            }
        }
    }

    /**
     * Visualizza le informazioni attuali della lobby.
     */
    private void displayCurrentLobbyInfo() {
        jout.print("=== Dettagli Lobby ===");
        jout.print("Nome           : " + (currentBean.getName() != null ? currentBean.getName() : ""));
        jout.print("Live/Online    : " + (currentBean.getLiveOnline() != null ? currentBean.getLiveOnline() : ""));
        jout.print("Max Giocatori  : " + currentBean.getNumberOfPlayers());
        jout.print("Durata         : " + (currentBean.getDuration() != null ? currentBean.getDuration() : ""));
    }

    /**
     * Mostra il menu per modificare o salvare la lobby.
     */
    private void displayMenu() {
        jout.print("=== Menu Gestione Lobby ===");
        jout.print("1. Modifica Nome Lobby");
        jout.print("2. Modifica Live/Online");
        jout.print("3. Modifica Numero Giocatori");
        jout.print("4. Modifica Durata");
        jout.print("5. Salva Lobby");
        jout.print("6. Torna alla Lista delle Lobby");
        jout.print("0. Esci senza salvare");
    }

    /**
     * Metodo di utilità per richiedere un input all'utente.
     */
    private String prompt(String message) {
        jout.print(message);
        return scanner.nextLine().trim();
    }

    // -------------------- Metodi per la modifica dei campi --------------------

    private void editLobbyName() {
        String nome = prompt("Inserisci il nuovo nome della lobby: ");
        currentBean.setName(nome);
    }

    private void editLiveOnline() {
        String liveOnline = prompt("Inserisci 'Live' o 'Online': ");
        currentBean.setLiveOnline(liveOnline);
    }

    private void editMaxPlayers() {
        String maxPlayers = prompt("Inserisci il numero massimo di giocatori: ");
        int num = parseIntOrZero(maxPlayers);
        currentBean.setNumberOfPlayers(num);
    }

    private void editDuration() {
        String duration = prompt("Inserisci la durata della lobby (es. One-Shot o Campaign): ");
        currentBean.setDuration(duration);
    }

    /**
     * Converte una stringa in intero, restituendo 0 in caso di input nullo o non numerico.
     */
    private int parseIntOrZero(String input) {
        if (input == null || input.trim().isEmpty()) {
            LOGGER.log(Level.SEVERE, "Il campo per il numero di giocatori è vuoto o nullo.");
            return 0;
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Valore non numerico: {0}", input);
            return 0;
        }
    }

    // -------------------- Metodi per gestire il salvataggio e la navigazione --------------------

    /**
     * Simula il salvataggio della lobby.
     */
    private void onClickSaveLobby() {
        // Legge i dati già impostati (i metodi edit hanno aggiornato il bean)
        // Validazione del bean tramite il controller
        String validationErrors = controller.validate(currentBean);
        if (!validationErrors.isEmpty()) {
            Alert.showError("Errore di Validazione", validationErrors);
            return;
        }

        if (!creationMode) {
            controller.updateLobby(oldName, currentBean);
            jout.print("Lobby aggiornata con successo.");
        } else {
            controller.createLobby(currentBean);
            jout.print("Lobby creata con successo.");
        }
        // Dopo il salvataggio, simula il cambio scena verso la lista delle lobby
        try {
            changeScene(SceneNames.MANAGE_LOBBY_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena verso la lista delle lobby.", e);
        }
    }

    /**
     * Simula il ritorno alla lista delle lobby senza salvare.
     */
    private void onClickGoBackToListOfLobbies() {
        try {
            changeScene(SceneNames.MANAGE_LOBBY_LIST);
        } catch (IOException e) {
            throw new SceneChangeException("Errore nel cambio scena dalla gestione lobby alla lista delle lobby.", e);
        }
    }

    /**
     * Simula il cambio scena in ambiente CLI.
     */
    private void changeScene(String fxml) throws IOException {
        jout.print("Cambio scena verso " + fxml + "...");
        SceneSwitcher.changeScene(null, fxml, currentUser);
    }

    // -------------------- Metodi di supporto per la gestione dei campi --------------------

    /**
     * Simula la pulizia dei campi (per la modalità creazione).
     */
    private void clearFields() {
        currentBean.setName("");
        currentBean.setLiveOnline("");
        currentBean.setNumberOfPlayers(0);
        currentBean.setDuration("");
    }

    /**
     * Simula il popolamento dei campi con i dati di un bean esistente.
     */
    private void populateFields(LobbyBean bean) {
        jout.print("Popolamento dei campi con i dati della lobby esistente:");
        jout.print("Nome           : " + bean.getName());
        jout.print("Live/Online    : " + bean.getLiveOnline());
        jout.print("Max Giocatori  : " + bean.getNumberOfPlayers());
        jout.print("Durata         : " + bean.getDuration());
    }

    // -------------------- Iniezione delle dipendenze --------------------

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (ManageLobbyController) logicController;
    }
}
