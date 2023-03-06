/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfaihi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class TestController implements Initializable {

    @FXML
    private Button admin;
    @FXML
    private Button freelan;
    @FXML
    private Button user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void admin(ActionEvent event) {
                                try{   
     FXMLLoader loader =new FXMLLoader(getClass().getResource("admin.fxml"));
     Parent root=(Parent) loader.load();
    AdminController controller =loader. getController();
    
    
    Scene scene= new Scene(root);
    Stage stage = new Stage() ;
    stage.setScene(scene);
    stage.show();
 
    }catch(IOException e) {
    System.out.append("erreur d'affichage ");
       
        }
    }

    @FXML
    private void freelan(ActionEvent event) {
                                try{   
     FXMLLoader loader =new FXMLLoader(getClass().getResource("freelancer.fxml"));
     Parent root=(Parent) loader.load();
    FreelancerController controller =loader. getController();
    
    
    Scene scene= new Scene(root);
    Stage stage = new Stage() ;
    stage.setScene(scene);
    stage.show();
 
    }catch(IOException e) {
    System.out.append("erreur d'affichage ");
       
        }
    }

    @FXML
    private void user(ActionEvent event) {
                                try{   
     FXMLLoader loader =new FXMLLoader(getClass().getResource("user.fxml"));
     Parent root=(Parent) loader.load();
    UserController controller =loader. getController();
    
    
    Scene scene= new Scene(root);
    Stage stage = new Stage() ;
    stage.setScene(scene);
    stage.show();
 
    }catch(IOException e) {
    System.out.append("erreur d'affichage ");
       
        }
    }
    
}
