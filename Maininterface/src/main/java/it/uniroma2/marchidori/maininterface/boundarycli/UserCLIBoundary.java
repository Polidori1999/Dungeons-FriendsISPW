package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.RunInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.UserController;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserCLIBoundary implements UserAwareInterface, ControllerAwareInterface, RunInterface {

    private UserBean currentUser;
    private UserController controller;
    private final Jout jout = new Jout(this.getClass().getSimpleName());
    private static final Logger LOGGER = Logger.getLogger(UserCLIBoundary.class.getName());
    private static final String SWITCHTO = "Switch Role to ";

    /**
     * Metodo principale per eseguire il boundary tramite CLI.
     */
    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Inizializzazione: mostra le informazioni dell'utente corrente (se presenti)
        if (currentUser != null) {
            jout.print("Benvenuto, " + currentUser.getEmail());
            jout.print("Ruolo attuale: " + currentUser.getRoleBehavior().getRoleName());
            displaySwitchRoleOption();
        }

        boolean exit = false;
        while (!exit) {
            showMenu();
            jout.print("Scegli un'opzione: ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    jout.print("Switching Role... from " + currentUser.getRoleBehavior().getRoleName());
                    onClickSwitchRole();
                    jout.print("Role Switched to " + currentUser.getRoleBehavior().getRoleName());
                    break;
                case "2":
                    jout.print("Logging out...");
                    Session.getInstance().clear();
                    changeScene("login.fxml");
                    break;
                case "3":
                    jout.print("Display info Use...");
                    jout.print("email: " + currentUser.getEmail());
                    jout.print("role: " + currentUser.getRoleBehavior().getRoleName());
                    break;
                case "4":
                    jout.print("Go to Home...");
                    exit = true;
                    changeScene("home.fxml");
                    break;
                default:
                    jout.print("Opzione non valida, riprova.");
            }
            jout.print(""); // Riga vuota per separare le iterazioni
        }
        scanner.close();
    }

    /**
     * Mostra il menù principale.
     */
    private void showMenu() {
        jout.print("=== MENU UTENTE (CLI) ===");
        jout.print("1. Action Switch Role");
        jout.print("2. Log Out");
        jout.print("3. Display info User");
        jout.print("4. Go to Home");
    }

    /**
     * Visualizza il testo relativo al cambio ruolo.
     */
    private void displaySwitchRoleOption() {
        if (currentUser != null) {
            if (currentUser.getRoleBehavior() == RoleEnum.PLAYER) {
                jout.print("[" + SWITCHTO + RoleEnum.DM.getRoleName() + "]");
            } else {
                jout.print("[" + SWITCHTO + RoleEnum.PLAYER.getRoleName() + "]");
            }
        }
    }

    /**
     * Simula l'azione del cambio ruolo.
     */
    private void onClickSwitchRole() {
        if (controller != null && currentUser != null) {
            controller.switchRole(currentUser.getRoleBehavior());
            jout.print("Ruolo aggiornato: " + currentUser.getRoleBehavior().getRoleName());
            displaySwitchRoleOption();
            LOGGER.log(Level.INFO, () -> "Ruolo cambiato a: " + currentUser.getRoleBehavior().getRoleName());
            jout.print("ciao mondo!");
        } else {
            jout.print("Operazione non disponibile: controller o utente non inizializzato.");
        }
    }

    /**
     * Simula il cambio scena stampando il nome della scena.
     *
     * @param sceneName il nome della scena verso cui cambiare
     * @throws IOException in caso di errore (simulato)
     */
    private void changeScene(String sceneName) throws IOException {
        // In modalità CLI si può semplicemente stampare un messaggio oppure chiamare un altro boundary
        jout.print("Cambio scena a: " + sceneName);
        if(sceneName.equals(SceneNames.LOGIN)){
            currentUser = null;
        }
        SceneSwitcher.changeScene(null,sceneName,currentUser);
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.controller = (UserController) logicController;
    }
}
