package com.example.pizzapp.controller.cucina;

import com.example.pizzapp.model.entities.delivery.Delivery;
import com.example.pizzapp.model.entities.pizza.Pizza;
import com.example.pizzapp.model.entities.user.User;
import com.example.pizzapp.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CucinaDashboardController implements Initializable {

    @FXML
    private Button bttSegnaCompletato;

    @FXML
    private Button bttSegnaInPreparazione;

    @FXML
    private TableColumn<Pizza, String> colIng;

    @FXML
    private TableColumn<Pizza, String> colNome;

    @FXML
    private TableColumn<Delivery, Integer> colNumOrdine;

    @FXML
    private TableColumn<Delivery, LocalDateTime> colOrario;

    @FXML
    private RadioButton radioDaFare;

    @FXML
    private RadioButton radioInPreparazione;

    @FXML
    private ToggleGroup show;

    @FXML
    private TableView<Pizza> tableDettaglio;

    @FXML
    private TableView<Delivery> tableOrdine;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    private Delivery selectedOrder;

    @FXML
    void popolateOrdini() {
        ObservableList<Delivery> list = FXCollections.observableArrayList();
        String status = "";
        if (this.radioDaFare.isSelected()) {
            status = "In attesa";
        } else if (this.radioInPreparazione.isSelected()) {
            status = "In preparazione";
        }
        System.out.println("Mostro ordini " + status);
        String query = "SELECT *\n" +
                "FROM Ordini\n" +
                "WHERE stato = ? AND DATE(orarioRitiro) = ?\n" +
                "ORDER BY orarioRitiro ASC, orarioEffettuazione ASC;";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setString(1, status);
            this.pst.setObject(2, LocalDate.now());
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                LocalDateTime time = (LocalDateTime) this.rs.getObject("orarioRitiro");
                list.add(new Delivery(this.rs.getInt("codOrdine"), time));
            }
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.colNumOrdine.setCellValueFactory(new PropertyValueFactory<>("codOrder"));
        this.colOrario.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));
        this.tableOrdine.setItems(list);
    }

    @FXML
    void segnaCompletato(ActionEvent event) {
        changeSatus("Completato");
    }

    @FXML
    void segnaPreparazione(ActionEvent event) {
        changeSatus("In preparazione");
    }

    @FXML
    void selectOrdine(MouseEvent event) {
        this.selectedOrder = this.tableOrdine.getSelectionModel().getSelectedItem();
        popolateDettaglio();
    }

    public void popolateDettaglio() {
        ObservableList<Pizza> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM Composizioni WHERE ordine = ?";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, this.selectedOrder.getCodOrder());
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                String[] tmp = this.rs.getString("ingredienti_array").split(",");
                list.add((new Pizza(this.rs.getString("pizza"),
                        new ArrayList<>(Arrays.asList(tmp)))));
            }
            this.colNome.setCellValueFactory(new PropertyValueFactory<>("name"));
            this.colIng.setCellValueFactory(new PropertyValueFactory<>("ingredienti"));
            this.tableDettaglio.setItems(list);
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeSatus(String str) {
        String query = "UPDATE Ordini SET stato = ? WHERE codOrdine = ?";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setString(1, str);
            this.pst.setInt(2, this.selectedOrder.getCodOrder());
            showAl(this.pst.executeUpdate());
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        popolateOrdini();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.radioDaFare.setSelected(true);
        popolateOrdini();
    }

    public void showAl(int flag) {
        if (flag > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modifica effettuata");
            alert.setHeaderText(null);
            alert.setContentText("Modifica avvenuta con successo!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore modifica");
            alert.setHeaderText(null);
            alert.setContentText("Impossibile eseguire la modifica");
            alert.showAndWait();
        }
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
        this.bttSegnaCompletato.getScene().getWindow().hide();
        stage.show();
    }
}
