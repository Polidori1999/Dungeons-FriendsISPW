package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.utils.CustomTimer;
import javafx.application.Platform;
import javafx.scene.control.Label;


public class TimerController {


    private CustomTimer timer;

    // Crea un nuovo TimerController associato a una Label e a una durata e un azione
    public TimerController(Label timerLabel, int durationInSeconds, Runnable onFinishAction) {
        this.timer = new CustomTimer(durationInSeconds, new CustomTimer.TimerListener() {
            @Override
            public void onTick(int secondsRemaining) {
                // Esegue l'aggiornamento dell'interfaccia grafica (Label)
                Platform.runLater(() -> timerLabel.setText(secondsRemaining + "s"));
            }

            @Override
            public void onFinish() {
                //Quando il timer raggiunge 0 aggiorna la Label
                Platform.runLater(() -> {
                    timerLabel.setText("0s");
                    if (onFinishAction != null) {
                        onFinishAction.run();
                    }
                });
            }
        });
    }

    public void start() {
        if (timer != null) {
            timer.start();
        }
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
        }
    }

}
