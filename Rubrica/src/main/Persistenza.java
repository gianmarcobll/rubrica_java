package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Persistenza {

    private GestoreDB gestoreDB;

    public Persistenza() {
    	this.gestoreDB = new GestoreDB();
    }

    // --- Metodi per Persona ---

    public boolean aggiungiPersona(Persona p) {
        String sql = "INSERT INTO persone (nome, cognome, indirizzo, telefono, eta, id_utente) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = gestoreDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
               pstmt.setString(1, p.getNome());
               pstmt.setString(2, p.getCognome());
               pstmt.setString(3, p.getIndirizzo());
               pstmt.setString(4, p.getTelefono());
               pstmt.setInt(5, p.getEta());
               pstmt.setInt(6, p.getIdUtente()); // Associa la persona all'utente loggato

               int affectedRows = pstmt.executeUpdate();
               if (affectedRows > 0) {
                   try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                       if (generatedKeys.next()) {
                           p.setId(generatedKeys.getInt(1)); // Imposta l'ID generato dal DB sull'oggetto Persona
                       }
                   }
                   return true;
               }
           } catch (SQLException e) {
               System.err.println("Errore SQL durante l'aggiunta della persona: " + e.getMessage());
           }
           return false;
    }
    
    public boolean modificaPersona(Persona p) {
    	String sql = "UPDATE persone SET nome = ?, cognome = ?, indirizzo = ?, telefono = ?, eta = ? WHERE id = ? AND id_utente = ?";
        try (Connection conn = gestoreDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
               pstmt.setString(1, p.getNome());
               pstmt.setString(2, p.getCognome());
               pstmt.setString(3, p.getIndirizzo());
               pstmt.setString(4, p.getTelefono());
               pstmt.setInt(5, p.getEta());
               pstmt.setInt(6, p.getId());
               pstmt.setInt(7, p.getIdUtente());
               
               int affectedRows = pstmt.executeUpdate();
               return affectedRows > 0;
           } catch (SQLException e) {
               System.err.println("Errore SQL durante la modifica della persona: " + e.getMessage());
           }
           return false;
    }

    public boolean eliminaPersona(int idPersona, int idUtente) {
        String sql = "DELETE FROM persone WHERE id = ? AND id_utente  = ?";
        try (Connection conn = gestoreDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPersona);
            pstmt.setInt(2, idUtente);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Errore SQL durante l'eliminazione della persona: " + e.getMessage());
        }
        return false;
    }

    public List<Persona> caricaContatti(int idUtente) {
    	List<Persona> contatti = new ArrayList<>();
    	String sql = "SELECT * FROM persone WHERE id_utente = ?";
        try (Connection conn = gestoreDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUtente);
            try (ResultSet rs = pstmt.executeQuery()) {
            	while (rs.next()) {
                    // Per ogni riga nel ResultSet, crea un oggetto Persona e popolalo
                    Persona p = new Persona();
                    p.setId(rs.getInt("id")); // L'ID della persona
                    p.setNome(rs.getString("nome"));
                    p.setCognome(rs.getString("cognome"));
                    p.setIndirizzo(rs.getString("indirizzo"));
                    p.setTelefono(rs.getString("telefono"));
                    int eta = rs.getInt("eta");
                    if (rs.wasNull()) { // Se l'ultimo valore letto era SQL NULL
                        p.setEta(0); // O qualsiasi valore di default appropriato, o un Integer wrapper
                    } else {
                        p.setEta(eta);
                    }
                    p.setIdUtente(rs.getInt("id_utente")); // L'ID dell'utente proprietario del contatto

                    contatti.add(p); // Aggiungi la persona alla lista
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore SQL durante il caricamento dei contatti dell'utente " + idUtente + ": " + e.getMessage());
        }
        return contatti;
      
    }

    // --- Metodi per Utente (Login/Registrazione) ---
    public Utente autenticaUtente(String username, String password) {
        String sql = "Select id, username, password FROM utenti WHERE username = ? AND password = ?";
        try (Connection conn = gestoreDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Utente(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore SQL durante l'autenticazione dell'utente: " + e.getMessage());
        }
        return null;
    }

    public boolean registraUtente(Utente utente) {
    	String sql = "INSERT INTO utenti (username, password) VALUES (?, ?)";
        try (Connection conn = gestoreDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, utente.getUsername());
            pstmt.setString(2, utente.getPassword());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        utente.setId(generatedKeys.getInt(1)); // Imposta l'ID generato sul nuovo utente
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { // SQLSTATE per violazione di integrità (es. unique constraint)
                System.err.println("Errore: Username '" + utente.getUsername() + "' già esistente.");
                JOptionPane.showMessageDialog(null, "Username già in uso. Scegliere un altro username.", "Errore Registrazione", JOptionPane.WARNING_MESSAGE);
            } else {
                System.err.println("Errore SQL durante la registrazione dell'utente: " + e.getMessage());
            }
        }
        return false;
    }
}