/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userteymour;

//import com.stripe.exception.StripeException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//import com.stripe.Stripe;
//import com.stripe.model.PaymentIntent;


/**
 * FXML Controller class
 *
 * @author farouk daadaa
 */
public class StripePaymentController implements Initializable {

    @FXML
    private TextField cnbr;
    @FXML
    private TextField exd;
    @FXML
    private PasswordField scod;
    @FXML
    private Label payamount;
    @FXML
    private Label tot;
    @FXML
    private TextField stra;
    @FXML
    private TextField cty;
    @FXML
    private TextField ste;
    @FXML
    private TextField zip;
    @FXML
    private TextField ctry;
    @FXML
    private Button conf;
    @FXML
    private Button ret;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void cardnbr(ActionEvent event) {
    }

    @FXML
    private void exDate(ActionEvent event) {
    }

    @FXML
    private void seccode(ActionEvent event) {
    }

    @FXML
    private void streetAddrss(ActionEvent event) {
    }

    @FXML
    private void city(ActionEvent event) {
    }

    @FXML
    private void state(ActionEvent event) {
    }

    @FXML
    private void zipCode(ActionEvent event) {
    }

    @FXML
    private void country(ActionEvent event) {
    }

    @FXML
    
private void confirm(ActionEvent event) {
    // Validate card number
    String cardNumber = cnbr.getText();
    if (!cardNumber.matches("^\\d{16}$")) {
       Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Invalid card number\", \"Please enter a 16-digit card number.");
        alert.showAndWait();
        return;
    }
    
    // Validate expiration date
    String expDate = exd.getText();
    if (!expDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Invalid expiration date\", \"Please enter the expiration date in the format MM/YY.");
        alert.showAndWait();
        return;
    }
    
    // Validate security code
    String securityCode = scod.getText();
    if (!securityCode.matches("^\\d{3,4}$")) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Invalid security code\", \"Please enter a 3 or 4 digit security code.");
        alert.showAndWait();
        return;
    }
    
    // Validate street address
    String streetAddress = stra.getText();
    if (streetAddress.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Missing street address\", \"Please enter a street address.");
        alert.showAndWait();
        return;
    }
    
    // Validate city
    String city = cty.getText();
    if (city.isEmpty()) {
       Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Missing city\", \"Please enter a city.");
        alert.showAndWait();
        return;
    }
    
    // Validate state
    String state = ste.getText();
    if (state.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Missing state\", \"Please enter a state.");
        alert.showAndWait();
        return;
    }
    
    // Validate zip code
    String zipCode = zip.getText();
    if (!zipCode.matches("^\\d{5}$")) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Invalid zip code\", \"Please enter a 5-digit zip code.");
        alert.showAndWait();
        return;
    }
    
    // Validate country
    String country = ctry.getText();
    if (country.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText("Missing country\", \"Please enter a country.");
        alert.showAndWait();
        return;
    }
    
    // Get payment amount
    int paymentAmount = Integer.parseInt(payamount.getText().substring(1));
    
    // Create payment intent
    Map<String, Object> automaticPaymentMethods = new HashMap<>();
    automaticPaymentMethods.put("enabled", true);
    Map<String, Object> params = new HashMap<>();
    params.put("amount", paymentAmount);
    params.put("currency", "usd");
    params.put("payment_method_types", Collections.singletonList("card"));
    params.put("automatic_payment_methods", automaticPaymentMethods);
    Map<String, Object> paymentMethodParams = new HashMap<>();
    paymentMethodParams.put("card[number]", cardNumber);
    paymentMethodParams.put("card[exp_month]", expDate.substring(0, 2));
    paymentMethodParams.put("card[exp_year]", "20" + expDate.substring(3, 5));
    paymentMethodParams.put("card[cvc]", securityCode);
    Map<String, Object> billingDetails = new HashMap<>();
    billingDetails.put("address", Collections.singletonMap("line1", streetAddress));
    billingDetails.put("address_city", city);
    billingDetails.put("address_state", state);
    billingDetails.put("address_zip", zipCode);
    billingDetails.put("address_country", country);
    paymentMethodParams.put("billing_details", billingDetails);
    params.put("payment_method_data", Collections.singletonMap("card", paymentMethodParams));
//    PaymentIntent paymentIntent;
//    try {
//        paymentIntent = PaymentIntent.create(params);
//    } catch (StripeException e) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Erreur de saisie");
//        alert.setHeaderText(null);
//        alert.setContentText("Le numéro de carte doit contenir 16 chiffres.");
//        alert.showAndWait();
//    }
}


    @FXML
    private void retrn(ActionEvent event) {
         // Charger la vue Details.fxml
    FXMLLoader loader = new FXMLLoader(getClass().getResource("details.fxml"));
    Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(DetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    // Créer une nouvelle scène
    Scene scene = new Scene(root);

    // Obtenir la fenêtre actuelle et la remplacer par la nouvelle scène
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
    }
    
}
