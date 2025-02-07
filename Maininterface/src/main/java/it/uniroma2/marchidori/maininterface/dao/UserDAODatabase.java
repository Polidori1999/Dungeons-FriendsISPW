package it.uniroma2.marchidori.maininterface.dao;
import it.uniroma2.marchidori.maininterface.bean.UserBean;
import it.uniroma2.marchidori.maininterface.boundary.UserDAO;
import it.uniroma2.marchidori.maininterface.enumerate.RoleEnum;
import it.uniroma2.marchidori.maininterface.dao.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

    public class UserDAODatabase implements UserDAO {

        @Override
        public void saveUser(String email, String password) {
            // Verifica se l'utente esiste già
            if (getUserByEmail(email) != null) {
                System.err.println("❌ Errore: Email già registrata - " + email);
                return;
            }

            // Controllo se la password è già criptata (per evitare doppia cifratura)
            if (!password.startsWith("$2a$")) {
                password = BCrypt.hashpw(password, BCrypt.gensalt());
            }
            System.out.println("🔍 DEBUG: Password hashata prima dell'inserimento → '" + password + "'");


            String query = "INSERT INTO users (email, password) VALUES (?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, email);
                stmt.setString(2, password);

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("✅ Utente salvato nel database: " + email);
                } else {
                    System.err.println("❌ ERRORE: Nessuna riga inserita nel database!");
                }
            } catch (SQLException e) {
                System.err.println("❌ Errore nel salvataggio dell'utente: " + e.getMessage());
            }
        }



        @Override
        public UserBean getUserByEmail(String email) {
            String query = "SELECT email, password FROM users WHERE email = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String foundEmail = rs.getString("email");
                    String hashedPassword = rs.getString("password").trim();

                    // Debug: stampa la password recuperata dal database
                    System.out.println("🔍 DEBUG: Password dal database per " + foundEmail + " → " + hashedPassword);

                    return new UserBean(
                            foundEmail,
                            hashedPassword,
                            RoleEnum.PLAYER,
                            new ArrayList<>(),
                            new ArrayList<>(),
                            new ArrayList<>()
                    );
                } else {
                    System.out.println("❌ Utente non trovato nel database: " + email);
                }
            } catch (SQLException e) {
                System.err.println("❌ Errore nel recupero dell'utente dal database: " + e.getMessage());
            }

            return null;
        }



    }

