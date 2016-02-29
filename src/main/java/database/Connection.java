/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author carlos
 */
public class Connection {
    public static final boolean IS_PRODUCTION = true;
    public static java.sql.Connection getConnection() throws URISyntaxException, SQLException {
        java.sql.Connection conn = null;
        
        try{
            if(IS_PRODUCTION){
                URI dbUri = new URI(System.getenv("DATABASE_URL"));

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                conn = DriverManager.getConnection(dbUrl, username, password);
            } else {
                conn = DriverManager.getConnection(
                        ConnectionSettings.HOST+ConnectionSettings.DB_NAME,
                        ConnectionSettings.USERNAME,
                        ConnectionSettings.PASSWORD);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return conn;
    }
}
