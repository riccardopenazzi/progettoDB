package com.example.pizzapp.controller.cliente;

import com.example.pizzapp.controller.AddOrderController;
import com.example.pizzapp.model.entities.coupon.Coupon;
import com.example.pizzapp.model.entities.user.User;
import com.example.pizzapp.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientePopupCouponController implements Initializable {

    @FXML
    private Button bttAnnulla;

    @FXML
    private Button bttUseCoupon;

    @FXML
    private TableColumn<Coupon, Integer> colPrezzo;

    @FXML
    private TableColumn<Coupon, Double> colSconto;

    @FXML
    private Label lbPunti;

    @FXML
    private TableView<Coupon> tableCoupon;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    private Coupon selectedCoupon;

    @FXML
    void annulla(ActionEvent event) {
        this.bttAnnulla.getScene().getWindow().hide();
    }

    @FXML
    void useCoupon(ActionEvent event) {
        if (Integer.parseInt(lbPunti.getText()) >= this.selectedCoupon.getCosto()) {
            String queryCheck = "INSERT INTO Utilizzi_coupon (utente, coupon) VALUES (?, ?)";
            this.connect = DatabaseConnection.connectDb();
            try {
                this.pst = this.connect.prepareStatement(queryCheck);
                this.pst.setInt(1, User.getCodUtente());
                this.pst.setInt(2, this.selectedCoupon.getCodCoupon());
                this.pst.executeUpdate();
                AddOrderController.setSconto(this.selectedCoupon.getSconto());
                String queryUpdatePoint = "UPDATE Carte_fedelta\n" +
                        "SET punti = ? \n" +
                        "WHERE codCarta IN (SELECT cartaFedelta FROM Appartenenze WHERE utente = ? );";
                this.pst = this.connect.prepareStatement(queryUpdatePoint);
                this.pst.setInt(1, (int) (Double.parseDouble(this.lbPunti.getText()) - this.selectedCoupon.getCosto()));
                this.pst.setInt(2, User.getCodUtente());
                this.pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.bttUseCoupon.getScene().getWindow().hide();
        }
    }

    @FXML
    public void selectCoupon() {
        this.selectedCoupon = this.tableCoupon.getSelectionModel().getSelectedItem();
    }

    public int getPunti() {
        String query = "SELECT C.punti\n" +
                "FROM Utenti U\n" +
                "INNER JOIN Appartenenze A ON U.codUtente = A.utente\n" +
                "INNER JOIN Carte_fedelta C ON A.cartaFedelta = C.codCarta\n" +
                "WHERE U.codUtente = ?;";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, User.getCodUtente());
            this.rs = this.pst.executeQuery();
            if (this.rs.next()) {
                return this.rs.getInt("punti");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public void popolateTable() {
        ObservableList<Coupon> list = FXCollections.observableArrayList();
        String query = "SELECT C.*\n" +
                "FROM Coupon C\n" +
                "LEFT JOIN Utilizzi_coupon U ON C.codCoupon = U.coupon AND U.utente = ?\n" +
                "WHERE C.disponibile = TRUE\n" +
                "  AND U.utente IS NULL;\n";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, User.getCodUtente());
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                list.add(new Coupon(this.rs.getDouble("sconto"),
                        this.rs.getInt("puntiRichiesti"),
                        this.rs.getBoolean("disponibile"),
                        this.rs.getInt("codCoupon")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.colSconto.setCellValueFactory(new PropertyValueFactory<>("sconto"));
        this.colPrezzo.setCellValueFactory(new PropertyValueFactory<>("costo"));
        this.tableCoupon.setItems(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int punti = getPunti();
        if (punti > -1) {
            this.lbPunti.setText(String.valueOf(punti));
        } else {
            this.lbPunti.setText("Errore");
        }
        popolateTable();
    }
}

