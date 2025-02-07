package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import java.io.*;
import java.util.ArrayList;

public class UserDAOFileSys implements UserDAO {
    private static final String DIRECTORY_PATH = "src/main/java/it/uniroma2/marchidori/maininterface/userpsw";
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

    // Metodo per salvare l'utente (rimane invariato)
    public void saveUser(String email, String password) {
        // Cripta la password con BCrypt
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
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
            return;
        }

        System.out.println("\n📌 Tentativo di scrittura dati...");
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            // Salva: email, password hashata
            out.println(email + "," + hashedPassword);
            System.out.println("Utente salvato: " + email + ", psw:" + hashedPassword);

        } catch (IOException e) {
            System.err.println("❌ Errore nella scrittura del file: " + e.getMessage());
        }
    }

    // Metodo per recuperare l'utente in base all'email (il DAO NON verifica la password)
    public UserBean getUserByEmail(String email) {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.err.println("❌ File non trovato: " + FILE_PATH);
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                System.out.println("📄 Lettura riga dal file: " + line);
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(email)) {
                    // Qui creiamo un UserBean che contiene anche la password hashata.
                    // Per questo, aggiungiamo un campo "password" a UserBean (o usiamo un costruttore dedicato).
                    return new UserBean(
                            String.valueOf(lineNumber),
                            email,
                            parts[1],          // password hashata
                            RoleEnum.PLAYER,   // Ruolo di default (puoi modificarlo se necessario)
                            new ArrayList<>(), // joinedLobbies vuota
                            new ArrayList<>(), // favouriteLobbies vuota
                            new ArrayList<>()  // characterSheets vuota
                    );
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Errore nella lettura del file: " + e.getMessage());
        }
        return null;
    }

}
