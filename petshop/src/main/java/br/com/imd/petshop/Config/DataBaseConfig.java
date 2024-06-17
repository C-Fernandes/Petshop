package br.com.imd.petshop.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public abstract class DataBaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/petshop";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}