package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.LoginController;
import it.uniroma2.marchidori.maininterface.control.RegisterController;

import java.io.IOException;
import java.util.Scanner;

public class RegisterCLIBoundary implements UserAwareInterface, ControllerAwareInterface {

    private UserBean currentUser;
    private RegisterController registerController;
    private Jout jout = new Jout(this.getClass().getSimpleName());

    /**
     * Avvia il processo di registrazione in modalità CLI.
     */
    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        jout.print("=== SCHERMATA DI REGISTRAZIONE ===");

        // Chiede l'email
        jout.print("Inserisci email: ");
        String email = scanner.nextLine().trim();

        // Chiede la password
        jout.print("Inserisci password: ");
        String password = scanner.nextLine().trim();

        // Chiede la conferma della password
        jout.print("Conferma password: ");
        String confirmPassword = scanner.nextLine().trim();

        // Verifica che tutti i campi siano stati compilati
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            jout.print("Errore: Tutti i campi devono essere compilati!");
            // Opzionalmente si potrebbe richiedere un nuovo inserimento
            return;
        }

        // Verifica che le password coincidano
        if (!password.equals(confirmPassword)) {
            jout.print("Errore: Le password non coincidono!");
            // Opzionalmente si potrebbe richiedere un nuovo inserimento
            return;
        }

        // Esegue la registrazione chiamando il controller
        registerController.register(email, password);
        jout.print("Registrazione completata per l'email: " + email);

        // Dopo la registrazione, passa alla schermata di login
        changeScene("LOGIN");
    }

    /**
     * Metodo per effettuare il cambio scena.
     *
     * @param sceneName Il nome della scena verso cui cambiare (ad es. "LOGIN")
     */
    private void changeScene(String sceneName) throws IOException {
        jout.print("Cambio scena verso: " + sceneName);
        if ("LOGIN".equalsIgnoreCase(sceneName)) {
            LoginCLIBoundary loginBoundary = new LoginCLIBoundary();
            // Per il login non abbiamo ancora un utente registrato, quindi si passa null o un UserBean vuoto
            loginBoundary.setCurrentUser(null);
            // Imposta il controller del login se necessario (dipende dalla tua implementazione)
            loginBoundary.setLogicController(new LoginController());
            loginBoundary.run();
        } else {
            jout.print("Scena non riconosciuta.");
        }
    }

    @Override
    public void setCurrentUser(UserBean user) {
        this.currentUser = user;
    }

    @Override
    public void setLogicController(Object logicController) {
        this.registerController = (RegisterController) logicController;
    }
}
