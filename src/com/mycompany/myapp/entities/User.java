/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.entities;





/**
 *
 * @author 21626
 */
public class User {
      
    private int iduser ;  
    private String nom ;
    private String prenom ;
    private String tel ;
    private String adresse ;
    private int id_role ;
    private String sexe;
  private String email ;
    private String passwd ;
  private String  age;
    private String image;
    
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getSexe() {
        return sexe;
    }

    public User(int iduser, String nom, String prenom, String tel, String adresse, int id_role, String sexe, String email, String passwd, String age, String image) {
        this.iduser = iduser;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.adresse = adresse;
        this.id_role = id_role;
        this.sexe = sexe;
        this.email = email;
        this.passwd = passwd;
        this.age = age;
        this.image = image;
    }

    public User(String nom, String prenom, String tel, String adresse, int id_role, String sexe, String email, String passwd, String age, String image) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.adresse = adresse;
        this.id_role = id_role;
        this.sexe = sexe;
        this.email = email;
        this.passwd = passwd;
        this.age = age;
        this.image = image;
    }


   
    public User() {
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIduser() {
        return iduser;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTel() {
        return tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public int getId_role() {
        return id_role;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getAge() {
        return age;
    }

    public String getImage() {
        return image;
    }
  
}
