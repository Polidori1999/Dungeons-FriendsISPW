package it.uniroma2.marchidori.maininterface.utils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;


public class CustomTimer {

    private int seconds;
    private final int initialSeconds;
    private Timeline timeline;
    private TimerListener listener;

    public interface TimerListener {
        void onTick(int secondsRemaining);
        void onFinish();
    }


    public CustomTimer(int seconds, TimerListener listener) {
        this.initialSeconds = seconds;
        this.seconds = seconds;
        this.listener = listener;
    }
    //Avvia il timer. Se era già in corso, lo interrompe e lo riavvia
    public void start() {
        if (timeline != null) {
            timeline.stop();
        }
        // Crea una nuova Timeline, con un KeyFrame ogni 1 secondo
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            seconds--;
            Platform.runLater(() -> {
                if (listener != null) {
                    listener.onTick(seconds);
                }
            });
            //se =0 ferma il timer e chiama onfinish
            if (seconds <= 0) {
                timeline.stop();
                Platform.runLater(() -> {
                    if (listener != null) {
                        listener.onFinish();
                    }
                });
            }
        }));
        //loop per il timer
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    //ferma il timer
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void reset() {
        stop();
        seconds = initialSeconds;
    }

    public int getSeconds() {
        return seconds;
    }
}
