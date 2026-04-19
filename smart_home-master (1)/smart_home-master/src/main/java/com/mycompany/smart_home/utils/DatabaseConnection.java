package com.mycompany.smart_home.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static HikariDataSource dataSource;

    static {
        try {
            Properties props = new Properties();
            // Đọc file database.properties
            InputStream is = DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties");
            if (is == null) {
                throw new RuntimeException("Can not find file database.properties!");
            }
            props.load(is);

            // Cấu hình HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.size", "10")));
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(30000);

            dataSource = new HikariDataSource(config);
            System.out.println("Da khoi tao xong HikariCP Connection Pool!");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Loi khoi tao cau hinh Database: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void main(String[] args) {
        // Chạy thử kết nối
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Lay ket noi thanh cong Pool!");
            }
        } catch (SQLException e) {
            System.err.println("Ket noi that bai " + e.getMessage());
        }
    }
}