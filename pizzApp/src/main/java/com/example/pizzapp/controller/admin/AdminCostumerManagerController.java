package com.example.pizzapp.controller.admin;
import com.example.pizzapp.utils.DatabaseConnection;
import com.example.pizzapp.utils.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminCostumerManagerController implements Initializable {

    @FXML
    private TableColumn<Employee, Integer> colCod;

    @FXML
    private TableColumn<Employee, String> colCognome;

    @FXML
    private TableColumn<Employee, String> colEmail;

    @FXML
    private TableColumn<Employee, String> colNome;

    @FXML
    private TableColumn<Employee, String> colTipo;

    @FXML
    private TableView<Employee> tableCostumer;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    private Employee selected;

    @FXML
    void blocca(ActionEvent event) {
        cambiaStato("bloccato");
    }

    @FXML
    void sblocca(ActionEvent event) {
        cambiaStato("cliente");
    }

    public void cambiaStato(String str) {
        String query = "UPDATE Utenti SET tipo = ? WHERE codUtente = ?";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.pst.setString(1, str);
            this.pst.setInt(2, selected.getCodice());
            int check = this.pst.executeUpdate();
            if (check > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Modifica effettuata");
                alert.setHeaderText(null);
                if (str.equals("bloccato")) {
                    alert.setContentText("Modifica avvenuta con successo, hai bloccato l'account " +
                            selected.getEmail());
                } else {
                    alert.setContentText("Modifica avvenuta con successo, hai sbloccato l'account " +
                            selected.getEmail());
                }
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore modifica");
                alert.setHeaderText(null);
                alert.setContentText("Errore nella modifica");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        popolateTable();
    }

    @FXML
    void selectCostumer(MouseEvent event) {
        this.selected = this.tableCostumer.getSelectionModel().getSelectedItem();
    }

    public void popolateTable() {
        ObservableList<Employee> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM Utenti WHERE tipo = 'cliente' OR tipo = 'bloccato'";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                list.add(new Employee(this.rs.getString("nome"), this.rs.getString("cognome"),
                        this.rs.getString("email"), this.rs.getInt("codUtente"),
                        this.rs.getString("tipo")));
            }
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.colCod.setCellValueFactory(new PropertyValueFactory<>("codice"));
        this.colTipo.setCellValueFactory(new PropertyValueFactory<>("ruolo"));
        this.colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        this.colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.tableCostumer.setItems(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popolateTable();
    }
}
