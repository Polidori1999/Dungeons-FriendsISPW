package it.uniroma2.marchidori.maininterface.boundarycli;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.ControllerAwareInterface;
import it.uniroma2.marchidori.maininterface.boundary.UserAwareInterface;
import it.uniroma2.marchidori.maininterface.control.RegisterController;
import it.uniroma2.marchidori.maininterface.exception.AccountAlreadyExistsException;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;

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
            changeScene(SceneNames.REGISTER);
            return;
        }

        // Verifica che le password coincidano
        if (!password.equals(confirmPassword)) {
            jout.print("Errore: Le password non coincidono!");
            changeScene(SceneNames.REGISTER);
            return;
        }

        // Esegue la registrazione chiamando il controller
        //registerController.register(email, password);
        try {
            registerController.register(email, password);
            changeScene(SceneNames.LOGIN);
        } catch (AccountAlreadyExistsException e) {
            jout.print("Errore: Email already exists!");
        }
        jout.print("Registrazione completata per l'email: " + email);

        // Dopo la registrazione, passa alla schermata di login
        //changeScene(SceneNames.LOGIN);
    }

    private void changeScene(String sceneName) throws IOException {
        jout.print("Cambio scena verso: " + sceneName);
        SceneSwitcher.changeScene(null, sceneName, currentUser);
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
