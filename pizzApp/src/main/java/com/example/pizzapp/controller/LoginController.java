package com.example.pizzapp.controller;

import com.example.pizzapp.model.entities.user.User;
import com.example.pizzapp.utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {
    @FXML
    private Button bttLogin;

    @FXML
    private Button bttRegistrati;

    @FXML
    private Label lbStart;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    @FXML
    public void login() {
        String loginQuery = "SELECT * FROM Utenti WHERE email = ? AND password = ?";
        this.connect = DatabaseConnection.connectDb();
        try{
            this.pst = this.connect.prepareStatement(loginQuery);
            this.pst.setString(1, this.txtEmail.getText());
            this.pst.setString(2, this.txtPassword.getText());
            this.rs = this.pst.executeQuery();

            if (this.rs.next()) {
                User.createUser(this.rs.getInt("codUtente"), this.rs.getString("nome"),
                        this.rs.getString("cognome"), this.rs.getString("email"),
                        this.rs.getString("telefono"), this.rs.getString("tipo"));
                String fileToLoad = "";
                if (User.getTipo().equals("pizzaiolo") || User.getTipo().equals("aiuto cuoco")) {
                    fileToLoad = "/com/example/pizzapp/fxmlFile/cucina/cucinaDashboard.fxml";
                } else {
                    fileToLoad = "/com/example/pizzapp/fxmlFile/" + User.getTipo() + "/" + User.getTipo() + "Dashboard.fxml";
                }
                try {
                    Parent root = FXMLLoader.load(getClass().getResource(fileToLoad));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    this.bttLogin.getScene().getWindow().hide();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            this.pst.close();
            this.connect.close();
            System.out.println("NUOVO USER CREATO: " + User.getTipo());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void openSignupPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/fxmlFile/signUp.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            this.bttLogin.getScene().getWindow().hide();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}