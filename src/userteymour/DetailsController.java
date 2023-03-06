package userteymour;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import entities.panier;
import utils.MyConx;
import javafx.scene.control.TableView;


public class DetailsController {
    
   


    private Connection connection;

    @FXML
    private TableView<panier> panierTable;
    private TableColumn<panier, Integer> idpaniercol;
    @FXML
    private TableColumn<panier, String> nomcol;
    @FXML
    private TableColumn<panier, Float> prixcol;
    @FXML
    private TableColumn<panier, Integer> idprojetcol;
    @FXML
    private Button supprimerpan;
    @FXML
    private Button ckpan;
    private TextField nvprix;
    @FXML
    private TableColumn<?, ?> qntcol;
    @FXML
    private Button plus;
    @FXML
    private Button moins;

     private int cartCounter = 0;

    private int qnt;
    
    private ObservableList<panier> panierData;
    
public void affiche (){
    // Get database connection
    connection = new MyConx().getCnx();

    // Initialize table columns
    nomcol.setCellValueFactory(new PropertyValueFactory<>("nom"));
    prixcol.setCellValueFactory(new PropertyValueFactory<>("prix"));  
    qntcol.setCellValueFactory(new PropertyValueFactory<>("qnt"));


    // Load data into table
    ObservableList<panier> data = FXCollections.observableArrayList();
    try {
        String query = "SELECT idpanier, nom, SUM(prix * qnt) AS total, idprojet, SUM(qnt) AS qnt FROM panier GROUP BY nom";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            data.add(new panier(
                0,
                resultSet.getString("nom"),
                resultSet.getFloat("total"),
                resultSet.getInt("idprojet"),
                resultSet.getInt("qnt")
            ));
        }

    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }

    panierTable.setItems(data);



}
public void initialize() {
    // Get database connection
    connection = new MyConx().getCnx();
    
    


affiche();
}public int getIdUserForPanier(int idpanier) {
    int iduser = -1; // Default value in case the query doesn't return anything

    try {
        String query = "SELECT p.idpanier, u.iduser FROM panier p INNER JOIN utilisateur u ON p.iduser = u.iduser WHERE p.idpanier = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idpanier);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            iduser = resultSet.getInt("iduser");
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }

    return iduser;

}

public void addProjectToPanier(int idProjet, int quantity) {
    try {
        connection = new MyConx().getCnx();
        // Get project details from projet table
        String query = "SELECT * FROM projet WHERE idprojet = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idProjet);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            // Check if the project already exists in panier table
            String selectQuery = "SELECT * FROM panier WHERE idprojet = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, resultSet.getInt("idprojet"));
            ResultSet selectResultSet = selectStatement.executeQuery();

            if (selectResultSet.next()) {
                // Project already exists in panier table, update quantity
                int newQnt = selectResultSet.getInt("qnt") + quantity;
                String updateQuery = "UPDATE panier SET qnt = ? WHERE idprojet = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, newQnt);
                updateStatement.setInt(2, resultSet.getInt("idprojet"));
                updateStatement.executeUpdate();
            } else {
                // Add project to panier table
                int idpanier = 0; // Set the idpanier to 0, the database will generate a new id for us
                int iduser = getIdUserForPanier(idpanier); // Get the iduser for the new panier item
                String insertQuery = "INSERT INTO panier (idpanier, iduser, idprojet, nom, prix, qnt) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, idpanier);
                insertStatement.setInt(2, iduser);
                insertStatement.setInt(3, resultSet.getInt("idprojet"));
                insertStatement.setString(4, resultSet.getString("titreprojet"));
                insertStatement.setDouble(5, resultSet.getDouble("prixprojet"));
                insertStatement.setInt(6, quantity);
                insertStatement.executeUpdate();
                
            }
            
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }
}


public String getTableViewContent() {
    String content = "";
ObservableList<panier> productList = panierTable.getItems();

    for (panier product : productList) {
        content += product.getNom() + ", " + product.getPrix() + "\n";
    }
    return content;
}


 @FXML
private void supppan(ActionEvent event) {
       // Get the selected panier item
    panier selectedPanier = panierTable.getSelectionModel().getSelectedItem();
    if (selectedPanier == null) {
        // No item selected, show an alert and return
        Alert alert = new Alert(AlertType.WARNING, "Veuillez sélectionner un élément à supprimer.");
        alert.showAndWait();
        return;
    }

    // Confirm the deletion with the user
    Alert alert = new Alert(AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer l'élément sélectionné?");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        // Remove the selected item from the table's ObservableList
        panierTable.getItems().remove(selectedPanier);

        // Delete the corresponding row from the database
        try {
            connection = new MyConx().getCnx();
            String query = "DELETE FROM panier WHERE nom = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedPanier.getNom());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    
}


    @FXML
    private void checkpan(ActionEvent event) {
         try {
        // Load Checkout.fxml view
        Parent root = FXMLLoader.load(getClass().getResource("Checkout.fxml"));
        Scene scene = new Scene(root);

        // Get the stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Set the new scene on the stage
        window.setScene(scene);
        window.show();
    } catch (IOException e) {
        System.err.println(e.getMessage());
    }
         
}
private void refreshPanierTable() {
    ObservableList<panier> data = FXCollections.observableArrayList();
    try {
        String query = "SELECT nom, SUM(prix) AS total, COUNT(*) AS qnt FROM panier GROUP BY nom";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            data.add(new panier(
                    0,
                    resultSet.getString("nom"),
                    resultSet.getFloat("total"),
                    0,
                    resultSet.getInt("qnt")));
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }

    panierTable.setItems(data);
}

    @FXML
    private void moinsqnt(ActionEvent event) {
          // Get selected item
    panier selectedItem = panierTable.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
        try {
            // Update item quantity in table view
            selectedItem.setQnt(selectedItem.getQnt() - 1);
            panierTable.refresh();

            // Update item quantity in database
            addProjectToPanier(selectedItem.getIdProjet(), -1);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }}        panierTable.refresh();
        affiche();
    }

    @FXML
    private void plusqnt(ActionEvent event)  {
          // Get selected item
    panier selectedItem = panierTable.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
        try {
            // Update item quantity in table view
            selectedItem.setQnt(selectedItem.getQnt() + 1);
            panierTable.refresh();

            // Update item quantity in database
            addProjectToPanier(selectedItem.getIdProjet(), 1);
        } catch (Exception e) {
            System.err.println(e.getMessage());
       
        }
        panierTable.refresh();
        affiche();
    }

 
    
}
}