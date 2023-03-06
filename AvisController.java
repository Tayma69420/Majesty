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
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    private TableView<portfolio> table2;
    
    @FXML
    private TableColumn<portfolio,String>id_portfolio;
    
    @FXML
    private TableColumn<portfolio,String>description;
    
    @FXML
    private TableColumn<portfolio,String>cv;
    
  
    @FXML
    private Button ptback;
    
    private portfolio selectedPortfolio;
    private avis selectedavis;
    
    
    @FXML
    private TableView<avis> table3;
    @FXML
    private TableColumn<avis,String> idav;
    @FXML
    private TableColumn<avis,String> avav;
    @FXML
    private Button supprimer;
    @FXML
    private Button stat;

    
@FXML
void addavis(ActionEvent event) {
    String commentaire;
    Connect();
    commentaire = avpt.getText();

    // Perform input validation and replace "bad" with asterisks
    if (commentaire.equalsIgnoreCase("bad")) {
        commentaire = "********";
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Validation");
        alert.setHeaderText("Invalid Input");
        alert.setContentText("The input contains a banned word.");
        alert.showAndWait();
    }

    try {
        pst = con.prepareStatement("INSERT INTO avis (commentaire, idportfolio) VALUES (?, ?)");
        pst.setString(1, commentaire);
        pst.setString(2, selectedPortfolio.getId());
        pst.executeUpdate();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Test Connection");
        alert.setHeaderText("Avis Added");
        alert.setTitle("SUCCESS");
        alert.showAndWait();

        avpt.setText("");
        avpt.requestFocus();

    } catch (SQLException ex) {
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
    }
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
portfolio.add(pt) ;
       }
    }
                table2.setItems(portfolio) ;
    id_portfolio.setCellValueFactory(f->f.getValue().idProperty());
    description.setCellValueFactory(f->f.getValue().descProperty());
    cv.setCellValueFactory(f->f.getValue ().cvProperty()) ;
               
       }
      
       catch (SQLException ex)
       {
           Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
       }
 
                table2.setRowFactory(tv -> {
    TableRow<portfolio> myRow = new TableRow<>();
    myRow.setOnMouseClicked(event -> {
        if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
            selectedPortfolio = myRow.getItem();
        }
    });
    return myRow;
});
      }
    
 
   
   
public void table3() {
    Connect();
    ObservableList<avis> aviList = FXCollections.observableArrayList();
    try {
        pst = con.prepareStatement("SELECT * FROM avis INNER JOIN portfolio ON avis.idportfolio = portfolio.idportfolio");
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            avis av = new avis();
            av.setidportfolio(rs.getString("idportfolio"));
            av.setcommentaire(rs.getString("commentaire"));
            aviList.add(av);
        }
    } catch (SQLException ex) {
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
    }

    table3.setItems(aviList);
    idav.setCellValueFactory(f->f.getValue().idportfolioProperty());
    avav.setCellValueFactory(f->f.getValue().commentaireProperty());

    table2.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            String selectedId = newSelection.getId(); // assuming the portfolio class has a getId() method
            ObservableList<avis> filteredAvisList = FXCollections.observableArrayList();
            for (avis av : aviList) {
                if (av.getidportfolio().equals(selectedId)) { // assuming the avis class has a getidportfolio() method
                    filteredAvisList.add(av);
                }
            }
            table3.setItems(filteredAvisList);
        }
    });

    table3.setRowFactory(tv -> {
        TableRow<avis> myRow = new TableRow<>();
        myRow.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                selectedavis = myRow.getItem();
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

    
    public void deleteSelectedAvis() {
    if(selectedavis != null) {
        try {
            pst = con.prepareStatement("DELETE FROM avis WHERE idportfolio = ?");
            pst.setString(1, selectedavis.getidportfolio());
            int result = pst.executeUpdate();
            if(result > 0) {
                table3.getItems().remove(selectedavis);
                selectedavis = null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    
    
    @FXML
    private void suppavis(ActionEvent event) {
         // Boîte de dialogue de confirmation pour la modification/suppression
 Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Supprimer le produit?");
    alert.setContentText("Êtes-vous sûr de vouloir supprimer ce produit?");
    Optional<ButtonType> result = alert.showAndWait();
    
    if (result.get() == ButtonType.OK) {
      deleteSelectedAvis();
    }}

  @FXML
private void viewStatistics(ActionEvent event) {
    Connect();
    try {
        // Query the database to get the statistics for each portfolio
        pst = con.prepareStatement("SELECT portfolio.description, COUNT(*) FROM avis "
                + "INNER JOIN portfolio ON avis.idportfolio = portfolio.idportfolio "
                + "GROUP BY portfolio.description");
        ResultSet rs = pst.executeQuery();

        // Create a PieChart object and set its title
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Statistics by Portfolio");

        // Add each portfolio's statistics to the pie chart as a new slice
        while (rs.next()) {
            String description = rs.getString(1);
            int count = rs.getInt(2);
            PieChart.Data slice = new PieChart.Data(description, count);
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
        Logger.getLogger(AvisController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

// Helper method to generate a random hex color code
private String getRandomColor() {
    Random random = new Random();
    return String.format("%02x%02x%02x", random.nextInt(256), random.nextInt(256), random.nextInt(256));
}
       
    
}
