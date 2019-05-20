package org.dreamexposure.novautils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings({"unused"})
public class MySQL extends Database {
    private final DatabaseSettings settings;

    public MySQL(DatabaseSettings settings) {
        this.settings = settings;
    }

    @Override
    public Connection openConnection() throws SQLException,
            ClassNotFoundException {
        if (checkConnection()) {
            return connection;
        }
        String connectionURL = "jdbc:mysql://"
                + this.settings.getHostname() + ":" + this.settings.getPort();
        if (this.settings.getDatabase() != null) {
            connectionURL = connectionURL + "/" + this.settings.getDatabase() + "?autoReconnect=true&useSSL=false";
        }

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(connectionURL,
                this.settings.getUser(), this.settings.getPassword());
        return connection;
    }

    public String getPrefix() {
        return settings.getPrefix();
    }
}