/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.TextField;
import com.mycompany.myapp.SessionManager;
import com.mycompany.myapp.utils.Statics;
import com.mycompany.myapp.services.ServiceUser;
import java.util.Map;
import com.mycompany.myapp.entities.User;
/**
 *
 * @author bhk
 */
public class HomeFormUser extends Form{

private ConnectionRequest req, cr;
boolean result = false;


    public HomeFormUser() {
        
        setTitle("Home");
        setLayout(BoxLayout.y());
        
        // Create email and password text fields
        TextField emailField = new TextField("", "Email", 20, TextField.EMAILADDR);
        TextField passwordField = new TextField("", "Password", 20, TextField.PASSWORD);
        
        Button btnLogin = new Button("Login");
        Button btnAddTask = new Button("Cree un compte pour freelancer ou Utilisateur");
     //   Button btnListTasks = new Button("List User");
         Button sendEmailButton = new Button("Forgot password");
     
        btnAddTask.addActionListener(e-> new AddUserForm(this).show());
        
        
    btnLogin.addActionListener(e -> {
    String email = emailField.getText();
    String password = passwordField.getText();
    boolean success = signin(emailField, passwordField);
    
});

        
        
        
sendEmailButton.addActionListener(evt -> {
    String email = emailField.getText();
    String url = Statics.BASE_URL + "/sendEmail?email=" + email;
    ConnectionRequest request = new ConnectionRequest(url);
    request.setPost(false);
    request.addResponseListener(response -> {
        if (response.getResponseCode() == 200) {
            Dialog.show("Success", "Email sent", "OK", null);
        } else {
            Dialog.show("Error", "Failed to send email", "OK", null);
        }
    });
    NetworkManager.getInstance().addToQueue(request);
});

// Add email and password text fields, login button, and "Forgot password" button to form
addAll(emailField, passwordField, btnLogin, btnAddTask, sendEmailButton);

     
        
    }   

    
   public boolean signin(TextField useremail, TextField password) {
    String url = Statics.BASE_URL + "/user-json?email="+useremail.getText().toString()+"&passwd="+password.getText().toString();
    req = new ConnectionRequest(url, false);
    req.setUrl(url);
   
    req.addResponseListener((e) ->{
        JSONParser j = new JSONParser();
        String json = new String(req.getResponseData()) + "";
        
        try {
            if((json.equals("incorrect password"))||(json.equals("user not found"))) {
                
                Dialog.show("Echec d'authentification","Email ou mot de passe incorrecte","OK",null);
                
            
            }
            else {
                Map<String,Object> user = j.parseJSON(new CharArrayReader(json.toCharArray()));
                
                //Session 
                int id = (int) Float.parseFloat(user.get("iduser").toString());
                int idrole = (int) Float.parseFloat(user.get("idRole").toString()); // Fetch the idrole of the user
                 
                SessionManager.setId(id);
                SessionManager.setId(idrole);
                SessionManager.setMdp(user.get("passwd").toString());
                SessionManager.setNom(user.get("nom").toString());
                SessionManager.setEmail(user.get("email").toString());
                   SessionManager.setImage(user.get("image").toString());
                
                // Redirect the user based on their idrole
                switch (idrole) {
                    case 1:
                        new ListUserForm(this).show();
                        break;
                    case 2:
                        new ConnUser(this).show();
                        break;
                    case 3:
                        new ListFreeConn(this).show();
                        break;
                    default:
                        Dialog.show("Error", "Invalid idrole", "OK", null);
                        break;
                }
                
                result = true;
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    });
      
    NetworkManager.getInstance().addToQueueAndWait(req);
    return result;
}

}
