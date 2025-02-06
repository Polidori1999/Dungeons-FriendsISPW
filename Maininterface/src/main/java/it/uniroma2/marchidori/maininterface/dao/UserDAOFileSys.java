package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class UserDAOFileSys implements UserDAO {
    private static final String DIRECTORY_PATH = "src/main/java/it/uniroma2/marchidori/maininterface/userps";
    private static final String FILE_PATH = DIRECTORY_PATH + "/users.txt";

    public UserDAOFileSys() {
        createDirectory(); // Assicura che la cartella esista
    }

    private void createDirectory() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Cartella creata: " + DIRECTORY_PATH);
            } else {
                System.err.println("Errore nella creazione della cartella: " + DIRECTORY_PATH);
            }
        }
    }


    public void saveUser(String email, String password) {
        System.out.println("🔍 Chiamato saveUser() con: " + email);
        File file = new File(FILE_PATH);

        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println("File creato: " + FILE_PATH);
                } else {
                    System.err.println("Errore nella creazione del file");
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Errore nella creazione del file: " + e.getMessage());
            return; // Interrompe il flusso in caso di errore
        }

        System.out.println("\n📌 Tentativo di scrittura dati...");
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(email + "," + password);
            System.out.println("Utente salvato: " + email + ", psw:" + password);

        } catch (IOException e) {
            System.err.println("❌ Errore nella scrittura del file: " + e.getMessage());
        }
    }

    //questo va in un controller

}
