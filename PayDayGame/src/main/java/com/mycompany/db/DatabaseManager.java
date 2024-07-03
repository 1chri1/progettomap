package com.mycompany.db;

import com.mycompany.type.Stanza;

import java.sql.*;
import java.util.List;
import org.h2.tools.Server;

public class DatabaseManager {

    private static DatabaseManager instance;
    private Connection connection;
    private Server h2Server;

    public DatabaseManager() {
        // Costruttore privato per evitare l'instanziazione diretta
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void initializeAndConnect(String dbUrl, String user, String password) throws SQLException {
        try {
            // Trova una porta libera per il server H2
            int port = findFreePort();
            System.out.println("Using port: " + port);

            // Avvia il server H2
            h2Server = Server.createWebServer("-webPort", String.valueOf(port), "-tcpAllowOthers").start();
            System.out.println("H2 console is running at: " + h2Server.getURL());

            // Connetti al database
            connection = DriverManager.getConnection(dbUrl, user, password);

            // Inizializza il database
            initializeDatabase();

            // Stampa il successo dell'inizializzazione
            System.out.println("Database e tabelle create con successo!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Connection getConnection() {
        return connection;
    }

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

    private void initializeDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Creazione delle tabelle
            stmt.execute("CREATE TABLE IF NOT EXISTS rooms (id INT AUTO_INCREMENT PRIMARY KEY, piano INT, nome VARCHAR(255), descrizione TEXT, guarda TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS objects (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), description TEXT, room_id INT, FOREIGN KEY (room_id) REFERENCES rooms(id))");
        }
    }

    public void insertRooms(List<Stanza> stanze) throws SQLException {
        String insertRoomSQL = "INSERT INTO rooms (piano, nome, descrizione, guarda) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertRoomSQL)) {
            for (Stanza stanza : stanze) {
                pstmt.setInt(1, stanza.getPiano());
                pstmt.setString(2, stanza.getNome());
                pstmt.setString(3, stanza.getDescrizione());
                pstmt.setString(4, stanza.getGuarda());
                pstmt.executeUpdate();
            }
        }
    }

    private static int findFreePort() {
        try (java.net.ServerSocket socket = new java.net.ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        } catch (java.io.IOException e) {
            throw new RuntimeException("No available ports found.", e);
        }
    }
}
