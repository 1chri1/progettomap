package com.mycompany.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.h2.tools.Server;

public class DatabaseManager {

    private static DatabaseManager instance;
    private Connection connection;
    private Server h2Server;
    private String dbName;

    private DatabaseManager() {}

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void initializeAndConnect(String dbUrl, String user, String password) throws SQLException {
        try {
            int port = findFreePort();
            System.out.println("Using port: " + port);

            h2Server = Server.createWebServer("-webPort", String.valueOf(port), "-tcpAllowOthers").start();
            System.out.println("H2 console is running at: " + h2Server.getURL());

            connection = DriverManager.getConnection(dbUrl, user, password);
            this.dbName = dbUrl; // Memorizza l'URL del database attuale

            initializeDatabase();
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
        try (var stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS rooms (id INT AUTO_INCREMENT PRIMARY KEY, piano INT, nome VARCHAR(255), descrizione TEXT, guarda TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS objects (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), description TEXT, room_id INT, FOREIGN KEY (room_id) REFERENCES rooms(id))");
        }
    }

    private static int findFreePort() {
        try (var socket = new java.net.ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        } catch (java.io.IOException e) {
            throw new RuntimeException("Nessuna porta disponibile trovata.", e);
        }
    }

    public String getDbName() {
        return dbName;
    }
}
