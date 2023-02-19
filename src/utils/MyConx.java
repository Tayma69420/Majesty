/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author teymour
 */
public class MyConx {
    
   private String url="jdbc:mysql://localhost:3306/teymouruser";
   private String login="root";
   private String pwd="";
   private Connection cnx;
   private static MyConx instance;

    private MyConx() {
       try {
           cnx=DriverManager.getConnection(url, login, pwd);
           System.out.println("Connexion etablie");
       } catch (SQLException ex) {
           Logger.getLogger(MyConx.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
   
    
public static MyConx getInstance() {
    if (instance == null) {
        instance = new MyConx();
    }
       return instance;
}

    /**
     *
     * @return
     */
    public Connection getCnx() {
        return cnx;
    }
   
    
    
}