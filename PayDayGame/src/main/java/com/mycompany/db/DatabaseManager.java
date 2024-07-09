package com.mycompany.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.h2.tools.Server;

/**
 * Classe singleton che gestisce la connessione e l'inizializzazione del database.
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private Connection connection;
    private Server h2Server;
    private String dbName;

    private DatabaseManager() {}

    /**
     * Restituisce l'istanza singleton di DatabaseManager.
     *
     * @return L'istanza singleton di DatabaseManager.
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Inizializza il server del database e si connette al database specificato.
     *
     * @param dbUrl L'URL del database.
     * @param user L'username per il database.
     * @param password La password per il database.
     * @throws SQLException Se si verifica un errore di accesso al database.
     */
    public void initializeAndConnect(String dbUrl, String user, String password) throws SQLException {
        try {
            int port = findFreePort();
            System.out.println("Utilizzo la porta: " + port);

            h2Server = Server.createWebServer("-webPort", String.valueOf(port), "-tcpAllowOthers", "-webAllowOthers").start();
            System.out.println("La console H2 è in esecuzione su: " + h2Server.getURL());

            connection = DriverManager.getConnection(dbUrl, user, password);
            this.dbName = dbUrl; // Memorizza l'URL corrente del database

            initializeDatabase();
            System.out.println("Database e tabelle create con successo!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Restituisce la connessione al database.
     *
     * @return La connessione al database.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Chiude la connessione al database e ferma il server H2.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            if (h2Server != null) {
                h2Server.stop();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inizializza il database creando le tabelle necessarie.
     *
     * @throws SQLException Se si verifica un errore di accesso al database.
     */
    private void initializeDatabase() throws SQLException {
        try (var stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS rooms (id INT AUTO_INCREMENT PRIMARY KEY, piano INT, nome VARCHAR(255), descrizione TEXT, guarda TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS objects (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), description TEXT, room_id INT, FOREIGN KEY (room_id) REFERENCES rooms(id))");
        }
    }

    /**
     * Trova una porta libera per il server H2.
     *
     * @return Un numero di porta libero.
     */
    private static int findFreePort() {
        try (var socket = new java.net.ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        } catch (java.io.IOException e) {
            throw new RuntimeException("Nessuna porta disponibile trovata.", e);
        }
    }

    /**
     * Restituisce l'URL corrente del database.
     *
     * @return L'URL corrente del database.
     */
    public String getDbName() {
        return dbName;
    }
}
