/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.majesety.services;

import edu.majesety.utils.MyConnection;
import edu.majesty.entities.Categorie_rec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author dhiaa
 */
public class Categorie_recCrud {
    private Connection connection;
    public Categorie_recCrud() {
         connection = MyConnection.getInstance().getCnx();
    }
    
    public  void createCategorie_rec(Categorie_rec rec)  {
        String query =
                "INSERT INTO categorie_rec (type) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setString(1, rec.getType());
            preparedStatement.executeUpdate();
        }
        
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }      
    
    }
    
     public static ObservableList<Categorie_rec> readAll() {
    ObservableList<Categorie_rec> categorie_recs = FXCollections.observableArrayList();

    try{
        Connection conn = MyConnection.getInstance().getCnx();
        
        String sql = "SELECT id,type FROM categorie_rec";
       Statement st = conn.createStatement();
                    ResultSet rs=st.executeQuery(sql);
                while (rs.next()) {
                    Categorie_rec categorie_rec = new Categorie_rec();
                    categorie_rec.setId(rs.getInt("id"));
                    categorie_rec.setType(rs.getString("type"));
                    
                    
                    categorie_recs.add(categorie_rec);
                }
            
        
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return categorie_recs;
}
     
     
      public void updateCategorie_rec(Categorie_rec rec) {
        
        
        
           try {
        String query =
            "UPDATE categorie_rec SET type=?  WHERE id=?";
        
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        
        preparedStatement.setString(1, rec.getType());

        preparedStatement.setInt(2, rec.getId());
        

        preparedStatement.executeUpdate();
    }

    catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
     

}
      
      
       public void DeleteCategorie_rec(Categorie_rec rec) {
    Connection cnx = MyConnection.getInstance().getCnx();
    String req = "DELETE FROM categorie_rec WHERE id=?";
    try {
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, rec.getId());
        ps.executeUpdate();
        System.out.println("categorie reclamation supprimé avec succès.");
    } catch (SQLException ex) {
        System.out.println("Erreur lors de la suppression de l'emploi: " + ex.getMessage());
    }
}
    
    
    
}
