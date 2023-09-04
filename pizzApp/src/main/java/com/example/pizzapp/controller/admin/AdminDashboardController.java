package com.example.pizzapp.controller.admin;

import com.example.pizzapp.model.entities.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    @FXML
    private Button bttAddOrder;

    @FXML
    private Button bttEmployeeManager;

    @FXML
    private Button bttMenuManager;

    @FXML
    private Label lbWelcome;

    @FXML
    private AnchorPane mainStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.lbWelcome.setText("Bentornato, " + User.getNome());
    }
}
