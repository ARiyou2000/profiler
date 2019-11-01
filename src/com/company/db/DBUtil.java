package com.company.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    public static final  String MYSQL_CONNECTION_STRING = "jdbc:mysql://localhost/profiler";
    public static final  String USERNAME = "root";
    public static final  String PASSWORD = "toor1234";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(MYSQL_CONNECTION_STRING,USERNAME,PASSWORD);
    }
}
