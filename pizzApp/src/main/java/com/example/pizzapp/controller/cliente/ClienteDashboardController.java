package com.example.pizzapp.controller.cliente;

import com.example.pizzapp.model.entities.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClienteDashboardController implements Initializable {

    @FXML
    private Button bttAddOrder;

    @FXML
    private Button bttFedelityCard;

    @FXML
    private Button bttOrderStatus;

    @FXML
    private Button bttPastOrder;

    @FXML
    private Button bttReview;

    @FXML
    private Button bttShowMenu;

    @FXML
    private Label lbWelcome;

    @FXML
    private AnchorPane mainStage;

    @FXML
    void showAddOrderPage(ActionEvent event) {
        try {
            AnchorPane tmp = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/fxmlFile/addOrderPage.fxml"));
            setNode(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void showMenuPage(ActionEvent event) {
        try {
            AnchorPane tmp = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/fxmlFile/cliente/clienteShowMenuPage.fxml"));
            setNode(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void showOrderStatusPage(ActionEvent event) {
        try {
            AnchorPane tmp = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/fxmlFile/cliente/clienteOrderStatusPage.fxml"));
            setNode(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void showReviewPage(ActionEvent event) {
        try {
            AnchorPane tmp = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/fxmlFile/cliente/clienteReviewPage.fxml"));
            setNode(tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.lbWelcome.setText("Bentornato, " + User.getNome());
    }

    private void setNode(Node n) {
        this.mainStage.getChildren().clear();
        this.mainStage.getChildren().add(n);
    }

    @FXML
    void logout(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/login.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        this.bttAddOrder.getScene().getWindow().hide();
        stage.show();
    }
}
