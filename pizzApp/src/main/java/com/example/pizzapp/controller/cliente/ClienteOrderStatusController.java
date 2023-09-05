package com.example.pizzapp.controller.cliente;

import com.example.pizzapp.model.entities.pizza.Pizza;
import com.example.pizzapp.model.entities.user.User;
import com.example.pizzapp.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ClienteOrderStatusController implements Initializable {

    @FXML
    private TableColumn<Pizza, String> colCurrentIng;

    @FXML
    private TableColumn<Pizza, String> colCurrentPizza;

    @FXML
    private TableView<Pizza> tableCurrentOrder;

    @FXML
    private Label lbStatus;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    private ObservableList<Pizza> currentOrder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String query = "SELECT * FROM Ordini WHERE utente = ? and stato != 'completato'";
        try {
            this.connect = DatabaseConnection.connectDb();
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, User.getCodUtente());
            this.rs = this.pst.executeQuery();
            if (! this.rs.next()) {
                this.lbStatus.setText("Non hai ordini in preparazione al momento");
                return;
            }
            int orderNum = this.rs.getInt("codOrdine");
            this.lbStatus.setText("Ordine numero: " + orderNum + "\t\t\t\t\t" + "Stato ordine: " + this.rs.getString("stato"));
            popolateCurrentOrder(orderNum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void popolateCurrentOrder(int orderNum) {
        this.currentOrder = FXCollections.observableArrayList();
        String query = "SELECT * FROM Composizioni WHERE ordine = ?";
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, orderNum);
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                String[] tmp = this.rs.getString("ingredienti_array").split(",");
                this.currentOrder.add((new Pizza(this.rs.getString("pizza"),
                        new ArrayList<>(Arrays.asList(tmp)))));
            }
            this.colCurrentPizza.setCellValueFactory(new PropertyValueFactory<>("name"));
            this.colCurrentIng.setCellValueFactory(new PropertyValueFactory<>("ingredienti"));
            this.tableCurrentOrder.setItems(this.currentOrder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
