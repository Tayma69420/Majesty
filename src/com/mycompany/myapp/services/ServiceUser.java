/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Dialog;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.SessionManager;
import com.mycompany.myapp.entities.User;
import com.mycompany.myapp.utils.Statics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *
 * @author bhk
 */
public class ServiceUser {

    public ArrayList<User> user;
boolean result = false;
    public static ServiceUser instance = null;
    public boolean resultOK;
    private ConnectionRequest req, cr;
      public int resultCode;

    private ServiceUser() {
        req = new ConnectionRequest();
    }

    public static ServiceUser getInstance() {
        if (instance == null) {
            instance = new ServiceUser();
        }
        return instance;
    }

    public boolean addTask(User v) {
        String url = Statics.BASE_URL + "/user/new?nom=" + v.getNom()
                + "&prenom=" + v.getPrenom() + "&email=" + v.getEmail()
                + "&tel=" + v.getTel() + "&adresse=" + v.getAdresse()
                 + "&passwd=" + v.getPasswd()
                + "&id_role=" + v.getId_role()+ "&sexe=" + v.getSexe()+ "&image=" + v.getImage();




        req.setUrl(url);
        req.setPost(false);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public ArrayList<User> getAll() {
        user = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/user-list");
        System.out.println("url = " + cr);
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    user = (ArrayList<User>) getList();
                }

                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    private ArrayList<User> getList() {
        JSONParser jsonp;
        jsonp = new JSONParser();

         try {
            Map<String, Object> parsedJson = jsonp.parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");
            for (Map<String, Object> obj : list) {
                User r = new User();
                float id = Float.parseFloat(obj.get("iduser").toString());
                
                     r.setNom((String) obj.get("nom").toString());
                      r.setPrenom((String) obj.get("prenom").toString());
                      
                      r.setEmail((String) obj.get("email").toString());
                //    r.setPicture((String) obj.get("picture").toString());
                // r.setEnergie((String) obj.get("energie").toString());
                // r.setEtat((String) obj.get("etat").toString());

                r.setIduser((int) id);

                user.add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }
    
//    
      public int delete(int id) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/deleteuserjson-"+id);
        System.out.println("url delete : "+cr);
        cr.setHttpMethod("POST");
        // cr.addArgument("id", String.valueOf(id));
        
        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                cr.removeResponseListener(this);
            }
        });
        
        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cr.getResponseCode();
    }
       public int edit(User v) {
        return manage(v );
    }

    public int manage(User v) {
        
        cr = new ConnectionRequest();
        
    
        cr.setHttpMethod("GET");                                               
            cr.setUrl(Statics.BASE_URL + "/updateVoitureJSON/?id=");
            
            cr.addArgument("id", String.valueOf(v.getIduser()));   
        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultCode = cr.getResponseCode();
                cr.removeResponseListener(this);
            }
        });
        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception ignored) {

        }
        return resultCode;
    }

 public boolean signin(TextField useremail, TextField password) {
    String url = Statics.BASE_URL + "/user-json?email=" + useremail.getText().toString() + "&mdp=" + password.getText().toString();
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
                float id = Float.parseFloat(user.get("iduser").toString());
                 float idrole = Float.parseFloat(user.get("idrole").toString());
                SessionManager.setId((int)id);
                SessionManager.setMdp(user.get("passwd").toString());
                 SessionManager.setIdrole((int)idrole);
                SessionManager.setNom(user.get("nom").toString());
                SessionManager.setEmail(user.get("email").toString());
                
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

      public boolean updateUser(User user) {
    String url = Statics.BASE_URL + "/updateuserjson-" + user.getIduser()+"&nom=" + user.getNom();
    
    ConnectionRequest req = new ConnectionRequest();
    req.setUrl(url);
    req.setPost(false);
    
    req.addArgument("nom", user.getNom());
    req.addArgument("prenom", user.getPrenom());
    req.addArgument("email", user.getEmail());
    req.addArgument("tel", user.getTel());
    req.addArgument("adresse", user.getAdresse());
    req.addArgument("passwd", user.getPasswd());
    req.addArgument("image", user.getImage());
    
    req.addResponseListener((NetworkEvent evt) -> {
        resultOK = req.getResponseCode() == 200; // Code HTTP 200 OK
    });
    
    NetworkManager.getInstance().addToQueueAndWait(req);
    
    return resultOK;
}


}
