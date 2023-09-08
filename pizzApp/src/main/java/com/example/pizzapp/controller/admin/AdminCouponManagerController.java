package com.example.pizzapp.controller.admin;

import com.example.pizzapp.model.entities.coupon.Coupon;
import com.example.pizzapp.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminCouponManagerController implements Initializable {

    @FXML
    private AnchorPane anchorNewCoupon;

    @FXML
    private CheckBox checkStato;

    @FXML
    private TableColumn<Coupon, Integer> colCosto;

    @FXML
    private TableColumn<Coupon, Double> colSconto;

    @FXML
    private TableColumn<Coupon, Boolean> colStato;

    @FXML
    private TableView<Coupon> tableCoupon;

    @FXML
    private TextField txtCosto;

    @FXML
    private TextField txtSconto;

    private PreparedStatement pst;

    private Connection connect;

    private Coupon selectedCoupon;

    @FXML
    public void abilitaCoupon(ActionEvent event) {
        String query = "UPDATE Coupon SET disponibile = TRUE WHERE codCoupon = ?";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, this.selectedCoupon.getCodCoupon());
            showAl(this.pst.executeUpdate());
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        popolateTable();
    }

    @FXML
    public void creaCoupon(ActionEvent event) {
        String query = "INSERT INTO Coupon (sconto, puntiRichiesti, disponibile) VALUES (?, ?, ?)";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setDouble(1, Double.parseDouble(this.txtSconto.getText()));
            this.pst.setInt(2, Integer.parseInt(this.txtCosto.getText()));
            this.pst.setBoolean(3, !(this.checkStato.isSelected()));
            showAl(this.pst.executeUpdate());
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        popolateTable();
        this.anchorNewCoupon.setVisible(false);
        this.txtCosto.clear();
        this.txtSconto.clear();
    }

    @FXML
    public void eliminaCoupon(ActionEvent event) {
        String query = "DELETE FROM Coupon WHERE codCoupon = ?";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, this.selectedCoupon.getCodCoupon());
            showAl(this.pst.executeUpdate());
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        popolateTable();
    }

    @FXML
    public void disabilitaCoupon(ActionEvent event) {
        String query = "UPDATE Coupon SET disponibile = FALSE WHERE codCoupon = ?";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, this.selectedCoupon.getCodCoupon());
            showAl(this.pst.executeUpdate());
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        popolateTable();
    }

    @FXML
    public void showNewCoupon(ActionEvent event) {
        this.anchorNewCoupon.setVisible(true);
    }

    @FXML
    public void selectCoupon() {
        this.selectedCoupon = this.tableCoupon.getSelectionModel().getSelectedItem();
    }

    public void popolateTable() {
        ObservableList<Coupon> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM Coupon";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            ResultSet rs = this.pst.executeQuery();
            while (rs.next()) {
                list.add(new Coupon(rs.getDouble("sconto"),
                        rs.getInt("puntiRichiesti"),
                        rs.getBoolean("disponibile"),
                        rs.getInt("codCoupon")));
            }
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.colSconto.setCellValueFactory(new PropertyValueFactory<>("sconto"));
        this.colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));
        this.colStato.setCellValueFactory(new PropertyValueFactory<>("attivo"));
        this.tableCoupon.setItems(list);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popolateTable();
    }
}
