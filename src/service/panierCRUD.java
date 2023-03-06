/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import entities.panier;
import panier.interfaces.interfaceCRUD;
import utils.MyConx;

public class panierCRUD implements interfaceCRUD<panier> {
    @Override
    public void ajouterEntite(panier t) {
        try {
            String sql = "INSERT INTO panier(nom, prix, idprojet) values (?, ?, ?)";
            PreparedStatement st = new MyConx().getCnx().prepareStatement(sql);
            st.setString(1, t.getNom());
            st.setFloat(2, t.getPrix());
            st.setInt(3, t.getIdProjet());
            st.executeUpdate();
            System.out.println("Entité ajoutée avec succès !");
        } catch (SQLException ex) {
            Logger.getLogger(panierCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<panier> listeDesEntites() {
        List<panier> myList = new ArrayList<>();
        String sql = "select * from panier";
        Statement st = null;
        try {
            st = new MyConx().getCnx().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                panier p = new panier();
                p.setIdPanier(rs.getInt("idpanier"));
                p.setNom(rs.getString("nom"));
                p.setPrix(rs.getFloat("prix"));
                p.setIdProjet(rs.getInt("idprojet"));
                myList.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(panierCRUD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return myList;
    }
    public void supprimerEntite(int idPanier) {
    try {
        String sql = "DELETE FROM panier WHERE idpanier = ?";
        PreparedStatement st = new MyConx().getCnx().prepareStatement(sql);
        st.setInt(1, idPanier);
        st.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(panierCRUD.class.getName()).log(Level.SEVERE, null, ex);
    }
}
   
public panier afficherEntite(int id) {
    panier p = null;
    String sql = "SELECT * FROM panier WHERE idpanier=?";
    try {
        PreparedStatement st = new MyConx().getCnx().prepareStatement(sql);
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            p = new panier();
            p.setIdPanier(rs.getInt("idpanier"));
            p.setNom(rs.getString("nom"));
            p.setPrix(rs.getFloat("prix"));
            p.setIdProjet(rs.getInt("idprojet"));
        }
    } catch (SQLException ex) {
        Logger.getLogger(panierCRUD.class.getName()).log(Level.SEVERE, null, ex);
    }
    return p;
}
public void modifierEntite(panier t) {
    try {
        // Vérifier si l'identifiant du projet existe dans la table projet
        String sqlCheck = "SELECT idprojet FROM projet WHERE idprojet = ?";
        PreparedStatement stCheck = new MyConx().getCnx().prepareStatement(sqlCheck);
        stCheck.setInt(1, t.getIdProjet());
        ResultSet rsCheck = stCheck.executeQuery();
        if (!rsCheck.next()) {
            throw new SQLException("L'identifiant du projet n'existe pas dans la base de données.");
        }

        // Modifier l'entité
        String sql = "UPDATE panier SET nom = ?, prix = ?, idprojet = ? WHERE idpanier = ?";
        PreparedStatement st = new MyConx().getCnx().prepareStatement(sql);
        st.setString(1, t.getNom());
        st.setFloat(2, t.getPrix());
        st.setInt(3, t.getIdProjet());
        st.setInt(4, t.getIdPanier());
        st.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(panierCRUD.class.getName()).log(Level.SEVERE, null, ex);
    }
}
}

