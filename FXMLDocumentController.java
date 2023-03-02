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
import static org.apache.http.client.methods.RequestBuilder.options;


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
    
    

  
    @FXML
    void Add(ActionEvent event) throws SQLException, FileNotFoundException, MessagingException {
String  des, cv,img;
Connect ();
des= despt.getText();
cv= cvpt.getText();
img= imgpt.getText();
//fis = new FileInputStream(selectedFile);


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
    alert = new Alert(AlertType.ERROR);
    alert.setTitle("Erreur");
    alert.setHeaderText("Produit déjà existant");
    alert.setContentText("Le produit " + cv + " existe déjà dans la base de données");
    alert.showAndWait();
} else{
pst = con.prepareStatement ("insert into portfolio (description,cv,image) values (?,?,?)  ") ;
pst.setString(1,des) ;
pst.setString (2,cv) ;
pst.setString(3,img);
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
  //mailutil.sendmail("majesty.projet@gmail.com");
  //msg.sendSMS("+21650381852");
    }

}}
    
     
    
 
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
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
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
 
    
    @FXML
    void Delete(ActionEvent event) throws SQLException {
        myIndex = table.getSelectionModel().getSelectedIndex();
       // id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));
       //Volet 1: Validation de champs de texte vides
 if (despt.getText().isEmpty() || cvpt.getText().isEmpty()) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champ(s) vide(s)");
        alert.setContentText("Veuillez remplir tous les champs obligatoires");
        alert.showAndWait();
    } else {      
        
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
       
    }}}
 
    
    
  @FXML
    void Update(ActionEvent event) throws SQLException {
      
       String  des, cv,img;
        
         myIndex = table.getSelectionModel().getSelectedIndex();
       // id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));
          
            des = despt.getText();
            cv = cvpt.getText();
            img = imgpt.getText();
             //Volet 1: Validation de champs de texte vides
 if (despt.getText().isEmpty() || cvpt.getText().isEmpty()) {
         alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champ(s) vide(s)");
        alert.setContentText("Veuillez remplir tous les champs obligatoires");
        alert.showAndWait();
    } else {
           
            //Volet 4: Boîte de dialogue de confirmation pour la modification/suppression
            Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Modifier le portfolio?");
    alert.setContentText("Êtes-vous sûr de vouloir modifier ce portfolio?");
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
        }}}
        
    
  
      
    
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
    
    
    @FXML
    private void date(ActionEvent event) {
     // Create a label to display the date
        Label dateLabel = new Label();
        Label timeLabel = new Label();
        // Create a button and set its action
        Button dateButton = new Button("Show Date");
        dateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LocalDate date = LocalDate.now();
                LocalTime time = LocalTime.now();
                dateLabel.setText("Today's date is: " + date.toString());
                timeLabel.setText("The current time is: " + time.toString());
            }
        });
        
        // Add the label and button to a VBox
        VBox root = new VBox();
        root.getChildren().addAll(dateLabel, timeLabel, dateButton);
        
        // Create a scene and set it on the stage
        Scene scene = new Scene(root, 300, 250);
        Stage stage = new Stage() ;
        stage.setScene(scene);
        stage.setTitle("Date Button");
        stage.show();
    }      

    private void sendSMS(ActionEvent event) {
                                      try{   
     FXMLLoader loader =new FXMLLoader(getClass().getResource("message.fxml"));
     Parent root=(Parent) loader.load();
    MessageController controller =loader. getController();
    
    
    Scene scene= new Scene(root);
    Stage stage = new Stage() ;
    stage.setScene(scene);
    stage.show();
 
    }catch(IOException e) {
    System.out.append("erreur d'affichage ");
       
        }
    
    }
    
    
    
    
    
    
    
    
    
    
    

   @FXML
private void importpdf(ActionEvent event) {
    // Create a FileChooser to select the PDF file
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new ExtensionFilter("PDF files", "*.pdf"));
    File selectedFile = fileChooser.showOpenDialog(null);

    if (selectedFile != null) {
        try {
            // Get the absolute path of the selected PDF file
            String pdfPath = selectedFile.getAbsolutePath();

            // Set the path in the cvpt TextField
            cvpt.setText(pdfPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

   @FXML
private void downloadCV(ActionEvent event) {
    portfolio selectedPortfolio = table.getSelectionModel().getSelectedItem();
    if (selectedPortfolio == null) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No portfolio item selected");
        alert.setContentText("Please select a portfolio item to download CV");
        alert.showAndWait();
    } else {
        try {
            // Retrieve CV file data from database
            pst = con.prepareStatement("SELECT cv FROM portfolio WHERE idportfolio = ?");
            pst.setString(1, selectedPortfolio.getId());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Blob cvBlob = rs.getBlob("cv");
                InputStream cvStream = cvBlob.getBinaryStream();
                
                // Show file chooser dialog to choose download location
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save CV");
                fileChooser.setInitialFileName(selectedPortfolio.getDesc() + ".pdf");
                File selectedFile = fileChooser.showSaveDialog(null);
                
                // Write CV data to file
                OutputStream outputStream = new FileOutputStream(selectedFile);
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = cvStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                cvStream.close();
                outputStream.close();
                
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("CV downloaded successfully");
                alert.showAndWait();
            } else {
                // Portfolio item not found in database
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Portfolio item not found");
                alert.setContentText("The selected portfolio item could not be found in the database");
                alert.showAndWait();
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("CV download failed");
            alert.setContentText("An error occurred while downloading the CV. Please try again later.");
            alert.showAndWait();
        }
    }
   
}


}
    
    
    
    
    
    
    
    
    
    
    
    



