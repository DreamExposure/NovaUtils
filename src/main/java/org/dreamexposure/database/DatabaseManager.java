package org.dreamexposure.database;


import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class DatabaseManager {
    /**
     * Connects to the MySQL server with the provided details in the settings.
     *
     * @param settings The DatabaseSettings with the needed info the connect to the database.
     * @return The DatabaseInfo with all related data for future use.
     */
    public static DatabaseInfo connectToMySQL(DatabaseSettings settings) {
        try {
            MySQL mySQL = new MySQL(settings.getHostname(), settings.getPort(), settings.getDatabase(), settings.getUser(), settings.getPassword());

            Connection mySQLConnection = mySQL.getConnection();

            System.out.println("Database connection successful!");

            return new DatabaseInfo(mySQL, mySQLConnection, settings);
        } catch (Exception e) {
            System.out.println("Failed to connect to database! Are the settings provided correct?");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Disconnects from the MySQL server in the DatabaseInfo.
     *
     * @param info The DatabaseInfo with all the required data.
     * @return <code>true</code> if successful, otherwise <code>false</code>
     */
    public static boolean disconnectFromMySQL(DatabaseInfo info) {
        try {
            info.getMySQL().closeConnection();
            System.out.println("Successfully disconnected from MySQL Database!");
            return true;
        } catch (SQLException e) {
            System.out.println("MySQL Connection may not have been closed properly! Data may be invalidated!");
            e.printStackTrace();
        }
        return false;
    }
}
