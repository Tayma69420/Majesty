/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;
import entities.role;
import entities.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.management.relation.Role;
 
import utils.MyConx;
/**
 *
 * @author teymour
 */
public abstract class UserService implements IService<User>{


    
    private Connection conn;
    private String requete;

    public UserService() {
        conn=MyConx.getInstance().getCnx();
    }

    

    @Override
    public void insert(User t) {
    String requete = "INSERT INTO utilisateur (nom, prenom, email, tel, adresse, age,passwd,id_role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    String emailRegex = "\\w+\\.?\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}";
    if (!t.getEmail().matches(emailRegex)) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("L'adresse email est invalide. Veuillez saisir une adresse email valide (ex: nom@gmail.com).");
        alert.showAndWait();
        return;
    }
      if (String.valueOf(t.getTel()).length() != 8) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Attention");
    alert.setHeaderText(null);
    alert.setContentText("Le numéro de téléphone doit comporter exactement 8 chiffres.");
    alert.showAndWait();
    return;
}
    try { 
        // Récupérer l'objet role correspondant au rôle "admin"
        
        RoleService roleService = new RoleService();
        

      

        // Insérer l'utilisateur dans la base de données
        PreparedStatement usr = conn.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        
        usr.setString(1, t.getNom());
        usr.setString(2, t.getPrenom());       
        usr.setString(3, t.getEmail());
        usr.setInt(4, t.getTel());
        usr.setString(5, t.getAdresse());
        usr.setInt(6, t.getAge());
        usr.setString(7, t.getPasswd());

        usr.setInt(8, 2);
        usr.executeUpdate();

        // Récupérer l'ID généré pour l'utilisateur inséré
        ResultSet rs = usr.getGeneratedKeys();
        if (rs.next()) {
            int userId = rs.getInt(1);
            t.setIduser(userId);
        }
    } catch (SQLException ex) {
        Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    public void insertFreelancer(User t) {
    String requete = "INSERT INTO utilisateur (nom, prenom, email, tel, adresse, age,passwd,id_role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    String emailRegex = "\\w+\\.?\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}";
     
    if (!t.getEmail().matches(emailRegex)) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText("L'adresse email est invalide. Veuillez saisir une adresse email valide (ex: nom@gmail.com).");
        alert.showAndWait();
        return;
    }
    if (String.valueOf(t.getTel()).length() != 8) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Attention");
    alert.setHeaderText(null);
    alert.setContentText("Le numéro de téléphone doit comporter exactement 8 chiffres.");
    alert.showAndWait();
    return;
}
        
    try {    
        // Récupérer l'objet role correspondant au rôle "admin"
        
        RoleService roleService = new RoleService();
        

      

        // Insérer l'utilisateur dans la base de données
        PreparedStatement usr = conn.prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);
        
        usr.setString(1, t.getNom());
        usr.setString(2, t.getPrenom());       
        usr.setString(3, t.getEmail());
        usr.setInt(4, t.getTel());
        usr.setString(5, t.getAdresse());
        usr.setInt(6, t.getAge());
        usr.setString(7, t.getPasswd());

        usr.setInt(8, 3);
        usr.executeUpdate();

        // Récupérer l'ID généré pour l'utilisateur inséré
        ResultSet rs = usr.getGeneratedKeys();
        if (rs.next()) {
            int userId = rs.getInt(1);
            t.setIduser(userId);
        }
    } catch (SQLException ex) {
        Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
    }
}


@Override
public void delete(User t) {
    String requetee;
        requetee = "DELETE FROM utilisateur WHERE iduser = ? and iduser != 1";
    try {
        PreparedStatement ps = conn.prepareStatement(requetee);
        ps.setInt(1, t.getIduser());
        ps.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
    }
}


    @Override
public void update(User t) {
    String requete = "UPDATE utilisateur SET nom=?, prenom=?, email=?, tel=?, adresse=?, age=?, passwd=?, id_role=? WHERE iduser=?";
    try {
        PreparedStatement pst = conn.prepareStatement(requete);
        pst.setString(1, t.getNom());
        pst.setString(2, t.getPrenom());
        pst.setString(3, t.getEmail());
        pst.setInt(4, t.getTel());
        pst.setString(5, t.getAdresse());
        pst.setInt(6, t.getAge());
        pst.setString(7, t.getPasswd());
        pst.setInt(8,2);
        pst.setInt(8,3);
        pst.setInt(9, t.getIduser());
        pst.executeUpdate();
        System.out.println("User updated successfully");
    } catch (SQLException ex) {
        System.out.println("Error updating user: " + ex.getMessage());
    }
}


    @Override
    public List<User> readAll() {
         String requete = "SELECT utilisateur.*, role.id_role, role.type FROM utilisateur INNER JOIN role ON utilisateur.id_role = role.id_role";
    List<User> list = new ArrayList<>();
    try {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(requete);
        while (rs.next()) {
            role r = new role();
            r.setId_role(rs.getInt("id_role"));
            r.setType(rs.getString("type"));
            User t;
            t = new User(rs.getInt("iduser"), rs.getString("nom"),rs.getString("prenom"),rs.getInt("tel"),rs.getString("adresse"),r,rs.getString("email"),rs.getString("passwd"),rs.getInt("age"));
            list.add(t);
        }
    } catch (SQLException ex) {
        Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
    }
    return list;
}
   
   

    

    @Override
    public User readById(int iduser) {
     
    requete = "SELECT utilisateur.*, role.id_role, role.type FROM user INNER JOIN role ON utilisateur.id_role = user.id_role where iduser=" + iduser;

    User t = null;
    try {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(requete);
        while (rs.next()) {
            role r = new role();
            r.setId_role(rs.getInt("id_role"));
            r.setType(rs.getString("type"));
           
            t = new User(rs.getInt("iduser"), rs.getString("nom"),rs.getString("prenom"),rs.getInt("tel"),rs.getString("adresse"),r,rs.getString("email"),rs.getString("passwd"),rs.getInt("age"));
        }
    } catch (SQLException ex) {
        Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
    }

    return t;
    }



    }