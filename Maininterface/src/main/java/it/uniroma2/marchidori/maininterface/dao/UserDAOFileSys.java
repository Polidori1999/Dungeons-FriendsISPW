package it.uniroma2.marchidori.maininterface.dao;

import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.entity.User;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static it.uniroma2.marchidori.maininterface.scenemanager.SceneSwitcher.logger;

public class UserDAOFileSys implements UserDAO {
    private static final String DIRECTORY_PATH = "src/main/java/it/uniroma2/marchidori/maininterface/userpsw";
    private static final String USER_FILE_PATH = DIRECTORY_PATH + "/users.txt";
    private static final String LOBBY_FILE_PATH = DIRECTORY_PATH + "/user_lobbies.txt";

    private static final Logger logger = Logger.getLogger(UserDAOFileSys.class.getName());

    public UserDAOFileSys() {
        createDirectory();
    }

    private void createDirectory() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                logger.info("Cartella creata: " + DIRECTORY_PATH);
            } else {
                logger.severe("Errore nella creazione della cartella.");
            }
        }
    }

    @Override
    public void saveUser(String email, String password) {
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());//da spostare nel controller
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE_PATH, true))) {
            writer.write(email + "," + hashedPassword);
            writer.newLine();
        } catch (IOException e) {
            logger.severe("Errore nella scrittura del file: " + e.getMessage());
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(email)) {
                    return new User(data[0], data[1], new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                }
            }
        } catch (IOException e) {
            logger.severe("Errore nella lettura del file: " + e.getMessage());
        }
        return null;
    }

    public void saveUserLobbies(String email, List<String> newLobbies) {
        List<String> allUserData = new ArrayList<>();
        boolean userFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(LOBBY_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(email)) {
                    Set<String> lobbiesSet = new HashSet<>();
                    for (int i = 1; i < data.length; i++) {
                        lobbiesSet.add(data[i]);
                    }
                    lobbiesSet.addAll(newLobbies);
                    allUserData.add(email + "," + String.join(",", lobbiesSet));
                    userFound = true;
                } else {
                    allUserData.add(line);
                }
            }
        } catch (IOException e) {
            logger.severe("Errore nella lettura del file: " + e.getMessage());
        }

        if (!userFound) {
            allUserData.add(email + "," + String.join(",", newLobbies));
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOBBY_FILE_PATH, false))) {
            for (String userData : allUserData) {
                writer.write(userData);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Errore nella scrittura delle lobby: " + e.getMessage());
        }
    }

    public List<String> getUserLobbies(String email) {
        List<String> lobbies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LOBBY_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(email)) {
                    for (int i = 1; i < data.length; i++) {
                        lobbies.add(data[i]);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            logger.severe("Errore nella lettura delle lobby: " + e.getMessage());
        }
        return lobbies;
    }


    public void removeUserLobby(String email, String lobbyName) {
        List<String> allUserData = new ArrayList<>();
        boolean userFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(LOBBY_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(email)) {
                    userFound = true;
                    List<String> updatedLobbies = new ArrayList<>();

                    // Mantiene solo le lobby diverse da quella da rimuovere
                    for (int i = 1; i < data.length; i++) {
                        if (!data[i].equals(lobbyName)) {
                            updatedLobbies.add(data[i]);
                        }
                    }

                    // Se l'utente ha ancora lobby, lo aggiungiamo alla lista
                    if (!updatedLobbies.isEmpty()) {
                        allUserData.add(email + "," + String.join(",", updatedLobbies));
                    }
                } else {
                    allUserData.add(line);
                }
            }
        } catch (IOException e) {
            logger.severe("Errore nella lettura del file delle lobby: " + e.getMessage());
            return;
        }

        // Scriviamo i dati aggiornati nel file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOBBY_FILE_PATH, false))) {
            for (String userData : allUserData) {
                writer.write(userData);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Errore nella scrittura delle lobby: " + e.getMessage());
        }

        if (userFound) {
            logger.info("Lobby '" + lobbyName + "' rimossa con successo per l'utente: " + email);
        } else {
            logger.warning("L'utente " + email + " non aveva la lobby: " + lobbyName);
        }
    }
}

