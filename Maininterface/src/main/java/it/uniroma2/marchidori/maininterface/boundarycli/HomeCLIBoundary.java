package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.Converter;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;

import java.io.IOException;
import java.util.Scanner;

public class HomeCLIBoundary implements UserAwareInterface, RunInterface {


    private UserBean currentUser = Converter.convert(Session.getInstance().getCurrentUser());
    private final Jout jout = new Jout(this.getClass().getSimpleName());


    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        jout.print("Benvenuto nella schermata HOME (CLI) per: " + currentUser.getEmail());
        while (!exit) {
            mostraMenu();
            if (!scanner.hasNextLine()) {
                jout.print("Input terminato. Uscita dalla modalità CLI.");
                break;
            }
            jout.print("Scegli un'opzione: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    // Avvia la schermata di Consult Rules
                    jout.print("Apertura Consult Rules...");
                    changeScene("consultRules.fxml");
                    break;
                case "2":
                    jout.print("Apertura Join Lobby...");
                    // Logica per partecipare a una lobby
                    changeScene("joinLobby.fxml");
                    break;
                case "3":
                    jout.print("Apertura Manage Lobby...");
                    if (currentUser != null) {
                        currentUser.setSelectedLobbyName(null);
                        changeScene("manageLobbyList.fxml");
                        jout.print("Reset selectedLobbyName per: " + currentUser.getEmail());
                    }
                    break;
                case "4":
                    jout.print("Apertura Character List...");
                    changeScene("characterList.fxml");
                    // Logica per mostrare il personaggio
                    break;
                case "5":
                    jout.print("Visualizzazione dati utente...");
                    changeScene("user.fxml");
                    // Puoi eventualmente rinfrescare la schermata o eseguire altre operazioni
                    break;
                case "6":
                    jout.print("Uscita dalla schermata HOME, terminazione programma...");
                    exit = true;
                    System.exit(0);
                    break;
                default:
                    jout.print("Opzione non valida, riprova.");
            }
            jout.print("");
        }
    }

    private void mostraMenu() {
        jout.print("=== MENU HOME ===");
        jout.print("1. Consult Rules");
        jout.print("2. Join Lobby");
        jout.print("3. Manage Lobby");
        jout.print("4. My Character");
        jout.print("5. Go To User");
        jout.print("6. Termina il programma");
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    private void changeScene(String sceneName) throws IOException {
        jout.print("Cambio scena verso: " + sceneName);
        SceneSwitcher.changeScene(null, sceneName, currentUser);
    }
}
