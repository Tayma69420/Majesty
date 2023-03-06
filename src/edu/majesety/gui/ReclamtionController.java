/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.majesety.gui;

import edu.majesety.services.ReclamationCrud;
import edu.majesty.entities.Reclamation;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.mail.MessagingException;

/**
 * FXML Controller class
 *
 * @author dhiaa
 */
public class ReclamtionController implements Initializable {

    @FXML
    private TextArea descriptionField;
    @FXML
    private TableView<Reclamation> reclamationTable;
    @FXML
    private TableColumn<Reclamation,String> IdColumn;
    @FXML
    private TableColumn<Reclamation,String> descriptionColumn;
    @FXML
    private Button add;
    @FXML
    private Button modifier;
    @FXML
    private Button del;
    @FXML
    private TableColumn<?, ?> datarecColumn;
    @FXML
    private TableColumn<?, ?> iduserrColumn;
    @FXML
    private TextField titreField;
    @FXML
    private Button btn_return;
    @FXML
    private TableColumn<Reclamation, String> titreCol;
    @FXML
    private TextField rech;

    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        affiche();
    }    

  @FXML
private void handleAjouterReclamation(ActionEvent event) throws MessagingException {
    if (descriptionField.getText().isEmpty() || titreField.getText().isEmpty()) {
        // Display error message if either field is empty
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs manquants");  
        alert.setContentText("Veuillez remplir tous les champs !");
        alert.showAndWait();
        return;
    }

    // Check if description contains any forbidden words and replace them with asterisks
    String recla_desc = descriptionField.getText();
    boolean containsForbiddenWords = false;
    if (recla_desc.toLowerCase().contains("fuck") || recla_desc.toLowerCase().contains("namm") || recla_desc.toLowerCase().contains("zboub")) {
        recla_desc = recla_desc.replaceAll("(?i)fuck|namm|zboub", "******");
        containsForbiddenWords = true;
    }

    // Create a new Reclamation object with the entered values
    String titre = titreField.getText();
    Reclamation newRec = new Reclamation(titre, recla_desc);
    
    // Add the new reclamation to the database
    ReclamationCrud ReclamationCrud = new ReclamationCrud();
    ReclamationCrud.createReclamation(newRec);
    titreField.clear();
    descriptionField.clear();
   
    // Display confirmation message
    Alert alert;
    if (containsForbiddenWords) {
        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText("Le message contient des mots interdits");
        alert.setContentText("Les mots interdits ont été remplacés par des astérisques.");
    } else {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Reclamation ajouté avec succès");
    }
    alert.showAndWait();

    // Refresh the table with the updated data
    reclamationTable.getItems().clear();
    affiche();
}







// Define a filter to use for the table data
private final FilteredList<Reclamation> filteredData = new FilteredList<>(ReclamationCrud.readAll(), p -> true);

public void affiche() {
    titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
    IdColumn.setCellValueFactory(new PropertyValueFactory<>("id_reclamation"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("recla_desc"));

    // Set up the table with the filtered data
    reclamationTable.setItems(filteredData);
    reclamationTable.refresh();
    
    // Add a listener to the search field to filter the data based on user input
    rech.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(reclamation -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();
            if (reclamation.getTitre().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches titre
            }
            return false; // Does not match
        });
    });
}

    @FXML
    private void SelectItems(MouseEvent event) {
    titreField.clear();
    titreField.appendText(reclamationTable.getSelectionModel().getSelectedItem().getTitre());
    descriptionField.clear();
    descriptionField.appendText(reclamationTable.getSelectionModel().getSelectedItem().getRecla_desc());
        
    }

    @FXML
    private void handleModifierReclamation(ActionEvent event) {
          Reclamation selectedReclamation = reclamationTable.getSelectionModel().getSelectedItem();
    if (selectedReclamation == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Aucun Reclamation sélectionné");
        alert.setContentText("Veuillez sélectionner une reclamation à modifier !");
        alert.showAndWait();
        return;
    }
    if ( titreField.getText().isEmpty()) {
   
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs manquants");
        alert.setContentText("Veuillez remplir tous les champs !");
        alert.showAndWait();
        return;
    }
    if ( descriptionField.getText().isEmpty()) {
   
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Champs manquants");
        alert.setContentText("Veuillez remplir tous les champs !");
        alert.showAndWait();
        return;
    }

    String titre = titreField.getText();
    String description = descriptionField.getText();
    
    // Update the selected Emploi object with the new values
    selectedReclamation.setTitre(titre);
    selectedReclamation.setRecla_desc(description);
   
    
    // Update the corresponding record in the database
    ReclamationCrud ReclamationCrud= new ReclamationCrud();
    ReclamationCrud.updateReclamatio(selectedReclamation);
    
    // Clear the input fields and refresh the TableView
    titreField.clear();
    descriptionField.clear();
  
    affiche();
    
    // Show a confirmation message
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Reclamation modifié avec succès");
    alert.showAndWait();

    }
    
   

    @FXML
    private void DeleteHandler(ActionEvent event) {
          Reclamation selectedReclamation = reclamationTable.getSelectionModel().getSelectedItem();
    if (selectedReclamation == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Aucun reclamation sélectionné");
        alert.setContentText("Veuillez sélectionner une reclamation dans la table !");
        alert.showAndWait(); 
        return;
    } 
    
     
   
     // Show a confirmation message
     Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
     
    alert.setTitle("Confirmation");
    alert.setHeaderText("Voulez vous supprimer?");
    alert.showAndWait();
    ReclamationCrud ReclamationCrud= new ReclamationCrud();
    ReclamationCrud.deleteReclamation(selectedReclamation);
    reclamationTable.getItems().remove(selectedReclamation);
    }

    @FXML
    private void retour(ActionEvent event) { 
          FXMLLoader loader = new FXMLLoader(getClass().getResource("acceuil.fxml"));
            try {
                Parent root = loader.load();
                btn_return.getScene().setRoot(root);
                

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
    }
    
}
