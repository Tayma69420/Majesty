/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfaihi;

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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;

public class FXMLDocumentController implements Initializable {
    
 @FXML
    private TextField cvpt;

    @FXML
    private TextField despt;

    @FXML
    private TextField imgpt;
    
    @FXML
    private TableView<portfolio> table;
    
    @FXML
    private TableColumn<portfolio,String>id_portfolio;
    
    @FXML
    private TableColumn<portfolio,String>description;
    
    @FXML
    private TableColumn<portfolio,String>cv;
    
    @FXML
    private TableColumn<portfolio,String>image;

    @FXML
    private Button ptadd;

    @FXML
    private Button ptdele;

    @FXML
    private Button ptmd;

    
    
    
    
    
    
    
    
    @FXML
    void Add(ActionEvent event) throws SQLException {
String  des, cv,img;
Connect ();
des= despt.getText();
cv= cvpt.getText();
img= imgpt.getText();

//Volet 1: Validation de champs de texte vides
 if (despt.getText().isEmpty() || cvpt.getText().isEmpty()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champ(s) vide(s)");
        alert.setContentText("Veuillez remplir tous les champs obligatoires");
        alert.showAndWait();
    } else {
     //Volet 2: Vérification des doublons dans la base de données
 PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM portfolio WHERE cv = ?");
stmt.setString(1, cv);
ResultSet rs = stmt.executeQuery();
if (rs.next() && rs.getInt(1) > 0) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Erreur");
    alert.setHeaderText("Produit déjà existant");
    alert.setContentText("Le produit " + cv + " existe déjà dans la base de données");
    alert.showAndWait();
} else {
    // varification de @ en description
     String email = despt.getText();
    if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Z|a-z]{2,}$")) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Adresse e-mail incorrecte");
        alert.setContentText("Veuillez saisir une adresse e-mail valide");
        alert.showAndWait();
    }else{
pst = con.prepareStatement ("insert into portfolio (description,cv,image) values (?,?,?)  ") ;
pst.setString(1,des) ;
pst.setString (2,cv) ;
pst.setString (3,img) ;
pst.executeUpdate () ;


Alert alert= new Alert(Alert.AlertType.INFORMATION) ;
alert.setTitle("Test Connection") ;

    alert.setHeaderText("portfolio Aded");
    alert.setTitle("SUCCES") ;

    alert.showAndWait();
    table();
    
    despt.setText("");
   cvpt.setText("");
   imgpt.setText("");
   despt.requestFocus();
    }
}}}
    
  
    public void table()
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
                table.setItems(portfolio) ;
    id_portfolio.setCellValueFactory(f->f.getValue().idProperty());
    description.setCellValueFactory(f->f.getValue().descProperty());
    cv.setCellValueFactory(f->f.getValue ().cvProperty()) ;
    image.setCellValueFactory(f->f.getValue().imgProperty());
               
       }
      
       catch (SQLException ex)
       {
           Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
       }
 
                table.setRowFactory( tv -> {
     TableRow<portfolio> myRow = new TableRow<>();
     myRow.setOnMouseClicked (event ->
     {
         //volet 5: il faut tester si l'utilisateur a cliqué sur une ligne de tableau pour la modifier/supprimer... 
         portfolio selectedProduct = table.getSelectionModel().getSelectedItem();
         if (selectedProduct == null) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Produit non sélectionné");
        alert.setContentText("Veuillez sélectionner un produit à modifier");
        alert.showAndWait();
    } else {
          if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    myIndex = table.getSelectionModel().getSelectedIndex();
                    id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));
                    despt.setText(table.getItems().get(myIndex).getDesc());
                    cvpt.setText(table.getItems().get(myIndex).getCv());
                    imgpt.setText(table.getItems().get(myIndex).getImg());                
                          
        }}
     });
        return myRow;
                   });
      } 
    
    
    @FXML
    void Delete(ActionEvent event) throws SQLException {
        myIndex = table.getSelectionModel().getSelectedIndex();
        id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));
             
        
        //Volet 4: Boîte de dialogue de confirmation pour la modification/suppression
 Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Supprimer le produit?");
    alert.setContentText("Êtes-vous sûr de vouloir supprimer ce produit?");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
            pst = con.prepareStatement("delete from portfolio where idportfolio = ? ");
            pst.setInt(1, id);
            pst.executeUpdate();
            
           
 
alert.setHeaderText("portfolio Selected");
alert.setContentText("Deletedd!");
 
alert.showAndWait();
                  table();
       
    }}
 
    
    
  @FXML
    void Update(ActionEvent event) throws SQLException {
      
       String  des, cv,img;
        
         myIndex = table.getSelectionModel().getSelectedIndex();
        id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));
          
            des = despt.getText();
            cv = cvpt.getText();
            img = imgpt.getText();
            
            //Volet 4: Boîte de dialogue de confirmation pour la modification/suppression
            Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Supprimer le produit?");
    alert.setContentText("Êtes-vous sûr de vouloir supprimer ce produit?");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      
            pst = con.prepareStatement("update portfolio set description = ? ,cv = ?,image = ? where idportfolio = ? ");
            pst.setString(1, des);
            pst.setString(2, cv);
            pst.setString(3, img);
             pst.setInt(4, id);
            pst.executeUpdate();
 
alert.setHeaderText("portfolio Selected");
alert.setContentText("Updateddd!");
 
alert.showAndWait();
                table();
        }}
        
    
    
      
    
 Connection con;
PreparedStatement pst;
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
       table();
    }    
    

}

