package com.example.pizzapp.controller.cliente;

import com.example.pizzapp.model.entities.review.Review;
import com.example.pizzapp.model.entities.user.User;
import com.example.pizzapp.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClienteReviewController implements Initializable {

    @FXML
    private Button bttConferma;

    @FXML
    private TableColumn<Review, String> colCommento;

    @FXML
    private TableColumn<Review, Integer> colStelle;

    @FXML
    private ComboBox<Integer> comboStelle;

    @FXML
    private TableView<Review> tableRecensione;

    @FXML
    private TextArea txtCommento;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    @FXML
    void addRecensione(ActionEvent event) {
        String query = "INSERT INTO Recensioni (stelle, commento, utente) VALUES (?, ?, ?)";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, this.comboStelle.getSelectionModel().getSelectedItem());
            this.pst.setString(2, this.txtCommento.getText());
            this.pst.setInt(3, User.getCodUtente());
            this.pst.executeUpdate();
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        popolateTable();
    }

    public void popolateTable() {
        ObservableList<Review> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM Recensioni";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                list.add(new Review(this.rs.getInt("codRecensione"),
                        this.rs.getInt("utente"),
                        this.rs.getInt("stelle"),
                        this.rs.getString("commento")));
            }
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.colStelle.setCellValueFactory(new PropertyValueFactory<>("stelle"));
        this.colCommento.setCellValueFactory(new PropertyValueFactory<>("commento"));
        this.colCommento.setCellFactory(tc -> {
            TableCell<Review, String> cell = new TableCell<Review, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                        setWrapText(true);
                    }
                }
            };
            return cell;
        });
        this.tableRecensione.setItems(list);
    }

    public void popolateCombo() {
        ObservableList<Integer> list = FXCollections.observableArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        this.comboStelle.setItems(list);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popolateTable();
        popolateCombo();
    }
}
