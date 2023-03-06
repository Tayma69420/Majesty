/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.majesety.services;

import edu.majesety.utils.MyConnection;
import edu.majesty.entities.Reclamation;
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
public class ReclamationCrud {
    private Connection connection;
    public ReclamationCrud() {
         connection = MyConnection.getInstance().getCnx();
    }
    
    public  void createReclamation(Reclamation rec)  {
        String query =
                "INSERT INTO reclamation (titre,recla_desc) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setString(1, rec.getTitre());
            preparedStatement.setString(2, rec.getRecla_desc());
            preparedStatement.executeUpdate();
        }
        
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }      
    
    }
    
     public static ObservableList<Reclamation> readAll() {
    ObservableList<Reclamation> reclamations = FXCollections.observableArrayList();

    try{
        Connection conn = MyConnection.getInstance().getCnx();
        
        String sql = "SELECT id_reclamation,titre,recla_desc FROM reclamation";
       Statement st = conn.createStatement();
                    ResultSet rs=st.executeQuery(sql);
                while (rs.next()) {
                    Reclamation reclamation = new Reclamation();
                    reclamation.setId_reclamation(rs.getInt("id_reclamation"));
                    reclamation.setTitre(rs.getString("titre"));
                    reclamation.setRecla_desc(rs.getString("recla_desc"));
                    
                    reclamations.add(reclamation);
                }
            
        
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return reclamations;
}
     
     
      public void updateReclamatio(Reclamation rec) {
        
        
        
           try {
        String query =
            "UPDATE reclamation SET titre=? , recla_desc=? WHERE id_reclamation=?";
        
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        
        preparedStatement.setString(1, rec.getTitre());

        preparedStatement.setString(2, rec.getRecla_desc());
        preparedStatement.setInt(3, rec.getId_reclamation());
        

        preparedStatement.executeUpdate();
    }

    catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
     

}
      
      
       public void deleteReclamation(Reclamation rec) { 
           
    Connection cnx = MyConnection.getInstance().getCnx();
    String req = "DELETE FROM reclamation WHERE id_reclamation=?";
    try {
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, rec.getId_reclamation());
        ps.executeUpdate();
        System.out.println("reclamation supprimé avec succès.");
    } catch (SQLException ex) {
        System.out.println("Erreur lors de la suppression de l'emploi: " + ex.getMessage());
    }
}
    
    
    
}
