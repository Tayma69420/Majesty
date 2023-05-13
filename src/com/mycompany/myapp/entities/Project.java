/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author bhk
 */
public class Project implements Serializable {
    private int idprojet;
    private String titreprojet;
    private String prixprojet;
    private String type;

    public Project(){

    }

    public Project(int idprojet, String titreprojet, String prixprojet, String type) {
        this.idprojet = idprojet;
        this.titreprojet = titreprojet;
        this.prixprojet = prixprojet;
        this.type = type;
       
       
  
    }

    public Project(String titreprojet, String prixprojet, String type) {
        this.titreprojet = titreprojet;
        this.prixprojet = prixprojet;
        this.type = type;
    }


    public int getIdprojet() {
        return idprojet;
    }

    public void setIdprojet(int idprojet) {
        this.idprojet = idprojet;
    }

    public String getTitreprojet() {
        return titreprojet;
    }

    public void setTitreprojet(String titreprojet) {
        this.titreprojet = titreprojet;
    }

    public String getPrixprojet() {
        return prixprojet;
    }

    public void setPrixprojet(String prixprojet) {
        this.prixprojet = prixprojet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Project{" + "idprojet=" + idprojet + ", titreprojet=" + titreprojet + ", prixprojet=" + prixprojet + ", type=" + type + '}';
    }
    
    


    
}

