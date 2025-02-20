package it.uniroma2.marchidori.maininterface.utils;

import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import javafx.concurrent.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


//Simula il download di un file partendo da un CharacterSheetBean.
public class CharacterSheetDownloadTask extends Task<Void> {


    private final CharacterSheetBean bean;
    private final String destinationPath;


    public CharacterSheetDownloadTask(CharacterSheetBean bean, String destinationPath) {
        this.bean = bean;
        this.destinationPath = destinationPath;
    }

    @Override
    protected Void call() throws Exception {
        // Simula il download in totalSteps passi, con una pausa di 50 ms per passo.
        // numero di "passi" simulati per il download
        int totalSteps = 50;
        for (int i = 1; i <= totalSteps; i++) {
            if (isCancelled()) {
                break;
            }
            // Aggiorna il progresso
            updateProgress(i, totalSteps);
            // Simula il tempo di download (50 ms * 100 = circa 5 secondi)
            Thread.sleep(50);
        }

        // Al termine della simulazione, scrivi i dati del bean in un file di testo.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destinationPath))) {
            writer.write("CharacterSheetBean:\n");
            if (bean.getInfoBean() != null) {
                writer.write("Name: " + bean.getInfoBean().getName() + "\n");
                writer.write("Class: " + bean.getInfoBean().getClasse() + "\n");
                writer.write("Level: " + bean.getInfoBean().getLevel() + "\n");
                writer.write("Race: " + bean.getInfoBean().getRace() + "\n");
                writer.write("Age: " + bean.getInfoBean().getAge() + "\n");
            } else {
                writer.write("CharacterInfoBean: NULL\n");
            }
            if (bean.getStatsBean() != null) {
                writer.write("Strength: " + bean.getStatsBean().getStrength() + "\n");
                writer.write("Dexterity: " + bean.getStatsBean().getDexterity() + "\n");
                writer.write("Constitution: " + bean.getStatsBean().getConstitution() + "\n");
                writer.write("Intelligence: " + bean.getStatsBean().getIntelligence() + "\n");
                writer.write("Wisdom: " + bean.getStatsBean().getWisdom() + "\n");
                writer.write("Charisma: " + bean.getStatsBean().getCharisma() + "\n");
            } else {
                writer.write("CharacterStatsBean: NULL\n");
            }
        } catch (IOException e) {
            // Propaga l'eccezione se il salvataggio fallisce
            throw new IOException("Errore durante la scrittura del file: " + e.getMessage(), e);
        }

        return null;
    }
}
