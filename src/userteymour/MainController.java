/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userteymour;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author farouk daadaa
 */
public class MainController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField cardNumberField;
    @FXML
    private TextField securityCodeField;
    @FXML
    private RadioButton visaRadioButton;
    @FXML
    private RadioButton mastercardRadioButton;
    @FXML
    private Button submitBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
