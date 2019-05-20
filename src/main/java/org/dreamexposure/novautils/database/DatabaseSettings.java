package org.dreamexposure.novautils.database;


import javax.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class DatabaseSettings {
    //For database
    private final String hostname;
    private final String port;
    private final String database;
    private final String user;
    private final String password;
    private final String prefix;

    //For SSH tunnel
    private String sshHost;
    private int sshPort;
    private int sshForwardPort;
    private String sshUser;
    private String sshPassword;
    private String sshKeyFile;

    private boolean useSSH = false;


    /**
     * Creates an instance of the DatabaseSettings with the required data in order to connect to the database.
     *
     * @param _hostname The hostname/IP of the database.
     * @param _port     The port the database runs on.
     * @param _database The name of the database to use.
     * @param _user     The user to login to the database with.
     * @param _password The password to login to the database with.
     * @param _prefix   The prefix to use before table names.
     */
    public DatabaseSettings(String _hostname, String _port, String _database, String _user, String _password, String _prefix) {
        hostname = _hostname;
        port = _port;
        database = _database;
        user = _user;
        password = _password;
        prefix = _prefix;
    }

    public void withSSH(String _host, int _port, String _user, @Nullable String _pass, @Nullable String _keyFile) {
        sshHost = _host;
        sshPort = _port;
        sshUser = _user;
        sshPassword = _pass;
        sshKeyFile = _keyFile;

        useSSH = true;
    }

    /**
     * Gets the hostname/IP of the database.
     *
     * @return The hostname/IP of the database.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Gets the port the database runs on.
     *
     * @return The port the database runs on.
     */
    public String getPort() {
        return port;
    }

    /**
     * Gets the name of the database to use.
     *
     * @return The name of the database to use.
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Gets the user to login to the database with.
     *
     * @return The user to login to the database with.
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets the password to login to the database with.
     *
     * @return The password to login to the database with.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the prefix to use before table names.
     *
     * @return The prefix to use before table names.
     */
    public String getPrefix() {
        return prefix;
    }

    public String getSshHost() {
        return sshHost;
    }

    public int getSshPort() {
        return sshPort;
    }

    public int getSshForwardPort() {
        return sshForwardPort;
    }

    public String getSshUser() {
        return sshUser;
    }

    public boolean isUseSSH() {
        return useSSH;
    }

    @Nullable
    public String getSshPassword() {
        return sshPassword;
    }

    @Nullable
    public String getSshKeyFile() {
        return sshKeyFile;
    }
}
