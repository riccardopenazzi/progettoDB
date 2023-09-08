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

    @FXML
    private TableColumn<Pizza, String> colPastIng;

    @FXML
    private TableColumn<Pizza, String> colPastNome;

    @FXML
    private TableView<Pizza> tabelPastOrder;

    @FXML
    private TableColumn<Pizza, Double> colOrderNumber;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    private ObservableList<Pizza> currentOrder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("INIZIO");
        popolatePastOrder();
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
            this.connect.close();
            this.pst.close();
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

    public void popolatePastOrder() {
        System.out.println("CHIAMATA");
        ObservableList<Pizza> list = FXCollections.observableArrayList();
        String query = "SELECT\n" +
                "    o.codOrdine,\n" +
                "    o.totale,\n" +
                "    o.stato,\n" +
                "    o.utente,\n" +
                "    o.orarioRitiro,\n" +
                "    o.orarioEffettuazione,\n" +
                "    c.pizza,\n" +
                "    c.ingredienti_array\n" +
                "FROM\n" +
                "    Ordini AS o\n" +
                "JOIN\n" +
                "    Composizioni AS c ON o.codOrdine = c.ordine\n" +
                "JOIN\n" +
                "    Pizze AS p ON c.pizza = p.nome\n" +
                "LEFT JOIN\n" +
                "    Ingredienti AS i ON c.ingredienti_array = i.nome\n" +
                "WHERE\n" +
                "    o.stato = 'Completato'\n" +
                "    AND o.utente = ?; ";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, User.getCodUtente());
            this.rs = this.pst.executeQuery();
            if(this.rs.next()) {
                System.out.println("Qualcosa c'è");
            }
            while (this.rs.next()) {
                System.out.println("Scorro risultati");
                String[] tmp = this.rs.getString("ingredienti_array").split(",");
                list.add((new Pizza(this.rs.getString("pizza"),
                        (double) this.rs.getInt("codOrdine"),
                        new ArrayList<>(Arrays.asList(tmp)))));
            }
            this.colCurrentPizza.setCellValueFactory(new PropertyValueFactory<>("name"));
            this.colPastIng.setCellValueFactory(new PropertyValueFactory<>("ingredienti"));
            this.colOrderNumber.setCellValueFactory(new PropertyValueFactory<>("price"));
            this.tabelPastOrder.setItems(list);
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
