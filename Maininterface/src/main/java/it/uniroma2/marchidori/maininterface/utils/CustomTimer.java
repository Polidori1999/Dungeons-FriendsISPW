package it.uniroma2.marchidori.maininterface.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

/**
 * CustomTimer incapsula la logica di un timer basato su Timeline.
 * Permette di impostare un conto alla rovescia (in secondi) e di ricevere notifiche
 * ad ogni tick e quando il timer raggiunge 0.
 */
public class CustomTimer {

    private int seconds;
    private final int initialSeconds;
    private Timeline timeline;
    private TimerListener listener;

    /**
     * Interfaccia per ricevere notifiche dal timer.
     */
    public interface TimerListener {
        /**
         * Viene chiamato ad ogni tick (ogni secondo).
         *
         * @param secondsRemaining i secondi rimanenti
         */
        void onTick(int secondsRemaining);

        /**
         * Viene chiamato quando il timer raggiunge 0.
         */
        void onFinish();
    }

    /**
     * Costruttore.
     *
     * @param seconds  il numero di secondi iniziali per il timer
     * @param listener il listener per ricevere le notifiche
     */
    public CustomTimer(int seconds, TimerListener listener) {
        this.initialSeconds = seconds;
        this.seconds = seconds;
        this.listener = listener;
    }

    /**
     * Avvia il timer. Il KeyFrame viene eseguito ogni secondo finché il timer non raggiunge 0.
     */
    public void start() {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            seconds--;
            // Assicuriamoci di aggiornare il listener sul thread JavaFX.
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

    /**
     * Ferma il timer.
     */
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * Resetta il timer al valore iniziale.
     */
    public void reset() {
        stop();
        seconds = initialSeconds;
    }

    /**
     * Restituisce i secondi rimanenti.
     *
     * @return i secondi rimanenti
     */
    public int getSeconds() {
        return seconds;
    }
}