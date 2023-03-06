/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfaihi;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableRow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import sfaihi.msg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;


public class UserController implements Initializable {
    
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
    private Button avis;
    @FXML
    private ImageView imageview;
    @FXML
    private Button btnimport;
    
    private Image img;
    private FileInputStream fis;
    private File selectedFile;
    
    ObservableList<portfolio> datalist;
    
    
    
    
    Alert alert;
    @FXML
    private Button ptdate;
    @FXML
    private TextField rech;
    @FXML
    private Button pdf;
    @FXML
    private Button dwd;
    
    

   
     
    
 
void search(){
      
          ObservableList<portfolio> portfolio = FXCollections.observableArrayList();
  rech.textProperty().addListener((observable, oldValue, newValue) -> {
    ObservableList<portfolio> searchResults = FXCollections.observableArrayList();
    try {
        pst = con.prepareStatement("SELECT * FROM portfolio WHERE description LIKE ?");
        pst.setString(1, "%" + newValue + "%");
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            portfolio pt = new portfolio();
            pt.setId(rs.getString("idportfolio"));
            pt.setDesc(rs.getString("description"));
            pt.setCv(rs.getString("cv"));
            pt.setImg(rs.getString("image"));
            searchResults.add(pt);
        }
        table.setItems(searchResults);
    } catch (SQLException ex) {
        Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
    }
});
}
    
 
public void table() {
    ObservableList<portfolio> portfolio = FXCollections.observableArrayList();
    try {
        pst = con.prepareStatement("SELECT * FROM portfolio");  
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            portfolio pt = new portfolio();
            pt.setId(rs.getString("idportfolio"));
            pt.setDesc(rs.getString("description"));
            pt.setCv(rs.getString("cv"));
            pt.setImg(rs.getString("image"));
            portfolio.add(pt);

            /*String imagePath = rs.getString("image");
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image img = new Image("file:" + imagePath, imageview.getFitWidth(), imageview.getFitHeight(), true, true);
                imageview.setImage(img);
            }*/
        }
        table.setItems(portfolio);
        id_portfolio.setCellValueFactory(f -> f.getValue().idProperty());
        description.setCellValueFactory(f -> f.getValue().descProperty());
        cv.setCellValueFactory(f -> f.getValue().cvProperty());
        image.setCellValueFactory(f -> f.getValue().imgProperty());
    } catch (SQLException ex) {
        Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
    }
    table.setRowFactory(tv -> {
        TableRow<portfolio> myRow = new TableRow<>();
      myRow.setOnMouseClicked(event -> {
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
        } else if (event.getClickCount() == 2 && (!myRow.isEmpty())) {
            String imagePath = table.getSelectionModel().getSelectedItem().getImg();
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image img = new Image("file:" + imagePath, imageview.getFitWidth(), imageview.getFitHeight(), true, true);
                imageview.setImage(img);
            }
        }
    }
});
        return myRow;
    });
}
 
    
    
  
      
    
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
       search();
    }    

    @FXML
    private void Avis(ActionEvent event) {
                             try{   
     FXMLLoader loader =new FXMLLoader(getClass().getResource("avis.fxml"));
     Parent root=(Parent) loader.load();
    AvisController controller =loader. getController();
    
    
    Scene scene= new Scene(root);
    Stage stage = new Stage() ;
    stage.setScene(scene);
    stage.show();
 
    }catch(IOException e) {
    System.out.append("erreur d'affichage ");
       
        }
    }

    


    @FXML
    private void importimage(ActionEvent event) {
       FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choisir une cv");
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Fichiers cv", "*.png","*.jpg"),
        new ExtensionFilter("Tous les fichiers", ".")
    );
    File selectedFile = fileChooser.showOpenDialog(null);
    if (selectedFile != null) {
        String cvPath = selectedFile.getAbsolutePath();
        imgpt.setText(cvPath);
        Image imgpt = new Image(selectedFile.toURI().toString());
        imageview.setImage(imgpt);
    }  
    }
    
  

}
    
    
    
    
    
    
    
    
    
    
    
    



