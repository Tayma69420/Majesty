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
public class Categorie_rec {
    
    int id ;
    String type;
   
    

    public Categorie_rec() {
    }

    public Categorie_rec(int id,String type) {
        this.id = id;
        this.type = type;
     
    }

    public Categorie_rec(String type) {
        this.type = type;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
        public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


 
    @Override
    public String toString() {
        return "Categorie_rec{" + "id=" + id+", type=" + type +  '}';
    }}