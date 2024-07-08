package com.mycompany.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.h2.tools.Server;

/**
 * Singleton class that manages the database connection and initialization.
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private Connection connection;
    private Server h2Server;
    private String dbName;

    private DatabaseManager() {}

    /**
     * Returns the singleton instance of DatabaseManager.
     *
     * @return The singleton instance of DatabaseManager.
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Initializes the database server and connects to the specified database.
     *
     * @param dbUrl The URL of the database.
     * @param user The username for the database.
     * @param password The password for the database.
     * @throws SQLException If a database access error occurs.
     */
    public void initializeAndConnect(String dbUrl, String user, String password) throws SQLException {
        try {
            int port = findFreePort();
            System.out.println("Using port: " + port);

            h2Server = Server.createWebServer("-webPort", String.valueOf(port), "-tcpAllowOthers").start();
            System.out.println("H2 console is running at: " + h2Server.getURL());

            connection = DriverManager.getConnection(dbUrl, user, password);
            this.dbName = dbUrl; // Store the current database URL

            initializeDatabase();
            System.out.println("Database e tabelle create con successo!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Returns the database connection.
     *
     * @return The database connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the database connection and stops the H2 server.
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
     * Initializes the database by creating necessary tables.
     *
     * @throws SQLException If a database access error occurs.
     */
    private void initializeDatabase() throws SQLException {
        try (var stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS rooms (id INT AUTO_INCREMENT PRIMARY KEY, piano INT, nome VARCHAR(255), descrizione TEXT, guarda TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS objects (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), description TEXT, room_id INT, FOREIGN KEY (room_id) REFERENCES rooms(id))");
        }
    }

    /**
     * Finds a free port for the H2 server.
     *
     * @return A free port number.
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
     * Returns the current database URL.
     *
     * @return The current database URL.
     */
    public String getDbName() {
        return dbName;
    }
}
