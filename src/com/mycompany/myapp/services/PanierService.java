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
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.entities.Panier;
import com.mycompany.myapp.utils.Statics;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 *
 * @author bhk
 */
public class PanierService {

    public ArrayList<Panier> paniers;

    public static PanierService instance = null;
    public boolean resultOK;
    private ConnectionRequest req, cr;
    public int resultCode;

    private PanierService() {
        req = new ConnectionRequest();
    }

    public static PanierService getInstance() {
        if (instance == null) {
            instance = new PanierService();
        }
        return instance;
    }

   public ArrayList<Panier> getCart() {
    ArrayList<Panier> cart = new ArrayList<>();

    ConnectionRequest req = new ConnectionRequest();
    req.setUrl("http://127.0.0.1:8000/paniermobile/list");
    req.setHttpMethod("GET");

    req.addResponseListener(new ActionListener<NetworkEvent>() {
        @Override
        public void actionPerformed(NetworkEvent evt) {
            if (req.getResponseCode() == 200) {
                JSONParser jsonp = new JSONParser();
                try {
                    Map<String, Object> parsedJson = jsonp.parseJSON(new CharArrayReader(
                            new String(req.getResponseData()).toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");
                    for (Map<String, Object> obj : list) {
                        Panier item = new Panier();
                        float id = Float.parseFloat(obj.get("idpanier").toString());
                        item.setNom((String) obj.get("nom"));
                        item.setPrix(Float.parseFloat(obj.get("prix").toString()));
                         item.setQnt((int) (double) obj.get("qnt"));
                          item.setIdpanier((int) id);
                        cart.add(item);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    NetworkManager.getInstance().addToQueueAndWait(req);

    return cart;
}
   

public void incrementCartItem(Panier item) {
    String url = "http://127.0.0.1:8000/incrementQntJSON/" + item.getIdpanier();
    ConnectionRequest request = new ConnectionRequest(url);
    request.setHttpMethod("POST");
    NetworkManager.getInstance().addToQueueAndWait(request);
}



    public ArrayList<Panier> getAll() {
        paniers = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl( "http://127.0.0.1:8000/projetmobile/list");
        System.out.println("url = " + cr);
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    paniers = (ArrayList<Panier>) getList();
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

        return paniers;
    }

    private ArrayList<Panier> getList() {
        JSONParser jsonp;
        jsonp = new JSONParser();

        try {
            Map<String, Object> parsedJson = jsonp.parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");
            for (Map<String, Object> obj : list) {
                Panier r = new Panier();
                float id = Float.parseFloat(obj.get("idprojet").toString());
                
                r.setNom((String) obj.get("titreprojet").toString());
                r.setPrix(Float.parseFloat(obj.get("prixprojet").toString()));
                r.setIdpanier((int) id);

                paniers.add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paniers;
    }
    
public Map<String, Object> parseJSON(String jsonString) {
    try {
        JSONParser parser = new JSONParser();
        Map<String, Object> json = parser.parseJSON(new CharArrayReader(jsonString.toCharArray()));
        return json;
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    return null;
}

public String addProjectToTable(Panier project) {
    final String[] response = {""}; // declare as final array

    ConnectionRequest req = new ConnectionRequest();
    req.setUrl("http://127.0.0.1:8000/addPanierJSON/new");
    req.setHttpMethod("POST");

    req.addArgument("nom", project.getNom());
    req.addArgument("prix", Float.toString(project.getPrix()));
    req.addArgument("qnt", "1"); // set quantity to 1

    req.addResponseListener(new ActionListener<NetworkEvent>() {
        @Override
        public void actionPerformed(NetworkEvent evt) {
            byte[] data = req.getResponseData();
            if (data != null) {
                response[0] = new String(data); // assign value to response
            }
        }
    });

    NetworkManager.getInstance().addToQueueAndWait(req);
    return response[0];
}

public void clearCart() {
        paniers.clear(); // Clear the cart ArrayList
    }

    public void clearDatabase() {
        ConnectionRequest request = new ConnectionRequest("http://127.0.0.1:8000/clearDatabasePanierJSON");
        request.setHttpMethod("DELETE");
        NetworkManager.getInstance().addToQueueAndWait(request);
    }
    
public void removeCartItem(Panier item) {
    String url = "http://127.0.0.1:8000/deletePanierJSON/" + item.getIdpanier();
    ConnectionRequest request = new ConnectionRequest(url);
    request.setHttpMethod("DELETE");
    NetworkManager.getInstance().addToQueueAndWait(request);
}

  
public double getTotalPrice() {
    double totalPrice = 0;
    for (Panier item : getCart()) {
        totalPrice += item.getPrix() * item.getQnt();
    }
    return totalPrice;
}


    public void decrementCartItem(Panier item) {
         String url = "http://127.0.0.1:8000/decrementQntJSON/" + item.getIdpanier();
    ConnectionRequest request = new ConnectionRequest(url);
    request.setHttpMethod("POST");
    NetworkManager.getInstance().addToQueueAndWait(request);
    }
}
