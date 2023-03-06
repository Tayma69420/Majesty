/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.majesety.gui;

import edu.majesety.services.Categorie_recCrud;
import edu.majesty.entities.Categorie_rec;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Haith
 */
public class Categorie_recController implements Initializable {

    @FXML
    private Button add;
    @FXML
    private Button modifier;
    @FXML
    private Button del;
    @FXML
    private TextField typeField;
    @FXML
    private TableColumn<Categorie_rec, String> IdColumn;
    @FXML
    private TableColumn<Categorie_rec, String> typeColumn;
    @FXML
    private TableView<Categorie_rec> categorie_recTable;
    @FXML
    private Button btn_retour;
    
    @FXML
    private ComboBox comb;
    @FXML
    private Button stat;
    @FXML
    private Button mail;
    @FXML
    private Button locationBtn;

    /**
     * Initializes the controller class.
     */
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connect();
        affiche();
        ObservableList<String> list=FXCollections.observableArrayList("Refund","Echange","Retard","Satistfait");
        comb.setItems(list);
    }    

    @FXML
    private void handleAjouterCategorie_rec(ActionEvent event) {
         /*  if (typeField.getText().isEmpty() ) {
   
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs manquants");  
        alert.setContentText("Veuillez remplir tous les champs !");
        alert.showAndWait();
        return;
    }*/
           String type = typeField.getText();
          String selectedValue = comb.getSelectionModel().getSelectedItem().toString();
typeField.setText(selectedValue);
           
             // Créer un nouvel objet Emploi avec ces valeurs
    Categorie_rec newRec = new Categorie_rec(selectedValue);
    
    // Ajouter le nouvel emploi à la base de données

    Categorie_recCrud Categorie_recCrud= new Categorie_recCrud();
        
    Categorie_recCrud.createCategorie_rec(newRec);
    typeField.clear();
    
   
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText("categorie Reclamation ajouté avec succees");
    alert.showAndWait();
    categorie_recTable.getItems().clear();
            affiche();
    }
    
      public void affiche(){
        ObservableList<Categorie_rec>data=Categorie_recCrud.readAll();  
        
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
       
        
     
       categorie_recTable.setItems(data);
        categorie_recTable.refresh();
        
    }

    @FXML
    private void handleModifierCategorie_rec(ActionEvent event) {
          Categorie_rec selectedCategorie_rec = categorie_recTable.getSelectionModel().getSelectedItem();
    if (selectedCategorie_rec == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Aucun Reclamation sélectionné");
        alert.setContentText("Veuillez sélectionner une categorie reclamation à modifier !");
        alert.showAndWait();
        return;
    }
    if ( typeField.getText().isEmpty()) {
   
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs manquants");
        alert.setContentText("Veuillez remplir tous les champs !");
        alert.showAndWait();
        return;
    }
    

    String type = typeField.getText();
    
    
    // Update the selected Emploi object with the new values
    selectedCategorie_rec.setType(type);
    
   
    
    // Update the corresponding record in the database
    Categorie_recCrud Categorie_recCrud= new Categorie_recCrud();
    Categorie_recCrud.updateCategorie_rec(selectedCategorie_rec);
    
    // Clear the input fields and refresh the TableView
    typeField.clear();
    
  
    affiche();
    
    // Show a confirmation message
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Emploi modifié avec succès");
    alert.showAndWait();

    }
    

    @FXML
    private void DeleteHandler(ActionEvent event) {
        Categorie_rec selectedCategorie_rec = categorie_recTable.getSelectionModel().getSelectedItem();
    if (selectedCategorie_rec == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Aucun reclamation sélectionné");
        alert.setContentText("Veuillez sélectionner un emploi dans la table !");
        alert.showAndWait();
        return;
    }
     Categorie_recCrud ReclamationCrud= new Categorie_recCrud();
    ReclamationCrud.DeleteCategorie_rec(selectedCategorie_rec);
    categorie_recTable.getItems().remove(selectedCategorie_rec);
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Reclamation supprimé avec succees");
    alert.showAndWait();
    }

    @FXML
    private void SelectItems(MouseEvent event) {
          typeField.clear();
          typeField.appendText(categorie_recTable.getSelectionModel().getSelectedItem().getType());
    }

    @FXML
    private void retour(ActionEvent event) { 
          FXMLLoader loader = new FXMLLoader(getClass().getResource("acceuil.fxml"));
            try {
                Parent root = loader.load();
                btn_retour.getScene().setRoot(root);
                

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
    }

    @FXML
    private void select(ActionEvent event) {
        String s=comb.getSelectionModel().getSelectedItem().toString();
    }

  @FXML
private void viewStatistics(ActionEvent event) {
    Connect();
    try {
        // Query the database to get the statistics for each portfolio
        pst = con.prepareStatement("SELECT type, COUNT(*) FROM categorie_rec GROUP BY type");
        ResultSet rs = pst.executeQuery();

        // Create a PieChart object and set its title
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Statistics by categorie_rec");

        // Add each portfolio's statistics to the pie chart as a new slice
        while (rs.next()) {
            String id = rs.getString(1);
            int count = rs.getInt(2);
            PieChart.Data slice = new PieChart.Data(id, count);
            pieChart.getData().add(slice);
        }

        // Set the colors for the pie chart slices
        ObservableList<PieChart.Data> data = pieChart.getData();
        for (int i = 0; i < data.size(); i++) {
            data.get(i).getNode().setStyle("-fx-pie-color: #" + getRandomColor() + ";");
        }

        // Create a new scene and display the pie chart in a new window
        Scene scene = new Scene(pieChart, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    } catch (SQLException ex) {
        Logger.getLogger(Categorie_recController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

// Helper method to generate a random hex color code
private String getRandomColor() {
    Random random = new Random();
    return String.format("%02x%02x%02x", random.nextInt(256), random.nextInt(256), random.nextInt(256));
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

    @FXML
    private void send_mail(ActionEvent event) {
                                        try{   
     FXMLLoader loader =new FXMLLoader(getClass().getResource("SendMail.fxml"));
     Parent root=(Parent) loader.load();
    SendMailController controller =loader. getController();
    
    
    Scene scene= new Scene(root);
    Stage stage = new Stage() ;
    stage.setScene(scene);
    stage.show();
 
    }catch(IOException e) {
    System.out.append("erreur d'affichage ");
       
        }
    }

    @FXML
    private void handleLocation(ActionEvent event) {
     /* if (!navigator.geolocation) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Geolocation API n'est pas pris en charge par votre navigateur");
        alert.showAndWait();
        return;
    }

    // Get the current location using the Geolocation API
    navigator.geolocation.getCurrentPosition(position -> {
        // Display the current location in an alert dialog
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Location");
        alert.setHeaderText("Votre emplacement actuel:");
        alert.setContentText("Latitude: " + position.coords.latitude + "\nLongitude: " + position.coords.longitude);
        alert.showAndWait();
    }, error -> {
        // Display an error message if the Geolocation API could not get the location
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Impossible de récupérer votre emplacement actuel");
        alert.setContentText("Code d'erreur: " + error.code);
        alert.showAndWait();
    });  
    }*/

}
    
    
}

