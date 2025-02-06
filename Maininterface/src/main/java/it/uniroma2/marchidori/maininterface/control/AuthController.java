package it.uniroma2.marchidori.maininterface.control;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.entity.Session;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.UUID;



public class AuthController {
    private User currentEntity = Session.getCurrentUser();

    private static final String DIRECTORY_PATH = "src/main/java/it/uniroma2/marchidori/maininterface/userpsw";
    private static final String FILE_PATH = DIRECTORY_PATH + "/users.txt";



    public UserBean authenticate(String email, String password) {
        System.out.println("🔍 Tentativo di login con: " + email + " - " + password);
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.err.println("❌ File non trovato: " + FILE_PATH);
            return null; // Se il file non esiste, ritorna null
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int lineNumber=0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                System.out.println("📄 Lettura riga dal file: " + line);
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(email) && parts[1].equals(password)) {
                    System.out.println("✅ Login riuscito per: " + email);
                    return new UserBean(String.valueOf(lineNumber), email, RoleEnum.PLAYER, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());                }
            }
        } catch (IOException e) {
            System.err.println("❌ Errore nella lettura del file: " + e.getMessage());
        }

        System.out.println("❌ Login fallito per: " + email);
        return null;
    }
    /*public UserBean authenticate(String email, String passString) {
        // Simula l'autenticazione
        if ("Mario".equals(email) && "123".equals(passString)) {
            // Restituisce un UserBean con i dati dell'utente.
            // Invece di passare null, passiamo due nuove liste vuote:
            return new UserBean("1", "Mario", email, RoleEnum.PLAYER, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }
        return null; // Restituisce null se l'autenticazione fallisce
    }*/
}
