package com.groepb.project2.Database;

import com.groepb.project2.data.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private final Environment environment;

    public ConnectDB(Environment environment) {
        this.environment = environment;
    }

    public Connection dbConnect() throws SQLException {
        if (!environment.isConfigured()) {
            throw new SQLException("Environment niet juist geconfigureerd.");
        }
        String url = environment.get("DB_URL");
        String user = environment.get("DB_USER");
        String password = environment.get("DB_PASS");

        return DriverManager.getConnection(url,user,password);
    }
}