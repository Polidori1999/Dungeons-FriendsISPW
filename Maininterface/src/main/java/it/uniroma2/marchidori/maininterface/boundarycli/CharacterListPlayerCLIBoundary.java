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


    private void menu() {
        jout.print("=== Menu Personaggi ===");
        String[] menuItems = {
                "Modifica un personaggio",
                "Elimina un personaggio",
                "Scarica un personaggio",
                "Crea un nuovo personaggio",
                "Aggiorna lista personaggi",
                "Torna a Home"
        };
        for (int i = 0; i < menuItems.length; i++) {
            jout.print(i + ". " + menuItems[i]);
        }
    }


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

    private void editCharacter(CharacterSheetBean characterSheetBean) throws IOException {
        // Imposta il nome del personaggio selezionato (usato per individuare il bean nell'editor)
        currentUser.setSelectedLobbyName(characterSheetBean.getInfoBean().getName());
        jout.print("Modifica del personaggio '" + characterSheetBean.getInfoBean().getName() + "'.");
        changeScene(SceneNames.CHARACTER_SHEET);
    }

    private void handleNewCharacter() throws IOException {
        currentUser.setSelectedLobbyName(null);
        jout.print("Creazione di un nuovo personaggio.");
        changeScene(SceneNames.CHARACTER_SHEET);
    }
}
