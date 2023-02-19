/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import javax.management.relation.Role;

/**
 *
 * @author 21626
 */
public class User {
    private int iduser ;  
    private String nom ;
    private String prenom ;
    private int tel ;
    private String adresse ;
    private role id_role ;
    private String email ;
    private String passwd ;
    private int age;

    public User(int aInt, String string, String string0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public int getTel() {
        return tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public role getId_role() {
        return id_role;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswd() {
        return passwd;
    }

    public int getAge() {
        return age;
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

    public void setTel(int tel) {
        this.tel = tel;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setId_role(role id_role) {
        this.id_role = id_role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setAge(int age) {
        this.age = age;
    }
 

    public User(String nom, String prenom, int tel, String adresse, role id_role, String email, String passwd, int age) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.adresse = adresse;
        this.id_role = id_role;
        this.email = email;
        this.passwd = passwd;
        this.age = age;
    }

    public User(int iduser, String nom, String prenom, int tel, String adresse, role id_role, String email, String passwd, int age) {
        this.iduser = iduser;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.adresse = adresse;       
        this.id_role = id_role;
        this.email = email;
        this.passwd = passwd;
        this.age = age;

    }

    public role getType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
