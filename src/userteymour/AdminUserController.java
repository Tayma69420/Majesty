package userteymour;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import entities.User;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.UserService;
import utils.MyConx;

public class AdminUserController {

    @FXML
    private TableView<User> utilisateur;

    @FXML
    private TableColumn<User, Integer> idusercol;

    @FXML
    private TableColumn<User, String> nomcol;

    @FXML
    private TableColumn<User, String> prenomcol;

    @FXML
    private TableColumn<User, String> emailcol;

    @FXML
    private TableColumn<User, String> telcol;

    @FXML
    private TableColumn<User, String> adrcol;

    @FXML
    private TableColumn<User, Integer> agecol;

    @FXML
    private TableColumn<User, String> passwdcol;

    @FXML
    private TableColumn<User, Integer> idrolecol;

    private ObservableList<User> data;

    private UserService userService;

    @FXML
    private Button supprimer;
    @FXML
    private Button modifier;
    
    private User selectedUser;
    @FXML
    private TextField teltf;
    @FXML
    private TextField passwdtf;
    @FXML
    private TextField emailtf;
    @FXML
    private TextField agetf;
    @FXML
    private TextField adrtf;
    @FXML
    private TextField pretf;
    @FXML
    private TextField nomtf;
    

    public void initialize() {
        // Initialize the columns
        idusercol.setCellValueFactory(new PropertyValueFactory<>("iduser"));
        nomcol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomcol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
        telcol.setCellValueFactory(new PropertyValueFactory<>("tel"));
        adrcol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        agecol.setCellValueFactory(new PropertyValueFactory<>("age"));
        passwdcol.setCellValueFactory(new PropertyValueFactory<>("passwd"));
        idrolecol.setCellValueFactory(new PropertyValueFactory<>("id_role"));

        // Initialize the data
        userService = new UserService() {};
        data = FXCollections.observableArrayList(userService.readAll());
        utilisateur.setItems(data);

       
    }

@FXML

private void delete(ActionEvent event) {
    // Get the selected user from the table view
    User selectedUser = utilisateur.getSelectionModel().getSelectedItem();

    // Check that a user is actually selected
    if (selectedUser != null) {
        // Delete the user from the database and remove it from the table view
        userService.delete(selectedUser);
        data.remove(selectedUser);
    }
}

    @FXML
    private void modifieruser(ActionEvent event) {
    }

        }
    
