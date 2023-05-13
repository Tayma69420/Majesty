/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp;

import com.codename1.io.Preferences;

/**
 *
 * @author 21626
 */
public class SessionManager {
        
    public static Preferences pref ; // 3ibara memoire sghira nsajlo fiha data 
    
    
    
    // hethom données ta3 user lyt7b tsajlhom fi session  ba3d login 
    private static int id ; 
    private static String nom ; 
    private static String email; 
    private static String mdp ;
private static int idrole ;
    private static String image ;



public static void setIdrole(int idrole) {
        pref.set("idRole",idrole);//nsajl id user connecté  w na3tiha identifiant "id";
    }
  
     public static int getIdrole() {
        return pref.get("idRole",idrole);// kif nheb njib id user connecté apres njibha men pref 
    }

    public static Preferences getPref() {
        return pref;
    }

    public static void setPref(Preferences pref) {
        SessionManager.pref = pref;
    }

    public static int getId() {
        return pref.get("id",id);// kif nheb njib id user connecté apres njibha men pref 
    }

    public static void setId(int id) {
        pref.set("id",id);//nsajl id user connecté  w na3tiha identifiant "id";
    }
 public static void setImage(String image) {
         pref.set("image",image);
    }
   
    public static String getImage() {
        return pref.get("image",image);
    }
    
    public static String getEmail() {
        return pref.get("email",email);
    }

    public static void setEmail(String email) {
         pref.set("email",email);
    }

    public static String getNom() {
        return nom;
    }

    public static void setNom(String nom) {
        SessionManager.nom = nom;
    }

    public static String getMdp() {
        return mdp;
    }

    public static void setMdp(String mdp) {
        SessionManager.mdp = mdp;
    }

  


    
    
    
}
