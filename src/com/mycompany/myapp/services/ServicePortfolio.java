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
import com.mycompany.myapp.entities.Reclamation;
import com.mycompany.myapp.entities.portfolio;
import com.mycompany.myapp.utils.Statics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *
 * @author bhk
 */
public class ServicePortfolio {

    public ArrayList<portfolio> portfolio;

    public static ServicePortfolio instance = null;
    public boolean resultOK;
    private ConnectionRequest req, cr;
    public int resultCode;

    private ServicePortfolio() {
        req = new ConnectionRequest();
    }

    public static ServicePortfolio getInstance() {
        if (instance == null) {
            instance = new ServicePortfolio();
        }
        return instance;
    }

    public boolean addTask(portfolio p) {
        String url = Statics.BASE_URL + "/addPortfolioJSON/new?description=" + p.getDescription()
                + "&image=" + p.getImage() ;

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

    public ArrayList<portfolio> getAll() {
        portfolio = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/portfoliomobile/list");
        System.out.println("url = " + cr);
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    portfolio = (ArrayList<portfolio>) getList();
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

        return portfolio;
    }

private ArrayList<portfolio> getList() {
    JSONParser jsonp = new JSONParser();
    ArrayList<portfolio> portfolioList = new ArrayList<>();

    try {
        Map<String, Object> parsedJson = jsonp.parseJSON(new CharArrayReader(
                new String(cr.getResponseData()).toCharArray()
        ));

        List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

        for (Map<String, Object> obj : list) {
            portfolio p = new portfolio();

            int id = ((Double) obj.get("idportfolio")).intValue();
            String description = (String) obj.get("description");
            String image = (String) obj.get("image");

            p.setIdportfolio(id);
            p.setDescription(description);
            p.setImage(image);

            portfolioList.add(p);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return portfolioList;
}






    public int delete(int id) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/deleteportfolioJSON/" + id);
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

    public int edit(portfolio v) {
        return manage(v);
    }

    public boolean modifier(portfolio v) {

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

    public int manage(portfolio v) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("GET");
        cr.setUrl(Statics.BASE_URL + "//?id=" );

        cr.addArgument("id", String.valueOf(v.getIdportfolio()));
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

    


    public boolean updateUser(portfolio p) {
        //String url = Statics.BASE_URL+"/addBadgej?nomB="badge.getNomB()"&nb="badge.getNb()"&logoB="badge.getLogoB();
        String url = Statics.BASE_URL + "/updateuserjson-{id}" ;
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200;
                req.removeResponseListener(this);

            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;

    }

}