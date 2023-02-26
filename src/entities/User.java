/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.FileInputStream;
import java.sql.Date;
import java.time.LocalDate;
import javafx.scene.control.DatePicker;
import javax.management.relation.Role;

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
    private role id_role ;
    private String email ;
    private String passwd ;
    private LocalDate  age;
    private FileInputStream image;
/*
    public User(String nom, String prenom, int tel, String adresse, role r1, String email, String passwd, LocalDate birthdate, String sexe, FileInputStream image) {
   
    }
 */
   

    public void setImage(FileInputStream image) {
        this.image = image;
    }

    public FileInputStream getImage() {
        return image;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }


    private String sexe;

    public User(int aInt, String string, String string0) {
        
    }
/*
    public User(String nom, String prenom, String tel, String adresse, role r1, String email, String passwd, LocalDate age,Sexe sexe,FileInputStream image) {

    }
*/

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

    public role getId_role() {
        return id_role;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswd() {
        return passwd;
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

    public void setId_role(role id_role) {
        this.id_role = id_role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public LocalDate getAge() {
        return age;
    }

    public void setAge(LocalDate age) {
        this.age = age;
    }

    /**
     *
     * @param nom
     * @param prenom
     * @param tel
     * @param adresse
     * @param id_role
     * @param email
     * @param passwd
     * @param age
     * @param sexe
     * @param image
     */
    public User(String nom, String prenom, String tel, String adresse, role id_role, String email, String passwd, LocalDate  age,String sexe,FileInputStream image) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.adresse = adresse;
        this.id_role = id_role;
        this.email = email;
        this.passwd = passwd;
        this.age = age;
        this.sexe = sexe;
        this.image = image;
    }

    public User(int iduser, String nom, String prenom, String tel, String adresse, role id_role, String email, String passwd, LocalDate  age,String sexe,FileInputStream image) {
        this.iduser = iduser;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.adresse = adresse;       
        this.id_role = id_role;
        this.email = email;
        this.passwd = passwd;
        this.age = age;
        this.sexe = sexe;
        this.image = image;
    }

    public role getType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }





}
