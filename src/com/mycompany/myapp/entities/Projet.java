/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.entities;

/**
 *
 * @author farouk daadaa
 */
public class Projet {
    private int id;
    private String titre;
    private float prix;
    
    
    public Projet(int id, String titre, float prix) {
    this.id = id;
    this.titre = titre;
    this.prix = prix;
}
    
    // Constructors, getters and setters go here...
    public int getId() {
    return id;
}

public void setId(int id) {
    this.id = id;
}

public String getTitre() {
    return titre;
}

public void setTitre(String titre) {
    this.titre = titre;
}

public float getPrix() {
    return prix;
}

public void setPrix(float prix) {
    this.prix = prix;
}
@Override
public String toString() {
    return "Projet{" +
            "id=" + id +
            ", titre='" + titre + '\'' +
            ", prix=" + prix +
            '}';
}

}

