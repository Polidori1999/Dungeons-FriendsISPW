package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.Jout;
import it.uniroma2.marchidori.maininterface.bean.charactersheetb.CharacterSheetBean;
import it.uniroma2.marchidori.maininterface.utils.CharacterSheetDownloadTask;

import java.nio.file.Paths;

public class CharacterSheetDownloadController {
    private final Jout jout = new Jout("CharacterSheetDownloadController");

    public CharacterSheetDownloadTask getDownloadTask(CharacterSheetBean bean) {
        try {
            // Ottieni la cartella di download (ad es. Downloads dell'utente)
            String userHome = System.getProperty("user.home");
            String downloadFolder = Paths.get(userHome, "Downloads").toString();
            // Crea il nome del file
            String fileName = "character_" + bean.getInfoBean().getName() + ".txt";
            String destinationPath = Paths.get(downloadFolder, fileName).toString();

            jout.print("Percorso di download: " + destinationPath);
            // Crea e restituisci il task di download
            return new CharacterSheetDownloadTask(bean, destinationPath);
        } catch (Exception e) {
            jout.print("Errore durante la preparazione del task di download: " + e.getMessage());
            return null;
        }
    }
}
