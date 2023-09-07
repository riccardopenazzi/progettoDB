package com.example.pizzapp.controller;

import com.example.pizzapp.model.entities.user.User;
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

    @FXML
    private TextField txtComune;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtVia;

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
                ResultSet gen = this.connect.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
                int userId = 0;
                if (gen.next()) {
                    userId = gen.getInt(1);
                }
                String query = "INSERT INTO Carte_fedelta VALUES ()";
                this.pst = this.connect.prepareStatement(query);
                this.pst.executeUpdate();
                int cardId = 0;
                gen = this.connect.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
                if (gen.next()) {
                    cardId = gen.getInt(1);
                }
                String query2 = "INSERT INTO Appartenenze (utente, cartaFedelta) VALUES (?, ?)";
                this.pst = this.connect.prepareStatement(query2);
                this.pst.setInt(1, userId);
                this.pst.setInt(2, cardId);
                this.pst.executeUpdate();

                String query3 = "INSERT IGNORE INTO Indirizzi (via, numero, comune) VALUES (?, ?, ?)";
                PreparedStatement tmpPst = this.connect.prepareStatement(query3);
                tmpPst.setString(1, txtVia.getText());
                tmpPst.setInt(2, Integer.parseInt(txtNumero.getText()));
                tmpPst.setString(3, txtComune.getText());
                tmpPst.executeUpdate();

                String query4 = "INSERT INTO Domiciliazioni (utente, via, numero, comune)\n" +
                        "VALUES (?, ?, ?, ?);";
                PreparedStatement tmpPst2 = this.connect.prepareStatement(query4);
                tmpPst2.setInt(1, userId);
                tmpPst2.setString(2, txtVia.getText());
                tmpPst2.setInt(3, Integer.parseInt(txtNumero.getText()));
                tmpPst2.setString(4, txtComune.getText());
                tmpPst2.executeUpdate();

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
