package it.uniroma2.marchidori.maininterface.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Genera un hash della password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Verifica una password rispetto all'hash salvato
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
