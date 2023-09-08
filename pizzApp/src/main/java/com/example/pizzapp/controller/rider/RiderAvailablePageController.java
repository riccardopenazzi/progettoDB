package com.example.pizzapp.controller.rider;

import com.example.pizzapp.model.entities.delivery.Delivery;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class RiderAvailablePageController implements Initializable {

    @FXML
    private Button bttAccept;

    @FXML
    private Button bttComplete;

    @FXML
    private ToggleGroup chooseShow;

    @FXML
    private TableColumn<Delivery, Integer> colCodOrdine;

    @FXML
    private TableColumn<Delivery, Integer> colCodUtente;

    @FXML
    private TableColumn<Delivery, LocalTime> colOrario;

    @FXML
    private TableColumn<Delivery, String> colIndirizzo;

    @FXML
    private TableColumn<Delivery, LocalTime> colOrarioEffettivo;

    @FXML
    private RadioButton radioShowAvailable;

    @FXML
    private RadioButton radioShowCurrent;

    @FXML
    private RadioButton radioShowMade;

    @FXML
    private TableView<Delivery> tableAvailable;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    private Delivery selectedDelivery;

    @FXML
    void acceptDelivery(ActionEvent event) {
        String queryCheck = "SELECT * FROM Assegnazioni WHERE utente = ? AND orario = ?";
        Boolean flag = false;
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(queryCheck);
            this.pst.setInt(1, User.getCodUtente());
            this.pst.setObject(2, this.selectedDelivery.getDeliveryTime());
            this.rs = this.pst.executeQuery();
            if (this.rs.next()) {
                int counter = this.rs.getInt("contatore");
                if (counter < 3) { //controllo non proprio necessario ma comunque utile
                    String selString = "ordine" + Integer.toString(counter + 1);
                    String queryUpdate = "UPDATE Assegnazioni SET " + selString + " = ? , contatore = ? " +
                            "WHERE utente = ? AND orario = ?";
                    this.pst = this.connect.prepareStatement(queryUpdate);
                    this.pst.setInt(1, this.selectedDelivery.getCodOrder());
                    this.pst.setInt(2, counter + 1);
                    this.pst.setInt(3, User.getCodUtente());
                    this.pst.setObject(4, this.selectedDelivery.getDeliveryTime());
                    this.pst.executeUpdate();
                    flag = true;
                }
            } else {
                String queryInsert = "INSERT INTO Assegnazioni (utente, ordine1, orario) " +
                        "VALUES (?, ?, ?)";
                this.pst = this.connect.prepareStatement(queryInsert);
                this.pst.setInt(1, User.getCodUtente());
                this.pst.setInt(2, this.selectedDelivery.getCodOrder());
                this.pst.setObject(3, this.selectedDelivery.getDeliveryTime());
                this.pst.executeUpdate();
                flag = true;
            }
            if (flag) {
                String querySetAssegnata = "UPDATE Ordini SET assegnata = TRUE WHERE codOrdine = ?";
                this.pst = this.connect.prepareStatement(querySetAssegnata);
                this.pst.setInt(1, this.selectedDelivery.getCodOrder());
                this.pst.executeUpdate();
            }
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        popolateTable();
    }

    @FXML
    void completeDelivery(ActionEvent event) {
        String queryTempi = "INSERT IGNORE into Tempi (orario) VALUES (?)";
        LocalDateTime eff = LocalDateTime.now();
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(queryTempi);
            this.pst.setObject(1, eff);
            this.pst.executeUpdate();

            String query = "SELECT * FROM Assegnazioni WHERE utente = ? AND orario = ? ";
            this.pst = this.connect.prepareStatement(query);
            this.pst.setInt(1, User.getCodUtente());
            this.pst.setObject(2, this.selectedDelivery.getDeliveryTime());
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                int cod1 = this.rs.getInt("ordine1");
                int cod2 = this.rs.getInt("ordine2");
                int cod3 = this.rs.getInt("ordine3");
                String secondQuery = "UPDATE Assegnazioni SET ";
                if(this.selectedDelivery.getCodOrder() == cod1) {
                    secondQuery = secondQuery + "orarioEffettivoOrdine1 = ? ";
                } else if (this.selectedDelivery.getCodOrder() == cod2) {
                    secondQuery = secondQuery + "orarioEffettivoOrdine2 = ? ";
                } else if (this.selectedDelivery.getCodOrder() == cod3) {
                    secondQuery = secondQuery + "orarioEffettivoOrdine3 = ? ";
                }
                secondQuery = secondQuery + "WHERE utente = ? AND orario = ? ";
                this.pst = this.connect.prepareStatement(secondQuery);
                this.pst.setObject(1, eff);
                this.pst.setInt(2, User.getCodUtente());
                this.pst.setObject(3, this.selectedDelivery.getDeliveryTime());
                this.pst.executeUpdate();
            }
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        popolateTable();
    }

    public void popolateTable() {
        ObservableList<Delivery> available = FXCollections.observableArrayList();
        if (this.radioShowAvailable.isSelected()) {
            String query = "SELECT * FROM Ordini WHERE tipo = 'consegna' AND assegnata = false AND DATE(orarioRitiro) = ?;";
            this.connect = DatabaseConnection.connectDb();
            try {
                this.pst = this.connect.prepareStatement(query);
                this.pst.setObject(1, LocalDate.now());
                this.rs = this.pst.executeQuery();
                while (this.rs.next()) {
                    //System.out.println("Scorro i risultati");
                    LocalDateTime time = (LocalDateTime) this.rs.getObject("orarioRitiro");
                    int codCons = this.rs.getInt("codOrdine");
                    available.add(new Delivery(this.rs.getInt("utente"),
                            codCons, time, getAddr(codCons)));
                }
                this.colCodOrdine.setCellValueFactory(new PropertyValueFactory<>("codOrder"));
                this.colCodUtente.setCellValueFactory(new PropertyValueFactory<>("codCostumer"));
                this.colOrario.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));
                this.colIndirizzo.setCellValueFactory(new PropertyValueFactory<>("dest"));
                this.tableAvailable.setItems(available);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (this.radioShowCurrent.isSelected()) {
            String query = "SELECT utente AS id_rider, ordine1 AS numero_consegna, orario AS orario_previsto, orarioEffettivoOrdine1 AS orario_effettivo\n" +
                    "FROM Assegnazioni\n" +
                    "WHERE ordine1 IS NOT NULL AND orarioEffettivoOrdine1 IS NULL AND utente = ?\n" +
                    "UNION ALL\n" +
                    "SELECT utente AS id_rider, ordine2 AS numero_consegna, orario AS orario_previsto, orarioEffettivoOrdine2 AS orario_effettivo\n" +
                    "FROM Assegnazioni\n" +
                    "WHERE ordine2 IS NOT NULL AND orarioEffettivoOrdine2 IS NULL AND utente = ?\n" +
                    "UNION ALL\n" +
                    "SELECT utente AS id_rider, ordine3 AS numero_consegna, orario AS orario_previsto, orarioEffettivoOrdine3 AS orario_effettivo\n" +
                    "FROM Assegnazioni\n" +
                    "WHERE ordine3 IS NOT NULL AND orarioEffettivoOrdine3 IS NULL AND utente = ?;";
            this.connect = DatabaseConnection.connectDb();
            try {
                this.pst = this.connect.prepareStatement(query);
                this.pst.setInt(1, User.getCodUtente());
                this.pst.setInt(2, User.getCodUtente());
                this.pst.setInt(3, User.getCodUtente());
                this.rs = this.pst.executeQuery();
                while (this.rs.next()) {
                    //System.out.println("Scorro i risultati");
                    LocalDateTime time = (LocalDateTime) this.rs.getObject("orario_previsto");
                    int codCons = this.rs.getInt("numero_consegna");
                    available.add(new Delivery(this.rs.getInt("id_rider"),
                            codCons, time, getAddr(codCons)));
                }
                this.colCodOrdine.setCellValueFactory(new PropertyValueFactory<>("codOrder"));
                this.colCodUtente.setCellValueFactory(new PropertyValueFactory<>("codCostumer"));
                this.colOrario.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));
                this.colIndirizzo.setCellValueFactory(new PropertyValueFactory<>("dest"));
                this.tableAvailable.setItems(available);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (this.radioShowMade.isSelected()) {
            String query = "SELECT utente AS id_rider, ordine1 AS numero_consegna, orario AS orario_previsto, orarioEffettivoOrdine1 AS orario_effettivo\n" +
                    "FROM Assegnazioni\n" +
                    "WHERE ordine1 IS NOT NULL AND orarioEffettivoOrdine1 IS NOT NULL AND utente = ?\n" +
                    "UNION ALL\n" +
                    "SELECT utente AS id_rider, ordine2 AS numero_consegna, orario AS orario_previsto, orarioEffettivoOrdine2 AS orario_effettivo\n" +
                    "FROM Assegnazioni\n" +
                    "WHERE ordine2 IS NOT NULL AND orarioEffettivoOrdine2 IS NOT NULL AND utente = ?\n" +
                    "UNION ALL\n" +
                    "SELECT utente AS id_rider, ordine3 AS numero_consegna, orario AS orario_previsto, orarioEffettivoOrdine3 AS orario_effettivo\n" +
                    "FROM Assegnazioni\n" +
                    "WHERE ordine3 IS NOT NULL AND orarioEffettivoOrdine3 IS NOT NULL AND utente = ?;";
            this.connect = DatabaseConnection.connectDb();
            try {
                this.pst = this.connect.prepareStatement(query);
                this.pst.setInt(1, User.getCodUtente());
                this.pst.setInt(2, User.getCodUtente());
                this.pst.setInt(3, User.getCodUtente());
                this.rs = this.pst.executeQuery();
                while (this.rs.next()) {
                    //System.out.println("Scorro i risultati");
                    LocalDateTime time = (LocalDateTime) this.rs.getObject("orario_previsto");
                    LocalDateTime time2 = (LocalDateTime) this.rs.getObject("orario_effettivo");
                    int codCons = this.rs.getInt("numero_consegna");
                    available.add(new Delivery(this.rs.getInt("id_rider"),
                            codCons, time, time2, getAddr(codCons)));
                }
                this.colCodOrdine.setCellValueFactory(new PropertyValueFactory<>("codOrder"));
                this.colCodUtente.setCellValueFactory(new PropertyValueFactory<>("codCostumer"));
                this.colOrario.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));
                this.colOrarioEffettivo.setCellValueFactory(new PropertyValueFactory<>("effectiveTime"));
                this.colIndirizzo.setCellValueFactory(new PropertyValueFactory<>("dest"));
                this.tableAvailable.setItems(available);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void selectDelivery() {
        this.selectedDelivery = this.tableAvailable.getSelectionModel().getSelectedItem();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popolateTable();
    }

    public String getAddr(int cod) {
        String query = "SELECT via, numero, comune FROM Destinazioni WHERE ordine = ?";
        String addr = "";
        try {
            PreparedStatement tmpPst = this.connect.prepareStatement(query);
            tmpPst.setInt(1, cod);
            ResultSet tmpRs = tmpPst.executeQuery();
            while (tmpRs.next()) {
                addr = tmpRs.getString("via") + " " + tmpRs.getInt("numero") +
                        " " + tmpRs.getString("comune");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Ritorno questo indirizzo " + addr);
        return addr;
    }
}
