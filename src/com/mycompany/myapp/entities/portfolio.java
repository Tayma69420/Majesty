/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.entities;

/**
 *
 * @author Ahmed
 */
public class portfolio {
     private int idportfolio;
    private String description;
    private String  cv;
    private String image;
    private int iduser;
    private int idprojet;
    private int rating;
    private int likes;
    private int dislikes;

    public portfolio(int idportfolio, String description, String cv, String image, int iduser, int idprojet, int rating, int likes, int dislikes) {
        this.idportfolio = idportfolio;
        this.description = description;
        this.cv = cv;
        this.image = image;
        this.iduser = iduser;
        this.idprojet = idprojet;
        this.rating = rating;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public portfolio(String description, String cv, String image, int iduser, int idprojet, int rating, int likes, int dislikes) {
        this.description = description;
        this.cv = cv;
        this.image = image;
        this.iduser = iduser;
        this.idprojet = idprojet;
        this.rating = rating;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public portfolio( String description, String image) {
        this.description = description;
        this.image = image;
    }

    public portfolio() {
    }

   

   

    public int getIdportfolio() {
        return idportfolio;
    }

    public void setIdportfolio(int idportfolio) {
        this.idportfolio = idportfolio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getImage() {
        return image;
    }
    
  

    public void setImage(String image) {
        this.image = image;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getIdprojet() {
        return idprojet;
    }

    public void setIdprojet(int idprojet) {
        this.idprojet = idprojet;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public String toString() {
        return "portfolio{" + "idportfolio=" + idportfolio + ", description=" + description + ", cv=" + cv + ", image=" + image + ", iduser=" + iduser + ", idprojet=" + idprojet + ", rating=" + rating + ", likes=" + likes + ", dislikes=" + dislikes + '}';
    }

  

   
    
}
