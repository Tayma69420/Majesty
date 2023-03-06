/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

public class panier {
    private int idPanier;
    private String nom;
    private float prix;
    private int idProjet;
    private int qnt;


    public panier() {}

    public panier(int idPanier, String nom, float prix, int idProjet, int qnt) {
    this.idPanier = idPanier;
    this.nom = nom;
    this.prix = prix;
    this.idProjet = idProjet;
    this.qnt = qnt;
}

public int getQnt() {
    return qnt;
}

public void setQnt(int qnt) {
    this.qnt = qnt;
}

    public panier(int idPanier, String nom, float prix, int idProjet) {
        this.idPanier = idPanier;
        this.nom = nom;
        this.prix = prix;
        this.idProjet = idProjet;
    }

    public int getIdPanier() {
        return idPanier;
    }

    public void setIdPanier(int idPanier) {
        this.idPanier = idPanier;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(int idProjet) {
        this.idProjet = idProjet;
    }
}


    
    

