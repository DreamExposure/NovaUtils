package org.dreamexposure.novautils.database;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.zaxxer.hikari.HikariDataSource;
import io.lettuce.core.RedisClient;

import java.util.Properties;

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
            if (settings.isUseSSH()) {
                //Create Session...
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                JSch jsch = new JSch();
                if (settings.getSshKeyFile() != null)
                    jsch.addIdentity(settings.getSshKeyFile());

                Session session = jsch.getSession(settings.getSshUser(), settings.getSshHost(), settings.getSshPort());
                session.setConfig(config);
                if (settings.getSshPassword() != null)
                    session.setPassword(settings.getSshPassword());
                session.setPortForwardingL(settings.getSshForwardPort(), settings.getHostname(), Integer.parseInt(settings.getPort()));
                session.connect();

                //Create MySQL connections
                HikariDataSource ds = new HikariDataSource();

                String connectionURL = "jdbc:mysql://" + settings.getSshHost() + ":" + settings.getSshForwardPort();
                if (settings.getDatabase() != null)
                    connectionURL = connectionURL + "/" + settings.getDatabase() + "?useSSL=false";

                ds.setJdbcUrl(connectionURL);
                ds.setUsername(settings.getUser());
                ds.setPassword(settings.getPassword());

                System.out.println("Database connection successful!");
                return new DatabaseInfo(ds, settings, session);

            } else {
                HikariDataSource ds = new HikariDataSource();

                String connectionURL = "jdbc:mysql://" + settings.getHostname() + ":" + settings.getPort();
                if (settings.getDatabase() != null)
                    connectionURL = connectionURL + "/" + settings.getDatabase() + "?useSSL=false";

                ds.setJdbcUrl(connectionURL);
                ds.setUsername(settings.getUser());
                ds.setPassword(settings.getPassword());

                System.out.println("Database connection successful!");
                return new DatabaseInfo(ds, settings, null);
            }
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

            if (info.getSettings().isUseSSH() && info.getSession() != null) {
                info.getSession().disconnect();
            }
            return true;
        } catch (Exception e) {
            System.out.println("MySQL Connection may not have been closed properly! Data may be invalidated!");
            e.printStackTrace();
        }
        return false;
    }
    
    public static RedisInfo connectToRedis(DatabaseSettings settings) {
        try {
            RedisClient client = RedisClient.create("redis://" + settings.getPassword() + "@" + settings.getHostname() + ":" + settings.getPort() + "/0");
            
            System.out.println("Database connection successful!");
            
            return new RedisInfo(client, settings);
        } catch (Exception e) {
            System.out.println("Failed to connect to Redis! Are the settings provided correct?");
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean disconnectFromRedis(RedisInfo info) {
        try {
            info.getClient().shutdown();
            
            System.out.println("Successfully disconnected from Redis!");
            return true;
        } catch (Exception e) {
            System.out.println("Redis Connection may not have been closed properly! Data may be invalidated!");
        }
        return false;
    }
}
