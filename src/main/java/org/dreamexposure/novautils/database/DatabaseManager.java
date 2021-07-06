package org.dreamexposure.novautils.database;

import com.zaxxer.hikari.HikariDataSource;

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
            HikariDataSource ds = new HikariDataSource();

            String connectionURL = "jdbc:mysql://" + settings.getHostname() + ":" + settings.getPort();
            if (settings.getDatabase() != null)
                connectionURL = connectionURL + "/" + settings.getDatabase();

            ds.setJdbcUrl(connectionURL);
            ds.setUsername(settings.getUser());
            ds.setPassword(settings.getPassword());

            System.out.println("Database connection successful!");
            return new DatabaseInfo(ds, settings);
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
            info.getSource().close();
            System.out.println("Successfully disconnected from MySQL Database!");
            return true;
        } catch (Exception e) {
            System.out.println("MySQL Connection may not have been closed properly! Data may be invalidated!");
            e.printStackTrace();
        }
        return false;
    }
}
