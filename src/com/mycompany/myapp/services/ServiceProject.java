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
import com.mycompany.myapp.entities.Project;
import com.mycompany.myapp.utils.Statics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *
 * @author bhk
 */
public class ServiceProject {

    public ArrayList<Project> project;

    public static ServiceProject instance = null;
    public boolean resultOK;
    private ConnectionRequest req, cr;
    public int resultCode;

    private ServiceProject() {
        req = new ConnectionRequest();
    }

    public static ServiceProject getInstance() {
        if (instance == null) {
            instance = new ServiceProject();
        }
        return instance;
    }

    public boolean addTask(Project pr) {
        String url = Statics.BASE_URL + "/addProjectJSON/new?titre=" + pr.getTitreprojet()
                + "&prixProjet=" + pr.getPrixprojet() 
                + "&type=" + pr.getType() ;;

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

    public ArrayList<Project> getAll() {
        project = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/ProjectMobile/list");
        System.out.println("url = " + cr);
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    project = (ArrayList<Project>) getList();
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

        return project;
    }

private ArrayList<Project> getList() {
        JSONParser jsonp;
        jsonp = new JSONParser();

         try {
            Map<String, Object> parsedJson = jsonp.parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");
            for (Map<String, Object> obj : list) {
                Project pr = new Project();
                float id = Float.parseFloat(obj.get("idProject").toString());
                 pr.setTitreprojet((String) obj.get("titreprojet").toString());
                  //pr.setPrixprojet((String) obj.get("prixprojet").toString());
                 pr.setType((String) obj.get("type").toString());
                
                


                pr.setIdprojet((int) id);

                project.add(pr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return project;
 }




    public int delete(int id) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/deleteProjectJSON/" + id);
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

    public int edit(Project pro) {
        return manage(pro);
    }

    public boolean modifier(Project pro) {

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

    public int manage(Project pro) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("GET");
        cr.setUrl(Statics.BASE_URL + "//?id=" );

        cr.addArgument("id", String.valueOf(pro.getIdprojet()));
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

}