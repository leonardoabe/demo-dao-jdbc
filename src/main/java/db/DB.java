package db;

import db.exception.DbException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    private final static String propsPath = "db.properties";
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            Properties props = loadProperties();
            String url = props.getProperty("dburl");
            try {
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DbException("Failed to connect to DB: " + e.getMessage());
            }
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException("Failed to close DB connection:" + e.getMessage());
            }
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream(propsPath)) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new DbException("Failed to load DB properties: " + e.getMessage());
        }
    }

    public static void closeStatement(Statement st) {
        try {
            st.close();
        } catch (SQLException e) {
            throw new DbException("Failed to close statement: " + e.getMessage());
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException e) {
            throw new DbException("Failed to close result set: " + e.getMessage());
        }
    }
}
