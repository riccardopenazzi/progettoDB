package com.example.pizzapp.controller;

import com.example.pizzapp.utils.UserData;
import com.example.pizzapp.utils.database.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        this.connect = Database.connectDb();
        try{
            this.pst = this.connect.prepareStatement(loginQuery);
            this.pst.setString(1, this.txtEmail.getText());
            this.pst.setString(2, this.txtPassword.getText());
            this.rs = this.pst.executeQuery();

            if(this.rs.next()){
                UserData.setCodUtente(25);
                this.lbStart.setText("OK eseguitoooo!!!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
