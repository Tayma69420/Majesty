/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.mycompany.myapp.entities.Panier;
import com.mycompany.myapp.services.PanierService;
import java.util.ArrayList;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.TextField;
import com.mycompany.myapp.utils.Statics;
import java.util.Map;

public class PanierForm extends Form {
    private Container cartContainer;

    public PanierForm(Form previous) {
       super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
 // Add a text field and a button for the discount code
    TextField discountCodeField = new TextField("", "Discount code");
    Button discountButton = new Button("Apply");

    discountButton.addActionListener(evt -> {
        // Check if the discount code is valid
        if (discountCodeField.getText().equals("Code10")) {
            // Calculate the discount
            double discount = getTotalPrice() * 0.1; // 10% discount

            // Display the total price with the discount applied
            Label totalPriceLabel = new Label("Total price: " + (getTotalPrice() - discount));
            cartContainer.add(totalPriceLabel);
        } else {
            // Display an error message if the discount code is invalid
            Dialog.show("Error", "Invalid discount code", "OK", null);
        }
    });

    // Add the text field and the button to the form
    addAll(discountCodeField, discountButton);
        setTitle("My Cart");
        setLayout(BoxLayout.y());

        Button sendEmailButton = new Button("Checkout");
        cartContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        add(cartContainer);

        showCartItems();
       sendEmailButton.addActionListener(evt -> {
    // Show a confirmation dialog before clearing the cart and database
    boolean confirmed = Dialog.show("Confirmation", "Are you sure you want to checkout?", "Yes", "No");
    if (confirmed) {
        // Clear the cart
        PanierService.getInstance().clearCart();

        // Delete all items from the database
        PanierService.getInstance().clearDatabase();

        // Refresh the cart items
        showCartItems();

        // Send the email
        String url = "http://127.0.0.1:8000/sendEmailPrix";
        ConnectionRequest request = new ConnectionRequest(url);
        request.setPost(false);
        request.addResponseListener(response -> {
            if (response.getResponseCode() == 200) {
                Dialog.show("Success", "Email sent", "OK", null);
            } else {
                Dialog.show("Error", "Failed to send email", "OK", null);
            }
        });
        NetworkManager.getInstance().addToQueue(request);
    }
});
        addAll(sendEmailButton);
    }

  private void showCartItems() {
    ArrayList<Panier> cart = PanierService.getInstance().getCart();

    // Clear the existing content of the cart container
    cartContainer.removeAll();

    if (cart.isEmpty()) {
        Label emptyLabel = new Label("Your cart is empty");
        cartContainer.add(emptyLabel);
    } else {
        for (Panier item : cart) {
            Container itemContainer = new Container(new FlowLayout());
            itemContainer.add(new Label("id: " + item.getIdprojet()));
            itemContainer.add(new Label(item.getNom()));
            itemContainer.add(new Label(String.valueOf(item.getPrix())));
            itemContainer.add(new Label("Quantity: " + item.getQnt()));

            Button incrementButton = new Button("+");
            incrementButton.addActionListener(evt -> {
                PanierService.getInstance().incrementCartItem(item);
                showCartItems(); // Refresh the cart items after incrementing
            });

            Button decrementButton = new Button("-");
            decrementButton.addActionListener(evt -> {
                PanierService.getInstance().decrementCartItem(item);
                showCartItems(); // Refresh the cart items after decrementing
            });

            Label totalPriceLabel = new Label("Total: " + item.getPrix() * item.getQnt());
            itemContainer.add(totalPriceLabel);

            Button deleteButton = new Button("Delete");
            deleteButton.addActionListener(evt -> {
                // Show a confirmation dialog before deleting the item
                boolean confirmed = Dialog.show("Confirmation", "Are you sure you want to delete this item?", "Yes", "No");
                if (confirmed) {
                    String url = "http://127.0.0.1:8000/deletePanierJSON/" + item.getIdpanier();
                    ConnectionRequest request = new ConnectionRequest(url);
                    request.setHttpMethod("DELETE");
                    request.addResponseListener(response -> {
                        if (response.getResponseCode() == 200) {
                            Dialog.show("Success", "Cart item deleted", "OK", null);
                            PanierService.getInstance().removeCartItem(item);
                            showCartItems(); // Refresh the cart items after deleting
                        } else {
                            Dialog.show("Error", "Failed to delete cart item", "OK", null);
                        }
                    });
                    NetworkManager.getInstance().addToQueue(request);
                }
            });

            itemContainer.add(deleteButton);
            itemContainer.add(decrementButton);
            itemContainer.add(incrementButton);

            cartContainer.add(itemContainer);
        }

        // Display the total price
        Label totalPriceLabel = new Label("Total price: " + PanierService.getInstance().getTotalPrice());
        cartContainer.add(totalPriceLabel);
    }
}
private double getTotalPrice() {
    ArrayList<Panier> cart = PanierService.getInstance().getCart();
    double totalPrice = 0;

    for (Panier item : cart) {
        totalPrice += item.getPrix() * item.getQnt();
    }

    return totalPrice;
}

}

