package it.uniroma2.marchidori.maininterface;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {

    static UserBean tempUser = new UserBean("", null, null, null);
    private static final Jout jout = new Jout("Main");
    private static final String choice = "Enter the corresponding number (1 or 2): ";

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Avvio della scena JavaFX utilizzando il file LOGIN
        SceneSwitcher.changeScene(primaryStage, SceneNames.LOGIN, tempUser);
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // 1. Seleziona la modalità applicazione (Demo / Non-Demo)
        boolean isDemo = chooseAppMode(scanner);
        Session.getInstance().setDemo(isDemo);

        // 2. Se la modalità non è demo, chiedi la scelta della data source
        if (!isDemo) {
            chooseDataSource(scanner);
        }

        // 3. Seleziona la modalità di esecuzione (CLI / JavaFX)
        chooseExecutionMode(scanner, args);
    }

    private static boolean chooseAppMode(Scanner scanner) {
        while (true) {
            jout.print("Select application mode:");
            jout.print("1. Demo Mode");
            jout.print("2. Non-Demo Mode");
            jout.print(choice);
            String appModeChoice = scanner.nextLine().trim();
            if ("1".equals(appModeChoice)) {
                jout.print("Demo Mode selected.");
                return true;
            } else if ("2".equals(appModeChoice)) {
                jout.print("Non-Demo Mode selected.");
                return false;
            } else {
                jout.print("Invalid input. Please try again.");
            }
        }
    }

    private static void chooseDataSource(Scanner scanner) {
        while (true) {
            jout.print("Select the data source option:");
            jout.print("1. DAO Database");
            jout.print("2. DAO FileSystem");
            jout.print(choice);
            String dataSourceChoice = scanner.nextLine().trim();
            if ("1".equals(dataSourceChoice)) {
                jout.print("DAO Database selected.");
                Session.getInstance().setDB(true);
                return;
            } else if ("2".equals(dataSourceChoice)) {
                jout.print("DAO FileSystem selected.");
                Session.getInstance().setDB(false);
                return;
            } else {
                jout.print("Invalid input. Please try again.");
            }
        }
    }

    private static void chooseExecutionMode(Scanner scanner, String[] args) throws IOException {
        while (true) {
            jout.print("Select the execution mode of the application:");
            jout.print("1. CLI");
            jout.print("2. JavaFX");
            jout.print(choice);
            String modeChoice = scanner.nextLine().trim();
            if ("1".equals(modeChoice)) {
                jout.print("Launching CLI mode...");
                Session.getInstance().setCLI(true);
                SceneSwitcher.changeScene(null, SceneNames.LOGIN, tempUser);
                return;
            } else if ("2".equals(modeChoice)) {
                jout.print("Launching JavaFX mode...");
                Session.getInstance().setCLI(false);
                launch(args);
                return;
            } else {
                jout.print("Invalid choice. Please try again.");
            }
        }
    }
}