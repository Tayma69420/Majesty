/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.majesety.gui;

/**
 *
 * @author Ahmed
 */
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class stat {
     public void start(Stage stage) {
        // Creating the pie chart
        PieChart pieChart = new PieChart();
        
        // Adding data to the chart
        pieChart.getData().add(new PieChart.Data("Apples", 20));
        pieChart.getData().add(new PieChart.Data("Oranges", 30));
        pieChart.getData().add(new PieChart.Data("Bananas", 50));
        
        // Customizing the colors of the slices
        for (PieChart.Data data : pieChart.getData()) {
            switch (data.getName()) {
                case "Apples":
                    data.getNode().setStyle("-fx-pie-color: #ff6347;");
                    break;
                case "Oranges":
                    data.getNode().setStyle("-fx-pie-color: #ffd700;");
                    break;
                case "Bananas":
                    data.getNode().setStyle("-fx-pie-color: #8fbc8f;");
                    break;
            }
        }
        
        // Creating a VBox to hold the chart
        VBox vbox = new VBox(pieChart);
        
        // Creating a scene and displaying it
        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
        stage.show();
    }
 
    
}

