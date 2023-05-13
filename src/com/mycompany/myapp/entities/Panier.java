/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.entities;

import java.io.Serializable;

/**
 *
 * @author bhk
 */
public class Panier implements Serializable {
   private int idpanier;
    private String nom;
      private float prix;
    private int idprojet;
    private int qnt;
    private int iduser;

    public int getIdpanier() {
        return idpanier;
    }

    public String getNom() {
        return nom;
    }

    public float getPrix() {
        return prix;
    }

    public int getIdprojet() {
        return idprojet;
    }

    public int getQnt() {
        return qnt;
    }

    public int getIduser() {
        return iduser;
    }
 

    public void setIdpanier(int idpanier) {
        this.idpanier = idpanier;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public void setIdprojet(int idprojet) {
        this.idprojet = idprojet;
    }

    public void setQnt(int qnt) {
        this.qnt = qnt;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }
    

   public Panier() {
        // empty constructor
    }

    public Panier(int idpanier, String nom, float prix, int idprojet, int qnt, int iduser) {
        this.idpanier = idpanier;
        this.nom = nom;
        this.prix = prix;
        this.idprojet = idprojet;
        this.qnt = qnt;
        this.iduser = iduser;
    }

   

 
    


     @Override
    public String toString() {
        return "Panier{" +
                "idpanier=" + idpanier +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", idprojet=" + idprojet +
                ", qnt=" + qnt +
                ", iduser=" + iduser +
                '}';
    }
    
    
 
}
