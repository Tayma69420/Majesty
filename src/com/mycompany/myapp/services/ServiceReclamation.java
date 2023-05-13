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
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.Reclamation;
import com.mycompany.myapp.utils.Statics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *
 * @author bhk
 */
public class ServiceReclamation {

    public ArrayList<Reclamation> reclamation;

    public static ServiceReclamation instance = null;
    public boolean resultOK;
    private ConnectionRequest req, cr;
    public int resultCode;

    private ServiceReclamation() {
        req = new ConnectionRequest();
    }

    public static ServiceReclamation getInstance() {
        if (instance == null) {
            instance = new ServiceReclamation();
        }
        return instance;
    }

    public boolean addTask(Reclamation v) {
        String url = Statics.BASE_URL + "/addReclamationJSON/new?titre=" + v.getTitre()
                + "&reclaDesc=" + v.getRecla_desc() ;

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

    public ArrayList<Reclamation> getAll() {
        reclamation = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/ReclamationMobile/list");
        System.out.println("url = " + cr);
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    reclamation = (ArrayList<Reclamation>) getList();
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

        return reclamation;
    }

private ArrayList<Reclamation> getList() {
        JSONParser jsonp;
        jsonp = new JSONParser();

         try {
            Map<String, Object> parsedJson = jsonp.parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");
            for (Map<String, Object> obj : list) {
                Reclamation r = new Reclamation();
                float id = Float.parseFloat(obj.get("idReclamation").toString());
                 r.setTitre((String) obj.get("titre").toString());
                 r.setRecla_desc((String) obj.get("reclaDesc").toString());


                r.setId_reclamation((int) id);

                reclamation.add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reclamation;
 }




    public int delete(int id) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/deleteReclamationJSON/" + id);
        System.out.println("url delete : " + cr);
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

    public int edit(Reclamation v) {
        return manage(v);
    }

    public boolean modifier(Reclamation v) {

        String url = Statics.BASE_URL + "//" ;

        req.setUrl(url);

        req.setHttpMethod("POST");

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; // Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public int manage(Reclamation v) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("GET");
        cr.setUrl(Statics.BASE_URL + "//?id=" );

        cr.addArgument("id", String.valueOf(v.getId_reclamation()));
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
public boolean updateUser(Reclamation user) {
    String url = Statics.BASE_URL + "/updateuserjson-" +user.getRecla_desc()+"&nom=" + user.getTitre();
    
    ConnectionRequest req = new ConnectionRequest();
    req.setUrl(url);
    req.setPost(false);
    
    req.addArgument("titre", user.getTitre());
    req.addArgument("description", user.getRecla_desc());
   TextField tftitre = new TextField("", "titre");
        TextField tfdesc = new TextField("", "description");
    
    req.addResponseListener((NetworkEvent evt) -> {
        resultOK = req.getResponseCode() == 200; // Code HTTP 200 OK
    });
    
    NetworkManager.getInstance().addToQueueAndWait(req);
    
    return resultOK;
}
}
