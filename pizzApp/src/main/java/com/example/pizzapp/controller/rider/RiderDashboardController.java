package com.example.pizzapp.controller.rider;

import com.example.pizzapp.model.entities.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RiderDashboardController implements Initializable {

    @FXML
    private Button bttShowAvailable;

    @FXML
    private Button bttShowCurrent;

    @FXML
    private Button bttShowMade;

    @FXML
    private Label lbWelcome;

    @FXML
    private AnchorPane mainStage;

    @FXML
    void showAvailable(ActionEvent event) {
        try {
            AnchorPane tmp = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/fxmlFile/rider/riderAvailablePage.fxml"));
            setNode(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void showCurrent(ActionEvent event) {
        try {
            AnchorPane tmp = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/fxmlFile/rider/riderCurrentPage.fxml"));
            setNode(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void showMade(ActionEvent event) {
        try {
            AnchorPane tmp = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/fxmlFile/rider/riderMadePage.fxml"));
            setNode(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setNode(Node n) {
        this.mainStage.getChildren().clear();
        this.mainStage.getChildren().add(n);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.lbWelcome.setText("Bentornato " + User.getNome());
    }
}
