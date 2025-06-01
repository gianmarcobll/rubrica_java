package main;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class GestoreDB {
	private static final String CONFIG_FILE = "credenziali_database.properties";
	private String url;
	private String user;
	private String password;
	
	public GestoreDB() {
		Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
            String ip = props.getProperty("db.ip-server-mysql", "localhost");
            String port = props.getProperty("db.porta", "3306");
            String dbName = props.getProperty("db.nome-database", "rubrica_db");
            
            this.url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            this.user = props.getProperty("db.username");
            this.password = props.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Configurazione database caricata: " + url + " per utente: " + user);

        } catch (IOException e) {
            System.err.println("Errore nel caricamento del file di configurazione " + CONFIG_FILE + ": " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Errore: File di configurazione database non trovato o illeggibile.\n" + e.getMessage(), "Errore Configurazione DB", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Impossibile caricare la configurazione del database.", e);
        } catch (ClassNotFoundException e) {
            System.err.println("Errore: Driver JDBC MySQL non trovato. Assicurati che mysql-connector-j.jar sia nel classpath.");
            JOptionPane.showMessageDialog(null, "Errore: Driver MySQL non trovato. Assicurati di aver aggiunto 'mysql-connector-j.jar' al progetto.", "Errore Driver DB", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Driver JDBC MySQL non disponibile.", e);
        }
    }

    public Connection getConnection() throws SQLException {
        if (url == null || user == null || password == null) {
            throw new SQLException("Le credenziali del database non sono state caricate correttamente.");
        }
        return DriverManager.getConnection(url, user, password);
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Errore nella chiusura della connessione: " + e.getMessage());
            }
        }
    }
}
