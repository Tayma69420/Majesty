/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ahmed
 */
   package sfaihi;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Ahmed
 */
public class AvisController implements Initializable {

 @FXML
    private TextField avpt;

 @FXML
    private Button ptaddavis;
 
     @FXML
    private TableColumn<avis,String> avis;
     
     @FXML
    private TableView<avis> table3;
 
    @FXML
    private TableView<portfolio> table2;
    
    @FXML
    private TableColumn<portfolio,String>id_portfolio;
    
    @FXML
    private TableColumn<portfolio,String>description;
    
    @FXML
    private TableColumn<portfolio,String>cv;
    
    @FXML
    private TableColumn<portfolio,String>image;
    @FXML
    private Button ptback;

    
      @FXML
    void addavis(ActionEvent event) {
String  commentaire;
Connect ();
commentaire= avpt.getText();
try
{
pst = con.prepareStatement ("insert into avis (commentaire) values (?)  ") ;
pst.setString(1,commentaire) ;
pst.executeUpdate () ;


Alert alert= new Alert(Alert.AlertType.INFORMATION) ;
alert.setTitle("Test Connection") ;

    alert.setHeaderText("Avis Aded");
    alert.setTitle("SUCCES") ;

    alert.showAndWait();
    
    
    table3();
    
    avpt.setText("");
  avpt.requestFocus();
    
    
    }
catch(SQLException ex)
{
    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE,null, ex);
}
    }

    
    
    public void table3()
      {
          Connect();
          ObservableList<avis> aviss = FXCollections.observableArrayList();
       try
       {
           pst = con.prepareStatement("SELECT commentaire FROM avis");  
           ResultSet rs = pst.executeQuery();
      {
        while (rs.next())
        {
avis av= new avis () ;
av.setcommentaire(rs.getString ("commentaire")) ;
aviss.add(av) ;
       }
    }
                table3.setItems(aviss) ;
    avis.setCellValueFactory(f->f.getValue().idavisProperty());
               
       }
      
       catch (SQLException ex)
       {
           Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
       }
 
                table3.setRowFactory( tv -> {
     TableRow<avis> myRow = new TableRow<>();
     myRow.setOnMouseClicked (event ->
     {
          if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    myIndex = table3.getSelectionModel().getSelectedIndex();
                    //id = Integer.parseInt(String.valueOf(table3.getItems().get(myIndex).getId()));               
                          
        }
     });
        return myRow;
                   });
      } 
    
    
    
    
    
    
    
    
    
    
    
  public void table2()
      {
          Connect();
          ObservableList<portfolio> portfolio = FXCollections.observableArrayList();
       try
       {
           pst = con.prepareStatement("SELECT * FROM portfolio");  
           ResultSet rs = pst.executeQuery();
      {
        while (rs.next())
        {
portfolio pt= new portfolio () ;
pt.setId (rs.getString ("idportfolio")) ;
pt.setDesc (rs.getString ("description")) ;
pt.setCv(rs.getString("cv"));
pt.setImg(rs.getString("image"));
portfolio.add(pt) ;
       }
    }
                table2.setItems(portfolio) ;
    id_portfolio.setCellValueFactory(f->f.getValue().idProperty());
    description.setCellValueFactory(f->f.getValue().descProperty());
    cv.setCellValueFactory(f->f.getValue ().cvProperty()) ;
    image.setCellValueFactory(f->f.getValue().imgProperty());
               
       }
      
       catch (SQLException ex)
       {
           Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
       }
 
                table2.setRowFactory( tv -> {
     TableRow<portfolio> myRow = new TableRow<>();
     myRow.setOnMouseClicked (event ->
     {
          if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    myIndex = table2.getSelectionModel().getSelectedIndex();
                    id = Integer.parseInt(String.valueOf(table2.getItems().get(myIndex).getId()));               
                          
        }
     });
        return myRow;
                   });
      } 
    
 
   
   
     
     
     
     
     
     
     
     
     
     
     
     
   

Connection con;
PreparedStatement pst ;
int myIndex;
int id;




public void Connect ()
{
try {
    Class.forName("com.mysql.jdbc.Driver");
    con = DriverManager.getConnection("jdbc:mysql://localhost/projet_freelance","root","");
    System.out.println("connection reussite yo !");
    }   catch (ClassNotFoundException ex) {
    System.err.println(ex.getMessage());
    }   catch (SQLException ex) {
        ex.printStackTrace();
    }
}
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       Connect();
       table2();
       table3();
       
    }    

    @FXML
    private void back(ActionEvent event) {
                                 try{   
     FXMLLoader loader =new FXMLLoader(getClass().getResource("portfolio.fxml"));
     Parent root=(Parent) loader.load();
    FXMLDocumentController controller =loader. getController();
    
    
    Scene scene= new Scene(root);
    Stage stage = new Stage() ;
    stage.setScene(scene);
    stage.show();
 
    }catch(IOException e) {
    System.out.append("erreur d'affichage ");
       
        }
    }
       
    
}
