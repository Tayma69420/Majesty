/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.majesty.entities;



/**
 *
 * @author dhiaa
 */
public class Reclamation {
    
    int id_reclamation ;
    String titre;
    String recla_desc;
    

    public Reclamation() {
    }

    public Reclamation(int id_reclamation,String titre, String recla_desc) {
        this.id_reclamation = id_reclamation;
        this.titre = titre;
        this.recla_desc = recla_desc;
    }

    public Reclamation(String titre,String recla_desc) {
        this.titre = titre;
        this.recla_desc = recla_desc;
    }

    public int getId_reclamation() {
        return id_reclamation;
    }

    

    public void setId_reclamation(int id_reclamation) {
        this.id_reclamation = id_reclamation;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
  
    public String getRecla_desc() {
        return recla_desc;
    }

    public void setRecla_desc(String recla_desc) {
        this.recla_desc = recla_desc;
    }

    @Override
    public String toString() {
        return "Reclamation{" + "id_reclamation=" + id_reclamation +", titre=" + titre + ", recla_desc=" + recla_desc + '}';
    }
    
    
    
    
}
