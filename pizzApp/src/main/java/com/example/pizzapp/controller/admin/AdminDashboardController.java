package com.example.pizzapp.controller.admin;

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
    @FXML
    public void showInsertOrder(ActionEvent event) {
        try {
            AnchorPane tmp = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/fxmlFile/addOrderPage.fxml"));
            setNode(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void showEmployee() {
        try {
            AnchorPane tmp = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/fxmlFile/admin/adminEmployeeManagementPage.fxml"));
            setNode(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void showMenuManager() {
        try {
            AnchorPane tmp = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/fxmlFile/admin/adminMenuManager.fxml"));
            setNode(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void showCouponManager() {
        try {
            AnchorPane tmp = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/fxmlFile/admin/adminCouponManager.fxml"));
            setNode(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setNode(Node n) {
        this.mainStage.getChildren().clear();
        this.mainStage.getChildren().add(n);
    }
}
