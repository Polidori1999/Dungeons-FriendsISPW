package it.uniroma2.marchidori.maininterface;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main extends Application {

    static UserBean tempUser = new UserBean("", null, null, null);
    private final Jout jout = new Jout("Main");

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Start the JavaFX scene using the LOGIN FXML
        SceneSwitcher.changeScene(primaryStage, SceneNames.LOGIN, tempUser);
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // -------------------------------
        // DATA SOURCE SELECTION MECHANISM
        // -------------------------------
        boolean validDataSourceSelection = false;
        while (!validDataSourceSelection) {
            System.out.println("Select the data source option:");
            System.out.println("1. DAO Database");
            System.out.println("2. DAO FileSystem");
            System.out.print("Enter the corresponding number (1 or 2): ");
            String dataSourceChoice = scanner.nextLine().trim();

            if ("1".equals(dataSourceChoice)) {
                System.out.println("DAO Database selected.");
                // Set your flag to true (for using the database)
                Session.getInstance().setDB(true);
                validDataSourceSelection = true;
            } else if ("2".equals(dataSourceChoice)) {
                System.out.println("DAO FileSystem selected.");
                // Set your flag to false (for using the filesystem)
                Session.getInstance().setDB(false);
                validDataSourceSelection = true;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }

        // ----------------------------------
        // EXECUTION MODE SELECTION (CLI/JavaFX)
        // ----------------------------------
        boolean exit = true;
        while (exit) {
            System.out.println("Select the execution mode of the application:");
            System.out.println("1. CLI");
            System.out.println("2. JavaFX");
            System.out.print("Enter the corresponding number (1 or 2): ");
            String modeChoice = scanner.nextLine().trim();
            if ("1".equals(modeChoice)) {
                System.out.println("Launching CLI mode...");
                exit = false;
                Session.getInstance().setCLI(true);
                SceneSwitcher.changeScene(null, SceneNames.LOGIN, tempUser);
            } else if ("2".equals(modeChoice)) {
                System.out.println("Launching JavaFX mode...");
                exit = false;
                launch(args);
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
