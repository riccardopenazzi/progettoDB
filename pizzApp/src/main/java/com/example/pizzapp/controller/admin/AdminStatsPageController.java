package com.example.pizzapp.controller.admin;

import com.example.pizzapp.model.entities.pizza.Pizza;
import com.example.pizzapp.utils.DatabaseConnection;
import com.example.pizzapp.utils.Employee;
import com.example.pizzapp.utils.Ingrediente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminStatsPageController implements Initializable {

    @FXML
    private TableColumn<Employee, Integer> colCodice;

    @FXML
    private TableColumn<Ingrediente, String> colIng;

    @FXML
    private TableColumn<Ingrediente, Integer> colIngNum;

    @FXML
    private TableColumn<Pizza, String> colPizza;

    @FXML
    private TableColumn<Pizza, Integer> colPizzaNum;

    @FXML
    private TableColumn<Employee, Double> colRitardo;

    @FXML
    private DatePicker dateSelector;

    @FXML
    private TableView<Ingrediente> tableIng;

    @FXML
    private TableView<Pizza> tablePizze;

    @FXML
    private TableView<Employee> tableRider;

    @FXML
    private TextField txtRider;

    private Employee currentRider;

    private PreparedStatement pst;

    private Connection connect;

    private ResultSet rs;

    private LocalDate start;

    private LocalDate end;

    @FXML
    void calcolaStat(ActionEvent event) {
        ObservableList<Pizza> pizzaList = FXCollections.observableArrayList();
        ObservableList<Ingrediente> ingredList = FXCollections.observableArrayList();
        String queryPizze = """
                SELECT c.pizza AS Pizza, COUNT(*) AS NumeroOrdini 
                FROM Composizioni c 
                INNER JOIN Ordini o ON c.ordine = o.codOrdine 
                WHERE o.stato = 'Completato' 
                AND DATE(o.orarioEffettuazione) BETWEEN ? AND ? 
                GROUP BY c.pizza 
                ORDER BY NumeroOrdini DESC 
                LIMIT 5;""";
        String queryIng = """
                SELECT c.ingrediente AS Ingrediente, COUNT(*) AS NumeroOrdini
                FROM Farciture c
                INNER JOIN Composizioni co ON c.pizza = co.pizza
                INNER JOIN Ordini o ON co.ordine = o.codOrdine
                WHERE o.stato = 'Completato'
                AND DATE(o.orarioEffettuazione) BETWEEN ? AND ?
                GROUP BY c.ingrediente
                ORDER BY NumeroOrdini DESC
                LIMIT 10;""";
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(queryPizze);
            this.pst.setObject(1, this.start);
            this.pst.setObject(2, this.end);
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                pizzaList.add(new Pizza(this.rs.getString("Pizza"),
                        this.rs.getInt("NumeroOrdini")));
            }
            this.pst = this.connect.prepareStatement(queryIng);
            this.pst.setObject(1, this.start);
            this.pst.setObject(2, this.end);
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                ingredList.add(new Ingrediente(this.rs.getString("Ingrediente"),
                        this.rs.getInt("NumeroOrdini")));
            }
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.colPizza.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colPizzaNum.setCellValueFactory(new PropertyValueFactory<>("count"));
        this.tablePizze.setItems(pizzaList);
        this.colIng.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colIngNum.setCellValueFactory(new PropertyValueFactory<>("count"));
        this.tableIng.setItems(ingredList);
    }

    @FXML
    void impostaFine(ActionEvent event) {
        this.end = this.dateSelector.getValue();
    }

    @FXML
    void impostaInizio(ActionEvent event) {
        this.start = this.dateSelector.getValue();
    }

    @FXML
    void selectRider(MouseEvent event) {
        this.currentRider = this.tableRider.getSelectionModel().getSelectedItem();
    }

    public void popolateTable() {
        ObservableList<Employee> riderList = FXCollections.observableArrayList();
        String query = """
                SELECT utente,
                       SUM(COALESCE(RitardoMedioOrdine1, 0) + COALESCE(RitardoMedioOrdine2, 0) + COALESCE(RitardoMedioOrdine3, 0)) AS SommaRitardoMedioTotale
                FROM (
                    SELECT utente,
                           ordine1,
                           ordine2,
                           ordine3,
                           AVG(TIMESTAMPDIFF(SECOND, orario, orarioEffettivoOrdine1)) AS RitardoMedioOrdine1,
                           AVG(TIMESTAMPDIFF(SECOND, orario, orarioEffettivoOrdine2)) AS RitardoMedioOrdine2,
                           AVG(TIMESTAMPDIFF(SECOND, orario, orarioEffettivoOrdine3)) AS RitardoMedioOrdine3
                    FROM Assegnazioni
                    WHERE orarioEffettivoOrdine1 IS NOT NULL
                       OR orarioEffettivoOrdine2 IS NOT NULL
                       OR orarioEffettivoOrdine3 IS NOT NULL
                    GROUP BY utente, ordine1, ordine2, ordine3
                ) AS RitardoMedioTabella
                GROUP BY utente;
                """;
        this.connect = DatabaseConnection.connectDb();
        try {
            this.pst = this.connect.prepareStatement(query);
            this.rs = this.pst.executeQuery();
            while (this.rs.next()) {
                riderList.add(new Employee(this.rs.getInt("utente"),
                        (this.rs.getDouble("SommaRitardoMedioTotale") / 180.0)));
            }
            this.connect.close();
            this.pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.colCodice.setCellValueFactory(new PropertyValueFactory<>("codice"));
        this.colRitardo.setCellValueFactory(new PropertyValueFactory<>("ritardo"));
        this.tableRider.setItems(riderList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popolateTable();
    }
}
