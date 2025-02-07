package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.utils.CustomTimer;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Gestisce il countdown basato su CustomTimer aggiornando la Label.
 */
public class TimerController {


    private CustomTimer timer;

    /**
     * @param timerLabel       Label da aggiornare con il countdown.
     * @param durationInSeconds Durata del timer in secondi.
     * @param onFinishAction   Azione da eseguire quando il timer scade.
     */
    public TimerController(Label timerLabel, int durationInSeconds, Runnable onFinishAction) {
        this.timer = new CustomTimer(durationInSeconds, new CustomTimer.TimerListener() {
            @Override
            public void onTick(int secondsRemaining) {
                Platform.runLater(() -> timerLabel.setText(secondsRemaining + "s"));
            }

            @Override
            public void onFinish() {
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
