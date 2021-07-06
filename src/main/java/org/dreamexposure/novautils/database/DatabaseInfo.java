package org.dreamexposure.novautils.database;

import com.zaxxer.hikari.HikariDataSource;

@SuppressWarnings({"unused", "WeakerAccess"})
public class DatabaseInfo {
    private final DatabaseSettings settings;
    private final HikariDataSource source;

    public DatabaseInfo(HikariDataSource _source, DatabaseSettings _settings) {
        source = _source;
        settings = _settings;
    }

    public HikariDataSource getSource() {
        return source;
    }

    public DatabaseSettings getSettings() {
        return settings;
    }
}
