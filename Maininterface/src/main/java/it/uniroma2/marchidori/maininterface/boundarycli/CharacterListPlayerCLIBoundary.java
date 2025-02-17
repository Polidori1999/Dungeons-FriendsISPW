package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.exception.SceneChangeException;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

import java.io.IOException;


public class CharacterListPlayerCLIBoundary extends CharacterListDMCLIBoundary {

    private final Jout jout = new Jout("CharacterListPlayerCLIBoundary");

    @Override
    public void run() throws IOException {
        if (currentUser == null) {
            jout.print("ERRORE: Utente non inizializzato.");
            return;
        }
        refreshTable();
        boolean exit = false;
        while (!exit) {
            displayCharacterList();
            menu();
            String input = prompt("Scegli un'opzione: ");
            exit = manageInput(input);
            jout.print(""); // Riga vuota per separare le iterazioni
        }
        changeScene(SceneNames.HOME);
    }

    /**
     * Visualizza il menu delle operazioni disponibili.
     */
    private void menu() {
        jout.print("=== Menu Personaggi ===");
        jout.print("1. Modifica un personaggio");
        jout.print("2. Elimina un personaggio");
        jout.print("3. Scarica un personaggio");
        jout.print("4. Crea un nuovo personaggio");
        jout.print("5. Aggiorna lista personaggi");
        jout.print("0. Torna a Home");
    }

    /**
     * Elabora la scelta dell'utente e richiama la relativa operazione.
     *
     * @param input La scelta inserita dall'utente.
     * @return true se si vuole uscire dalla modalità, false altrimenti.
     * @throws IOException in caso di errori nel cambio scena.
     */
    private boolean manageInput(String input) throws IOException {
        switch (input) {
            case "1":
                handleEditCharacter();
                break;
            case "2":
                handleDeleteCharacter();
                break;
            case "3":
                handleDownloadCharacter();
                break;
            case "4":
                handleNewCharacter();
                break;
            case "5":
                refreshTable();
                jout.print("Lista aggiornata.");
                break;
            case "0":
                return true;
            default:
                jout.print("Opzione non valida, riprova.");
        }
        return false;
    }




    /**
     * Gestisce la modifica di un personaggio selezionato.
     */
    private void handleEditCharacter() {
        handleCharacterSelection("Nessun personaggio da modificare.",
                "Inserisci il numero del personaggio da modificare: ",
                characterSheetBean -> {
                    try {
                        editCharacter(characterSheetBean);
                    } catch (IOException e) {
                        throw new SceneChangeException("Errore durante l'editing del personaggio", e);
                    }
                });
    }

    /**
     * Imposta il personaggio da modificare e simula il cambio scena verso l'editor.
     */
    private void editCharacter(CharacterSheetBean characterSheetBean) throws IOException {
        // Imposta il nome del personaggio selezionato (usato per individuare il bean nell'editor)
        currentUser.setSelectedLobbyName(characterSheetBean.getInfoBean().getName());
        jout.print("Modifica del personaggio '" + characterSheetBean.getInfoBean().getName() + "'.");
        changeScene(SceneNames.CHARACTER_SHEET);
    }

    /**
     * Gestisce la creazione di un nuovo personaggio.
     */
    private void handleNewCharacter() throws IOException {
        currentUser.setSelectedLobbyName(null);
        jout.print("Creazione di un nuovo personaggio.");
        changeScene(SceneNames.CHARACTER_SHEET);
    }
}
