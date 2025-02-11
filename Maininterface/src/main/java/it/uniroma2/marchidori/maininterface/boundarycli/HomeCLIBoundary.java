package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.ConsultRulesController; // Assicurati che il package sia corretto
import it.uniroma2.marchidori.maininterface.control.UserController;

import java.io.IOException;
import java.util.Scanner;

public class HomeCLIBoundary implements UserAwareInterface {

    private UserBean currentUser;
    private Jout jout = new Jout(this.getClass().getSimpleName());

    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        jout.print("Benvenuto nella schermata HOME (CLI) per: " + currentUser.getEmail());
        while (!exit) {
            mostraMenu();
            jout.print("Scegli un'opzione: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    // Avvia la schermata di Consult Rules
                    jout.print("Apertura Consult Rules...");
                    ConsultRulesCLIBoundary consultRulesBoundary = new ConsultRulesCLIBoundary();
                    consultRulesBoundary.setCurrentUser(currentUser);
                    // Imposto il controller per consultare i manuali (assumendo un costruttore predefinito)
                    consultRulesBoundary.setLogicController(new ConsultRulesController());
                    consultRulesBoundary.run();
                    break;
                case "2":
                    jout.print("Partecipazione alla Lobby...");
                    // Logica per partecipare a una lobby
                    break;
                case "3":
                    jout.print("Gestione della Lobby...");
                    if (currentUser != null) {
                        currentUser.setSelectedLobbyName(null);
                        jout.print("Reset selectedLobbyName per: " + currentUser.getEmail());
                    }
                    break;
                case "4":
                    jout.print("Visualizzazione del personaggio...");
                    // Logica per mostrare il personaggio
                    break;
                case "5":
                    jout.print("Visualizzazione dati utente...");
                    UserCLIBoundary userBoundary = new UserCLIBoundary();
                    userBoundary.setCurrentUser(currentUser);
                    // Imposto il controller per consultare i manuali (assumendo un costruttore predefinito)
                    userBoundary.setLogicController(new UserController());
                    userBoundary.run();
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
        scanner.close();
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
}
