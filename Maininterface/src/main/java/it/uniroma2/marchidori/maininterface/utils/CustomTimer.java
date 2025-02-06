package it.uniroma2.marchidori.maininterface.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

/**
 * Incapsula la logica di un timer basato su Timeline.
 */
public class CustomTimer {


    private int seconds;
    private final int initialSeconds;
    private Timeline timeline;
    private TimerListener listener;

    public interface TimerListener {
        void onTick(int secondsRemaining);
        void onFinish();
    }

    /**
     * @param seconds  il numero di secondi iniziali.
     * @param listener listener per ricevere aggiornamenti.
     */
    public CustomTimer(int seconds, TimerListener listener) {
        this.initialSeconds = seconds;
        this.seconds = seconds;
        this.listener = listener;
    }

    public void start() {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            seconds--;
            Platform.runLater(() -> {
                if (listener != null) {
                    listener.onTick(seconds);
                }
            });
            if (seconds <= 0) {
                timeline.stop();
                Platform.runLater(() -> {
                    if (listener != null) {
                        listener.onFinish();
                    }
                });
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

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
