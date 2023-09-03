package com.example.pizzapp.controller;

import com.example.pizzapp.utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignupController {

    @FXML
    private Button bttSignup;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtSurname;

    @FXML
    private TextField txtTelefono;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    @FXML
    public void register() {
        String signupQuery = "INSERT INTO Utenti (nome, cognome, email, telefono, password, tipo) VALUES (?, ?, ?, ?, ?, 'cliente')";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(signupQuery);
            this.pst.setString(1, this.txtName.getText());
            this.pst.setString(2, this.txtSurname.getText());
            this.pst.setString(3, this.txtEmail.getText());
            this.pst.setString(4, this.txtTelefono.getText());
            this.pst.setString(5, this.txtPassword.getText());

            int res = this.pst.executeUpdate();

            if(res > 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Registrazione effettuata");
                alert.setHeaderText(null);
                alert.setContentText("Registrazione avvenuta con successo! Ora puoi tornare alla schermata " +
                        "precedente ed effettuare il login.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore registrazione");
                alert.setHeaderText(null);
                alert.setContentText("Errore nella registrazione, controlla di aver inserito tutti i campi");
                alert.showAndWait();
            }

            this.pst.close();
            this.connect.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void backToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/pizzapp/login.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            this.bttSignup.getScene().getWindow().hide();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
