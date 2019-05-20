package org.dreamexposure.novautils.database;


import com.jcraft.jsch.Session;
import com.zaxxer.hikari.HikariDataSource;

import javax.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
public class DatabaseInfo {
    private DatabaseSettings settings;
    private HikariDataSource source;
    private Session session;

    public DatabaseInfo(HikariDataSource _source, DatabaseSettings _settings, @Nullable Session _session) {
        source = _source;
        settings = _settings;
        session = _session;
    }

    public HikariDataSource getSource() {
        return source;
    }

    public DatabaseSettings getSettings() {
        return settings;
    }

    @Nullable
    public Session getSession() {
        return session;
    }
}
