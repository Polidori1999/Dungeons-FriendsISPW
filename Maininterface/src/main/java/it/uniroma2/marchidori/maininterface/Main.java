package it.uniroma2.marchidori.maininterface;

import it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher;
import it.uniroma2.marchidori.maininterface.utils.SceneNames;
import javafx.application.Application;

import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneSwitcher.changeScene(primaryStage, SceneNames.LOGIN, null);
    }


    public static void main(String[] args) {
        launch();
    }
}